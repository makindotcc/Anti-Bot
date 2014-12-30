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

package pl.filippop1.antibot.option;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.filippop1.antibot.AntiBotPlugin;

public class BlockCmdOption extends Option implements Listener {
    public BlockCmdOption() {
        super("block-cmd");
    }
    
    @Override
    protected void reload(boolean enabled) {
        AntiBotPlugin plugin = (AntiBotPlugin) Bukkit.getPluginManager().getPlugin("Anti-Bot");
        if (enabled) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else {
            HandlerList.unregisterAll(this);
        }
    }
    
    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        if (Bukkit.getOnlinePlayers().size() > 1) {
            for (String command : AntiBotPlugin.getConfiguration().getBlockedCmds()) {
                if (e.getMessage().contains(command)) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(AntiBotPlugin.getConfiguration().getBlockedCmdsMsg());
                }
            }
        }
    }
}
