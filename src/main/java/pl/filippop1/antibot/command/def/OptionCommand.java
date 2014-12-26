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
import pl.filippop1.antibot.option.Option;

public class OptionCommand extends Command {
    public OptionCommand() {
        super("option", "Zarzadzanie rodzajami ochrony serwera", "<default|get|list|set> [option [enable|disable]]",
                new String[] {"options", "opt"});
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            throw new CommandException(this.getUsage());
        } else {
            switch (args[1].toLowerCase()) {
                case "default": case "def": case "config": case "cfg":
                    this.fromConfig(sender);
                    break;
                case "get":
                    this.get(sender, args);
                    break;
                case "list":
                    this.list(sender);
                    break;
                case "set":
                    this.set(sender, args);
                    break;
                default: throw new CommandException(this.getUsage());
            }
        }
    }
    
    private String boolToString(boolean bool, boolean colors) {
        String color = "", result;
        if (bool) {
            if (colors) {
                color = ChatColor.GREEN.toString();
            }
            result = "wlaczony";
        } else {
            if (colors) {
                color = ChatColor.RED.toString();
            }
            result = "wylaczony";
        }
        return color + result;
    }
    
    private void fromConfig(CommandSender sender) throws CommandException {
        if (!sender.hasPermission("antibot.option.default")) {
            throw new PermissionException("antibot.option.default");
        } else {
            long ms = System.currentTimeMillis();
            AntiBotPlugin.getOptions().addDefaultOptions();
            sender.sendMessage(ChatColor.GREEN + "Pobrano domyslna ochrone z pliku config.yml w " + (System.currentTimeMillis() - ms) + " ms.");
        }
    }
    
    private void get(CommandSender sender, String[] args) throws CommandException {
        if (!sender.hasPermission("antibot.option.get")) {
            throw new PermissionException("antibot.option.get");
        } else if (args.length != 3) {
            throw new CommandException("/anti-bot option get <variable>");
        } else {
            String query = args[2].toLowerCase();
            Option option = AntiBotPlugin.getOptions().getOption(query);
            if (option == null) {
                option = AntiBotPlugin.getOptions().findOption(query);
            }
            if (option == null) {
                throw new CommandException("Zadna opcja zawierajaca " + args[2] + " nie zostala odnaleziona.");
            } else {
                sender.sendMessage(ChatColor.GOLD + option.getID() + ChatColor.YELLOW + " jest teraz "
                        + this.boolToString(option.getValue(), true) + ChatColor.YELLOW + ".");
            }
        }
    }
    
    private void list(CommandSender sender) throws CommandException {
        if (!sender.hasPermission("antibot.option.get")) {
            throw new PermissionException("antibot.option.get");
        } else {
            for (Option option : AntiBotPlugin.getOptions().getOptions()) {
                sender.sendMessage(ChatColor.GOLD + option.getID() + ChatColor.YELLOW + " jest teraz "
                        + this.boolToString(option.getValue(), true) + ChatColor.YELLOW + ".");
            }
        }
    }
    
    private void set(CommandSender sender, String[] args) throws CommandException {
        if (!sender.hasPermission("antibot.option.set")) {
            throw new PermissionException("antibot.option.set");
        } else if (args.length != 4) {
            throw new CommandException("/anti-bot option set <variable> <enable|disable>");
        } else {
            boolean value;
            switch (args[3].toLowerCase()) {
                case "enable": case "on":
                    value = true;
                    break;
                case "disable": case "off":
                    value = false;
                    break;
                default: throw new CommandException("/anti-bot option set <variable> <enable|disable>");
            }
            
            String query = args[2].toLowerCase();
            Option option = AntiBotPlugin.getOptions().getOption(query);
            if (option == null) {
                option = AntiBotPlugin.getOptions().findOption(query);
            }
            if (option == null) {
                throw new CommandException("Zadna opcja zawierajaca " + args[2] + " nie zostala odnaleziona.");
            } else if (option.getValue() == value) {
                throw new CommandException(option.getID() + " jest juz " + this.boolToString(option.getValue(), false) + ChatColor.RED + ".");
            } else {
                long ms = System.currentTimeMillis();
                option.setValue(value);
                option.reload();
                sender.sendMessage(ChatColor.GREEN + option.getID() + " zostal " + this.boolToString(value, false)
                        + " (zajelo " + (System.currentTimeMillis() - ms) + " ms)");
                
                if (!AntiBotPlugin.isPluginEnabled()) {
                    sender.sendMessage(ChatColor.GOLD + "Plugin jest wylaczony. Zmiany nie beda widoczne na serwerze");
                    sender.sendMessage(ChatColor.GOLD + "[Porada] Aby wlaczyc plugin uzyj /anti-bot status enable.");
                }
            }
        }
    }
}
