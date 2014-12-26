/*
 * Copyright 2014 Aleksander.
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
package pl.filippop1.antibot.command.def;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.filippop1.antibot.AntiBotPlugin;
import pl.filippop1.antibot.LatestLog;
import pl.filippop1.antibot.command.Command;
import pl.filippop1.antibot.command.CommandException;
import pl.filippop1.antibot.command.PermissionException;

public class LogsCommand extends Command {
    public LogsCommand() {
        super("logs", "Pokazuje ostatnie 20 zarejestrowanych kont",
                new String[] {"log", "latest"});
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (!sender.hasPermission("antibot.log")) {
            throw new PermissionException("antibot.log");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Zbieranie najswizszych informacji, moze to zajac kilka sekund...");
            this.latest(sender);
        }
    }
    
    private void latest(final CommandSender sender) throws CommandException {
        Thread thread = new Thread(new Runnable() {
            
            @Override
            public synchronized void run() {
                List<String> accounts = new ArrayList<>();
                for (LatestLog.LatestBot bot : AntiBotPlugin.getLogs().get()) {
                    accounts.add(this.account(bot.getIP(), bot.getUUID(), bot.getNickname()));
                }
                
                if (sender != null) {
                    if (accounts.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "Brak kont do wyswietlenia.");
                    } else {
                        int id = 0;
                        for (String account : accounts) {
                            id++;
                            String suffix = "";
                            if (id == 20) {
                                suffix = ChatColor.RED + " (najnowszy z listy)";
                            }
                            sender.sendMessage(ChatColor.YELLOW.toString() + id + ". " + account + suffix);
                        }
                        if (id < 20) {
                            sender.sendMessage(ChatColor.RED + "Lista wyswietla " + id + " najnowszych kont od czasu zaladowania pluginu.");
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "[Porada] Uzyj /anti-bot status info, aby wyswietlic status kont.");
                }
            }
            
            private String account(String ip, String uuid, String nickname) {
                String ipString = "";
                if (ip != null) {
                    ipString = ChatColor.RED + ip + ChatColor.YELLOW + ": ";
                }
                return ipString + ChatColor.GOLD + uuid + ChatColor.YELLOW + " (znany jako " + ChatColor.GOLD + nickname + ChatColor.YELLOW + ")";
            }
        });
        thread.start();
    }
}
