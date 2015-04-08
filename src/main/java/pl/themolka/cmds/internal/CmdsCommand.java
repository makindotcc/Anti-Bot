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
package pl.themolka.cmds.internal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import pl.themolka.cmds.Settings;
import pl.themolka.cmds.command.Command;
import pl.themolka.cmds.command.CommandException;
import pl.themolka.cmds.command.Commands;

/**
 *
 * @author Aleksander
 */
public class CmdsCommand extends Command {
    public static final String DESCRIPTION = "Commands and other simple API for Bukkit";
    public static final String ISSUES = "https://github.com/ShootGame/CMDS/issues";
    public static final String SOURCE = "https://github.com/ShootGame/CMDS";
    
    public CmdsCommand() {
        super(new String[] {"cmds"});
        super.setDescription("General information about the CMDS library for Bukkit");
        super.setUsage("[list]");
    }
    
    @Override
    public void handle(CommandSender sender, String label, String[] args) throws CommandException {
        if (args.length > 0 && (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
            if (args.length == 1) {
                this.printCommands(sender);
            } else if (args.length == 2) {
                Plugin plugin = this.getPlugin(args[1]);
                if (plugin != null) {
                    this.printCommands(sender, plugin);
                } else {
                    throw new CommandException(args[1] + " was not found on this server.");
                }
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "======" + ChatColor.DARK_PURPLE + "=== CMDS ===" + ChatColor.YELLOW + "======");
            sender.sendMessage(ChatColor.YELLOW + CmdsCommand.DESCRIPTION);
            sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.GREEN + this.getVersion());
            sender.sendMessage(ChatColor.GOLD + "Authors: " + ChatColor.GREEN + "TheMolkaPL");
            sender.sendMessage(ChatColor.GOLD + "Source code: " + ChatColor.GREEN + CmdsCommand.SOURCE);
            sender.sendMessage(ChatColor.GOLD + "Issue tracker: " + ChatColor.GREEN + CmdsCommand.ISSUES);
        }
    }
    
    private Plugin getPlugin(String name) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getName().toLowerCase().contains(name.toLowerCase())) {
                return plugin;
            }
        }
        return null;
    }
    
    private String getVersion() {
        return Settings.VERSION;
    }
    
    private void printCommands(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "======" + ChatColor.DARK_PURPLE + "=== Command List ===" + ChatColor.YELLOW + "======");
        sender.sendMessage(ChatColor.GREEN + "You have access to the following commands:");
        
        for (Command command : Commands.getCommands()) {
            if (!command.hasPermission() || sender.hasPermission(command.getPermission())) {
                sender.sendMessage(ChatColor.GREEN + "/" + command.getName() + ChatColor.GOLD + " - " + command.getDescription());
            }
        }
    }
    
    private void printCommands(CommandSender sender, Plugin plugin) {
        sender.sendMessage(ChatColor.YELLOW + "======" + ChatColor.DARK_PURPLE + "=== " + plugin.getName() + " Command List ===" + ChatColor.YELLOW + "======");
        sender.sendMessage(ChatColor.GREEN + "You have access to the following " + plugin.getName() + " commands:");
        
        for (Command command : Commands.getCommands()) {
            if (command.getPlugin().equals(plugin)) {
                if (!command.hasPermission() || sender.hasPermission(command.getPermission())) {
                    sender.sendMessage(ChatColor.GREEN + "/" + command.getName() + ChatColor.GOLD + " - " + command.getDescription());
                }
            }
        }
    }
}
