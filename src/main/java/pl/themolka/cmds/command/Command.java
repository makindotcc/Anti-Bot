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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.themolka.cmds.Settings;

/**
 *
 * @author Aleksander
 */
public class Command extends CommandOptions implements Executable {
    private final String[] aliases;
    private String description;
    private String permission;
    private String usage;
    
    public Command(String[] aliases) {
        Validate.notNull(aliases, "aliases can not be null");
        Validate.notEmpty(aliases, "aliases can not be empty");
        this.aliases = aliases;
    }
    
    @Override
    public List<String> completer(CommandSender sender, String label, String[] args) {
        return null;
    }
    
    @Override
    public void handle(CommandSender sender, String label, String[] args) throws CommandException {
        if (sender instanceof Player) {
            this.handle((Player) sender, label, args);
        } else {
            throw new CommandException(Settings.getMessage("console"));
        }
    }
    
    @Override
    public void handle(Player player, String label, String[] args) throws CommandException {
        throw new UnsupportedOperationException(Settings.getMessage("not-implemented"));
    }
    
    public String[] getAliases() {
        return this.aliases;
    }
    
    public String getDescription() {
        if (this.hasDescription()) {
            return this.description;
        } else {
            return "Not available yet.";
        }
    }
    
    public String getName() {
        return this.getAliases()[0];
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public String getUsage() {
        String result = "/" + this.getName();
        if (this.hasCustomUsage()) {
            result += " " + this.usage;
        }
        return result;
    }
    
    public boolean hasCustomUsage() {
        return this.usage != null;
    }
    
    public boolean hasDescription() {
        return this.description != null;
    }
    
    public boolean hasPermission() {
        return this.permission != null;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    public void setUsage(String usage) {
        this.usage = usage;
    }
}
