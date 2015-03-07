package pl.filippop1.antibot.option;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import pl.filippop1.antibot.AntiBotPlugin;
import pl.filippop1.antibot.Configuration;

public class ExpiresOption extends Option implements Listener {
    private int taskID;
    private Time time;
    
    public ExpiresOption() {
        super("expires");
    }
    
    @Override
    protected void reload(boolean enabled) {
        this.taskID = 0;
        this.time = null;
        
        AntiBotPlugin plugin = (AntiBotPlugin) Bukkit.getPluginManager().getPlugin("Anti-Bot");
        if (enabled) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else {
            HandlerList.unregisterAll(this);
        }
        
        Bukkit.getScheduler().cancelTask(this.taskID);
        try {
            this.time = Time.convert(AntiBotPlugin.getConfiguration().getExpires());
        } catch (IllegalArgumentException ex) {
            Configuration.exception(ex);
        }
        
        if (enabled && this.time != null && this.time.getTicks() > 0) {
            this.taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin,
                    new ExpiresThread(this.time), 0L, 60 * 60 * 20L).getTaskId();
        }
    }
    
    private class ExpiresThread implements Runnable {
        private long now;
        private final Time time;
        
        public ExpiresThread(Time time) {
            this.time = time;
        }
        
        @Override
        public void run() {
            this.now = System.currentTimeMillis() - time.getTicks();
            this.removePlayers();
        }
        
        private boolean isPlayerFile(File file) throws FileNotFoundException {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                if (line.split("=")[0].equals("last-time")) {
                    try {
                        return this.now > Long.parseLong(line.split("=")[1]);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return false;
        }
        
        private void print(int removed, int of) {
            Bukkit.getLogger().log(Level.INFO, "Usunieto {0} graczy z {1} zarejestrowanych.",
                    new Object[] {removed, of});
        }
        
        private void removePlayers() {
            int removed = 0, of = 0;
            for (File file : AntiBotPlugin.getConfiguration().getFileFolder().listFiles()) {
                try {
                    if (this.isPlayerFile(file)) {
                        file.delete();
                        removed++;
                    }
                } catch (FileNotFoundException ex) {}
                of++;
            }
            this.print(removed, of);
        }
    }
    
    private static class Time {
        private long ticks;
        
        public Time(long ticks) {
            this.ticks = ticks;
        }
        
        public long getTicks() {
            return this.ticks;
        }
        
        public void setTicks(long ticks) {
            this.ticks = ticks;
        }
        
        public static Time convert(String string) throws IllegalArgumentException {
            String time = AntiBotPlugin.getConfiguration().getExpires();
            long ms = 0;
            try {
                ms = Integer.valueOf(time.substring(0, time.length() - 1));
            } catch (IllegalArgumentException ex) {
                Configuration.exception(ex);
            }
            
            switch (time.substring(time.length() - 1).toLowerCase()) {
                case "d":
                    ms = ms * 24;
                case "h":
                    ms = ms * 60;
                case "m":
                    ms = ms * 60;
                case "s":
                    ms = ms * 20;
                case "t":
                    break;
                default:
                    Configuration.exception(new Exception("Podany format nie jest prawidlowy"));
                    break;
            }
            return new Time(ms);
        }
    }
}
