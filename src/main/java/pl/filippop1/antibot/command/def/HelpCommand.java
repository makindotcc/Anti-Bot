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

package pl.filippop1.antibot.command.def;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.filippop1.antibot.command.Command;
import pl.filippop1.antibot.command.CommandException;
import pl.filippop1.antibot.command.CommandExecutor;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Lista komend oraz ich pomoc", "[command]",
                new String[] {"?", "pomoc"});
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        CommandExecutor manager = CommandExecutor.get();
        if (args.length == 1) {
            this.showCommands(sender, manager);
        } else {
            String query = args[1].toLowerCase();
            Command command = null;
            int found = 0;
            String alias = null;
            for (String cmd : manager.getCommands()) {
                if (cmd.contains(query)) {
                    found++;
                    command = manager.getCommand(cmd);
                    if (!command.getName().contains(cmd)) {
                        alias = cmd;
                    } else {
                        alias = null;
                    }
                }
            }
            
            if (command == null) {
                throw new CommandException("Nie znaleziono pomocy dla komendy " + query + ".");
            } else if (found > 1) {
                sender.sendMessage(ChatColor.GREEN + "Znaleziono wiecej niz jedna komende.");
            }
            this.showCommand(sender, command, alias);
        }
    }
    
    private void showCommand(CommandSender sender, Command command, String query) {
        sender.sendMessage(ChatColor.GOLD + "---------- Pomoc " + ChatColor.YELLOW + "(dla " +
                command.getName() + ")" + ChatColor.GOLD + " ----------");
        if (query != null) {
            sender.sendMessage(ChatColor.YELLOW + query + ChatColor.GOLD + " jest aliasem do " + ChatColor.YELLOW + command.getName());
        }
        if (command.hasCustomUsage()) {
            sender.sendMessage(ChatColor.GOLD + "Uzycje: " + ChatColor.YELLOW + command.getUsage());
        }
        sender.sendMessage(ChatColor.GOLD + "Opis: " + ChatColor.YELLOW + command.getDescription());
        if (command.hasAliases()) {
            sender.sendMessage(ChatColor.GOLD + "Aliasy: " + ChatColor.YELLOW + this.aliases(command.getAliases()));
        }
    }
    
    private void showCommands(CommandSender sender, CommandExecutor manager) {
        int commands = 0;
        for (String cmd : manager.getCommands()) {
            if (cmd.equals(manager.getCommand(cmd).getName())) {
                commands++;
            }
        }
        
        sender.sendMessage(ChatColor.GOLD + "---------- Pomoc " + ChatColor.YELLOW + "(" +
                commands + " komend)" + ChatColor.GOLD + " ----------" );
        for (String cmd : manager.getCommands()) {
            Command command = manager.getCommand(cmd);
            if (cmd.equals(command.getName())) {
                sender.sendMessage(ChatColor.GOLD + command.getUsage() + ChatColor.GRAY + " - " + command.getDescription());
            }
        }
        sender.sendMessage(ChatColor.GREEN + "[Porada] Uzyj /anti-bot help <command>, aby otrzymac dokladne informacje o komendzie");
    }
    
    private String aliases(String[] aliases) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < aliases.length; i++) {
            if (i != 0) {
                builder.append(ChatColor.GOLD);
                if (aliases.length == (i + 1)) {
                    builder.append(" oraz ");
                } else {
                    builder.append(", ");
                }
            }
            builder.append(ChatColor.YELLOW).append(aliases[i]);
        }
        return builder.toString();
    }
}
