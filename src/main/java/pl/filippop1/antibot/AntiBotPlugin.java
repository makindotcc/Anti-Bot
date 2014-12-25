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
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import pl.filippop1.antibot.command.CommandExecutor;

public class AntiBotPlugin extends JavaPlugin {
    public static String AUTHORS = "filippop1";
    public static String SOURCE_URL = "https://github.com/Thefilippop1PL/AntiBot";
    public static String VERSION = "${project.version}";
    private static Configuration configuration;
    
    @Override
    public void onEnable() {
        // Configuration
        this.saveDefaultConfig();
        configuration = new Configuration(this.getConfig());
        configuration.loadConfiguration();
        
        this.getCommand("antibot").setExecutor(new CommandExecutor());
        CommandExecutor.get().registerDefaults();
        
        // Shutdown if disabled
        if (!getConfiguration().isEnabled()) {
            this.getLogger().log(Level.INFO, "Wylaczanie...");
            this.getLogger().log(Level.INFO, "Plugin zostanie automatycznie wylaczony poniewaz opcja `enabled` jest ustawiona na `false`.");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        
        // Listeners
        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
        
        // Metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Configuration getConfiguration() {
        return configuration;
    }
}
