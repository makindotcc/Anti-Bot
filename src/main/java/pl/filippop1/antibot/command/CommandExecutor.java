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

package pl.filippop1.antibot.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.filippop1.antibot.command.def.OptionCommand;
import pl.filippop1.antibot.command.def.HelpCommand;
import pl.filippop1.antibot.command.def.LogsCommand;
import pl.filippop1.antibot.command.def.StatusCommand;
import pl.filippop1.antibot.command.def.VersionCommand;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private static CommandExecutor instance;
    private final Map<String, Command> commandMap;
    
    public CommandExecutor() {
        instance = this;
        this.commandMap = new HashMap<>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("anti-bot")) {
            if (args.length == 0) {
                this.execute(sender, new String[] {"help"});
            } else {
                this.execute(sender, args);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public void execute(CommandSender sender, String[] args) {
        String name = args[0].toLowerCase();
        if (this.commandMap.containsKey(name)) {
            Command command = this.commandMap.get(name);
            try {
                command.execute(sender, args);
            } catch (CommandException ex) {
                sender.sendMessage(ChatColor.RED + ex.getMessage());
                if (ex.print()) {
                    sender.sendMessage(ChatColor.RED + "Patrz na konsole (lub logi) po wiecej informacji.");
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                if (ex instanceof NumberFormatException) {
                    sender.sendMessage(ChatColor.RED + "Musisz podac liczbe!");
                } else {
                    sender.sendMessage(ChatColor.RED + "Wykryto powazny blad podczas wykonywania tej komendy.");
                    ex.printStackTrace();
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Komenda " + name + " nie zostala odnaleziona.");
            sender.sendMessage(ChatColor.GRAY + "Uzyj /anti-bot help, aby uzyskac liste komend oraz pomoc");
        }
    }
    
    public Command getCommand(String name) {
        Validate.notNull(name, "name can not be null");
        return this.commandMap.get(name.toLowerCase());
    }
    
    public Set<String> getCommands() {
        return this.commandMap.keySet();
    }
    
    public void register(Command command) {
        Validate.notNull(command, "command can not be null");
        this.commandMap.put(command.getName().toLowerCase(), command);
        if (command.hasAliases()) {
            for (String alias : command.getAliases()) {
                this.commandMap.put(alias.toLowerCase(), command);
            }
        }
    }
    
    public void registerDefaults() {
        this.register(new OptionCommand());
        this.register(new HelpCommand());
        this.register(new LogsCommand());
        this.register(new StatusCommand());
        this.register(new VersionCommand());
    }
    
    public static CommandExecutor get() {
        return instance;
    }
}
