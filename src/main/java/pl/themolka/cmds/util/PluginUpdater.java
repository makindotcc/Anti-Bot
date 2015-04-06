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
package pl.themolka.cmds.util;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Aleksander
 */
public class PluginUpdater extends Updater {
    private final Plugin plugin;
    
    public PluginUpdater(Plugin plugin) {
        Validate.notNull(plugin, "plugin can not be null");
        this.plugin = plugin;
    }
    
    public void checkForUpdates() {
        super.run();
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public String getPluginVersion() {
        return this.plugin.getDescription().getVersion();
    }
    
    public boolean isAvailable() {
        String current = this.getPluginVersion();
        if (current == null || current.equalsIgnoreCase("unknown")) {
            return false;
        }
        
        for (String version : super.getVersions()) {
            if (current.equals(version)) {
                return false;
            }
        }
        return true;
    }
}
