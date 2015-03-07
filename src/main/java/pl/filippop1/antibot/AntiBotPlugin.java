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
import pl.filippop1.antibot.option.OptionsManager;

public class AntiBotPlugin extends JavaPlugin {
    public static String AUTHORS = "filippop1 & TheMolkaPL";
    public static String SOURCE_URL = "https://github.com/Thefilippop1PL/Anti-Bot";
    private static Configuration configuration;
    private static boolean enabled;
    private static LatestLog logs;
    private static OptionsManager options;
    private static int regiseredAccounts = 0;
    private static String version;
    
    @Override
    public void onEnable() {
        // Configuration
        this.loadConfiguration();
        
        // Logs
        logs = new LatestLog();
        
        // Version
        version = this.getDescription().getVersion();
        
        // Commands and listeners
        this.getCommand("antibot").setExecutor(new CommandExecutor());
        CommandExecutor.get().registerDefaults();
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        
        // Options
        options = new OptionsManager(this.getConfig());
        options.addDefaultOptions();
        
        // Metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Shutdown if disabled
        if (getConfiguration().isEnabled()) {
            enable();
        } else {
            this.getLogger().log(Level.INFO, "Wylaczanie...");
            this.getLogger().log(Level.INFO, "Plugin zostanie automatycznie wylaczony poniewaz opcja `enabled` jest ustawiona na `false`.");
            this.getLogger().log(Level.INFO, "Mozesz go wlaczyc zmieniajac ustawienie `enabled` na `true` lub wpisujac komende /anti-bot status enable.");
            disable();
        }
    }
    
    public static void addRegisteredAccount() {
        regiseredAccounts++;
    }
    
    public static void enable() {
        enabled = true;
        getOptions().reloadAll();
    }
    
    public static void disable() {
        enabled = false;
        getOptions().reloadAll();
    }
    
    public static Configuration getConfiguration() {
        return configuration;
    }
    
    public static LatestLog getLogs() {
        return logs;
    }
    
    public static OptionsManager getOptions() {
        return options;
    }
    
    public static int getRegisteredAccounts() {
        return regiseredAccounts;
    }
    
    public static String getVersion() {
        return version;
    }
    
    public static boolean isPluginEnabled() {
        return enabled;
    }
    
    public void loadConfiguration() {
        this.saveDefaultConfig();
        configuration = new Configuration(this.getConfig());
        configuration.loadConfiguration();
        
        enabled = configuration.isEnabled();
    }
}
