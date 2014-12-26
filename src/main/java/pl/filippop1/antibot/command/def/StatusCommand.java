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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.filippop1.antibot.AntiBotPlugin;
import pl.filippop1.antibot.command.Command;
import pl.filippop1.antibot.command.CommandException;
import pl.filippop1.antibot.command.PermissionException;

public class StatusCommand extends Command {
    public StatusCommand() {
        super("status", "Status graczy oraz pluginu", "<enable|disable|info>");
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            throw new CommandException(this.getUsage());
        } else {
            if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on")) {
                this.enablePlugin(sender);
            } else if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off")) {
                this.disablePlugin(sender);
            } else if (args[1].equalsIgnoreCase("info")) {
                this.info(sender);
            } else {
                throw new CommandException(this.getUsage());
            }
        }
    }
    
    private void enablePlugin(CommandSender sender) throws CommandException {
        String version = AntiBotPlugin.getVersion();
        if (!sender.hasPermission("antibot.enable")) {
            throw new PermissionException("antibot.enable");
        } else if (AntiBotPlugin.isPluginEnabled()) {
            throw new CommandException("Anti-Bot v" + version + " juz jest wlaczony.");
        } else {
            long ms = System.currentTimeMillis();
            AntiBotPlugin.enable();
            sender.sendMessage(ChatColor.GREEN + "Anti-Bot v" + version + " zostal wlaczony (zajelo " + (System.currentTimeMillis() - ms) + " ms).");
        }
    }
    
    private void disablePlugin(CommandSender sender) throws CommandException {
        String version = AntiBotPlugin.getVersion();
        if (!sender.hasPermission("antibot.disable")) {
            throw new PermissionException("antibot.disable");
        } else if (!AntiBotPlugin.isPluginEnabled()) {
            throw new CommandException("Anti-Bot v" + version + " juz jest wylaczony.");
        } else {
            long ms = System.currentTimeMillis();
            AntiBotPlugin.disable();
            sender.sendMessage(ChatColor.GREEN + "Anti-Bot v" + version + " zostal wylaczony (zajelo " + (System.currentTimeMillis() - ms) + " ms).");
        }
    }
    
    private void info(CommandSender sender) throws CommandException {
        if (!sender.hasPermission("antibot.info")) {
            throw new PermissionException("antibot.info");
        } else {
            String enabledValue;
            if (AntiBotPlugin.isPluginEnabled()) {
                enabledValue = ChatColor.GREEN + "wlaczony";
            } else {
                enabledValue = ChatColor.RED + "wylaczony";
            }
            
            int accounts = AntiBotPlugin.getConfiguration().getFileFolder().listFiles().length;
            
            sender.sendMessage(ChatColor.GOLD + "Anti-Bot jest teraz " + enabledValue + ChatColor.GOLD + ".");
            sender.sendMessage(ChatColor.GOLD + "Obecnie zarejestrowanych kont: " + ChatColor.YELLOW + accounts);
            sender.sendMessage(ChatColor.GOLD + "Zarejestrowane konta od zaladowania pluginu: " + ChatColor.YELLOW + AntiBotPlugin.getRegisteredAccounts());
            sender.sendMessage(ChatColor.GREEN + "[Porada] Uzyj /anti-bot logs, aby wyswietlic najnowsze zarejestrowane konta.");
        }
    }
}
