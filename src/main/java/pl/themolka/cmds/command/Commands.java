/*
 * Copyright 2015 Aleksander.
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
package pl.themolka.cmds.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import pl.themolka.cmds.Settings;

/**
 *
 * @author Aleksander
 */
public class Commands {
    private static final List<Command> commands = new ArrayList<>();
    
    public static Command getCommand(String alias) {
        Validate.notNull(alias, "alias can not be null");
        for (Command command : getCommands()) {
            for (String al : command.getAliases()) {
                if (al.equalsIgnoreCase(alias)) {
                    return command;
                }
            }
        }
        return null;
    }
    
    public static List<Command> getCommands() {
        return commands;
    }
    
    public static void perform(String command, CommandSender sender, String[] args) {
        Validate.notNull(command, "command can not be null");
        Validate.notNull(sender, "sender can not be null");
        if (args == null) {
            args = new String[] {};
        }
        
        Command cmd = getCommand(command);
        if (cmd != null) {
            try {
                if (!cmd.hasPermission() || sender.hasPermission(cmd.getPermission())) {
                    cmd.handle(sender, command.toLowerCase(), args);
                } else {
                    throw new PermissionException();
                }
            } catch (Exception ex) {
                if (ex instanceof UsageException) {
                    if (ex.getMessage() != null) {
                        sender.sendMessage(ChatColor.RED + ex.getMessage());
                    }
                    sender.sendMessage(ChatColor.RED + cmd.getUsage());
                } else if (ex instanceof CommandException) {
                    sender.sendMessage(ChatColor.RED + ex.getMessage());
                } else if (ex instanceof NumberFormatException) {
                    sender.sendMessage(ChatColor.RED + Settings.getMessage("number"));
                } else {
                    Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
                    sender.sendMessage(ChatColor.RED + Settings.getMessage("internal-error"));
                }
            }
        }
    }
    
    public static List<String> performCompleter(String command, CommandSender sender, String[] args) {
        Validate.notNull(command, "command can not be null");
        Validate.notNull(sender, "sender can not be null");
        if (args == null) {
            args = new String[] {};
        }
        
        Command cmd = getCommand(command);
        if (cmd != null) {
            return cmd.completer(sender, command.toLowerCase(), args);
        } else {
            return null;
        }
    }
    
    public static void register(Plugin plugin, Class<? extends Command>... command) {
        Validate.notNull(plugin, "plugin can not be null");
        Validate.notNull(command, "command can not be null");
        
        for (Class<? extends Command> cmdClass : command) {
            try {
                Command cmd = cmdClass.newInstance();
                commands.add(cmd);
                
                org.bukkit.command.Command bCommand = new CommandPerformer(BukkitCommandExecutor.getInstance(), cmd.getName());
                bCommand.setAliases(Arrays.asList(cmd.getAliases()));
                bCommand.setDescription(cmd.getDescription());
                bCommand.setLabel(cmd.getName());
                if (cmd.hasPermission()) {
                    bCommand.setPermission(cmd.getPermission());
                }
                bCommand.setUsage(cmd.getUsage());
                
                inject(bCommand, plugin.getName());
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void inject(org.bukkit.command.Command cmd, String pluginName) {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            if (field != null) {
                field.setAccessible(true);
                CommandMap map = (CommandMap) field.get(Bukkit.getServer());
                map.register(cmd.getName(), pluginName, cmd);
            }
        } catch (NoSuchFieldException
                | SecurityException
                | IllegalArgumentException
                | IllegalAccessException ex) {
            Logger.getLogger(BukkitCommandExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static class CommandPerformer extends org.bukkit.command.Command {
        private final BukkitCommandExecutor executor;
        
        public CommandPerformer(BukkitCommandExecutor executor, String command) {
            super(command);
            this.executor = executor;
        }
        
        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            return this.executor.onCommand(sender, this, label, args);
        }
        
        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            return this.executor.onTabComplete(sender, this, alias, args);
        }
    }
}
