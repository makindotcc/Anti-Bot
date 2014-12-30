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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
    private final FileConfiguration file;
    private File fileFolder;
    private boolean enabled;
    private String kickMessage;
    private List<String> blockedCmds;
    private String blockedCmdsMsg;
    
    public Configuration(FileConfiguration file) {
        Validate.notNull(file, "file can not be null");
        this.file = file;
    }
    
    public List<String> getBlockedCmds() {
        return this.blockedCmds;
    }
    
    public String getBlockedCmdsMsg() {
        return this.blockedCmdsMsg;
    }
    
    public File getFileFolder() {
        return fileFolder;
    }
    
    public String getKickMessage() {
        return this.kickMessage;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void loadConfiguration() {
        // File foler
        String directory = this.file.getString("file-directory", "./plugins/Anti-Bot/players");
        this.fileFolder = new File(directory.replace("/", File.separator));
        this.fileFolder.mkdir();
        
        // Enabled option
        this.enabled = this.file.getBoolean("enabled", true);
        
        // Kick message option
        StringBuilder builder = new StringBuilder();
        for (String string : this.file.getStringList("kick-message")) {
            builder.append(this.translateColors(string)).append("\n");
        }
        this.kickMessage = builder.toString();
        
        // List of blocked commands
        this.blockedCmds = this.file.getStringList("block-cmd-list");
        if (this.blockedCmds == null) {
            this.blockedCmds = new ArrayList<>();
        }
        
        // Reason message when player is trying to send bot-message
        this.blockedCmdsMsg = this.translateColors(this.file.getString("block-cmd-message", "&&cNie mozesz uzywac komend od botów!"));
    }
    
    private String translateColors(String message) {
        Validate.notNull(message, "message can not be null");
        return message.replace("&&", "§");
    }
}
