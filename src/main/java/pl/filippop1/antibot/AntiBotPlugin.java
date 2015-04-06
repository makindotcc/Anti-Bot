/*
 * Copyright 2014 Filip.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.filippop1.antibot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import pl.filippop1.antibot.command.CommandExecutor;
import pl.filippop1.antibot.option.OptionsManager;
import pl.themolka.cmds.Settings;
import static pl.themolka.cmds.Settings.setMessage;
import pl.themolka.cmds.util.AsyncPluginUpdater;
import pl.themolka.cmds.util.PluginUpdater;
import pl.themolka.cmds.util.UpdateFuture;

public class AntiBotPlugin extends JavaPlugin {
    public static final String AUTHORS = "filippop1 & TheMolkaPL";
    public static final String SOURCE_URL = "https://github.com/Thefilippop1PL/Anti-Bot";
    public static final String UPDATER_URL = "https://raw.githubusercontent.com/Thefilippop1PL/Anti-Bot/master/updater.txt";
    private static Configuration configuration;
    private static boolean enabled;
    private static Plugin instance;
    private static LatestLog logs;
    private static OptionsManager options;
    private static int regiseredAccounts = 0;
    private static AsyncPluginUpdater updater;
    private static String version;
    
    @Override
    public void onEnable() {
        instance = this;
        // Configuration
        this.loadConfiguration();
        
        // Logs
        logs = new LatestLog();
        
        // Version
        version = this.getDescription().getVersion();
        
        // Commands and listeners
        this.getCommand("antibot").setExecutor(new CommandExecutor());
        CommandExecutor.get().registerDefaults();
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        
        // Options
        options = new OptionsManager(this.getConfig());
        options.addDefaultOptions();
        
        // CMDS library
        Settings.setup(this);
        setMessage("console", "Musisz byc graczem w grze, aby wykonac ta komende.");
        setMessage("internal-error", "Wewnetrzny blad serwera: zobacz konsole po wiecej informacji.");
        setMessage("not-implemented", "Nie zaimplementowano jeszcze w wykonawcy.");
        setMessage("number", "Musisz podac liczbe (otrzymano tekst).");
        setMessage("permission", "Nie posiadasz uprawnien do wykonania tej komendy.");
        
        // Metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Updater
        checkForUpdates(this.getServer().getConsoleSender());
        
        // Shutdown if disabled
        if (getConfiguration().isEnabled()) {
            enable();
        } else {
            this.getLogger().log(Level.INFO, "Wylaczanie...");
            this.getLogger().log(Level.INFO, "Plugin zostanie automatycznie wylaczony poniewaz opcja `enabled` jest ustawiona na `false`.");
            this.getLogger().log(Level.INFO, "Mozesz go wlaczyc zmieniajac ustawienie `enabled` na `true` lub wpisujac komende /anti-bot status enable.");
            disable();
        }
    }
    
    @Override
    public void onDisable() {
        // CMDS library
        Settings.destroy();
    }
    
    public static void addRegisteredAccount() {
        regiseredAccounts++;
    }
    
    public static void checkForUpdates(final CommandSender sender) {
        try {
            updater = new AsyncPluginUpdater(instance);
            updater.setURL(new URL(AntiBotPlugin.UPDATER_URL));
            updater.asyncCheckForUpdates(new UpdateFuture() {
                
                @Override
                public void handle(PluginUpdater updater) {
                    if (updater.getVersions().isEmpty()) {
                        sender.sendMessage("Nie udalo sie pobrac aktualizacji. Host nie odpowiada?");
                        sender.sendMessage("Sprawdz ponownie za kilka chwil uzywajac komende /anti-bot updater check.");
                    } else if (updater.isAvailable()) {
                        sender.sendMessage(String.format("Znaleziono aktualizacje! Obecna - %s, najnowsza %s.",
                                new Object[] {updater.getPluginVersion(), updater.getVersions().get(0)}));
                    } else {
                        sender.sendMessage("Plugin jest aktualny z najnowsza wersja.");
                    }
                }
            });
        } catch (MalformedURLException ex) {
            Logger.getLogger(AntiBotPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void disable() {
        enabled = false;
        getOptions().reloadAll();
    }
    
    public static void enable() {
        enabled = true;
        getOptions().reloadAll();
    }
    
    public static Configuration getConfiguration() {
        return configuration;
    }
    
    public static LatestLog getLogs() {
        return logs;
    }
    
    public static OptionsManager getOptions() {
        return options;
    }
    
    public static int getRegisteredAccounts() {
        return regiseredAccounts;
    }
    
    public static PluginUpdater getUpdater() {
        return updater;
    }
    
    public static String getVersion() {
        return version;
    }
    
    public static boolean isPluginEnabled() {
        return enabled;
    }
    
    public void loadConfiguration() {
        this.saveDefaultConfig();
        configuration = new Configuration(this.getConfig());
        configuration.loadConfiguration();
        
        enabled = configuration.isEnabled();
    }
}
