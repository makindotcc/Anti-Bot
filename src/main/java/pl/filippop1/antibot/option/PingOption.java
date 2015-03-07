/*
 * Copyright 2015 Filip.
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

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import pl.filippop1.antibot.AntiBotPlugin;
import pl.filippop1.antibot.BotLoginEvent;
import pl.filippop1.antibot.BotPlayer;

public class PingOption extends Option implements Listener {
    public static final long TIMEOUT = 10 * 1000L;
    private final Map<String, Long> pinged = new HashMap<>();
    
    public PingOption() {
        super("ping");
    }
    
    @Override
    public void reload(boolean enabled) {
        AntiBotPlugin plugin = (AntiBotPlugin) Bukkit.getPluginManager().getPlugin("Anti-Bot");
        if (enabled) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else {
            HandlerList.unregisterAll(this);
        }
    }
    
    @EventHandler
    public void onBotLogin(BotLoginEvent e) {
        if (!this.pinged.containsKey(e.getBot().getAddress().getHostAddress())) {
            e.setCancelled(true);
            e.setReason(this.translate(AntiBotPlugin.getConfiguration().getPingMessage(), e.getBot()));
        }
    }
    
    @EventHandler
    public void onServerListPingEvent(ServerListPingEvent e) {
        String address = e.getAddress().getHostAddress();
        if (!this.pinged.containsKey(address)) {
            this.checkListedAddresses();
            this.pinged.put(address, System.currentTimeMillis());
        }
    }
    
    private void checkListedAddresses() {
        for (String address : this.pinged.keySet()) {
            long millis = this.pinged.get(address);
            if (PingOption.TIMEOUT < (System.currentTimeMillis() - millis)) {
                this.pinged.remove(address);
            }
        }
    }
    
    private String translate(String message, BotPlayer bot) {
        Validate.notNull(message, "message can not be null");
        Validate.notNull(bot, "bot can not be null");
        return message
                .replace("$player", bot.getNickname())
                .replace("$uuid", bot.getUUID().toString())
                .replace("$ip", bot.getAddress().getHostAddress());
    }
}