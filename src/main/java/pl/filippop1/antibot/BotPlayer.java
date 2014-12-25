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
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BotPlayer {
    private File file;
    private final Player player;
    
    public BotPlayer(Player player) {
        Validate.notNull(player, "player can not be null");
        this.player = player;
        this.loadFile();
    }
    
    public boolean firstJoin() {
        return !this.file.exists();
    }
    
    public File getFile() {
        return this.file;
    }
    
    private void loadFile() {
        String path = AntiBotPlugin.getConfiguration().getFileFolder().getAbsolutePath() + File.separator;
        this.file = new File(path + this.player.getUniqueId() + ".bot");
    }
    
    public void register() {
        try {
            file.createNewFile();
        } catch (IOException ex) {
            String message = "Nie udalo sie stworzyc nowego pliku gracza " + this.player.getUniqueId().toString()
                    + " (znanego jako `" + this.player.getName() + "`)";
            Bukkit.getLogger().log(Level.SEVERE, message, ex);
        }
    }
}
