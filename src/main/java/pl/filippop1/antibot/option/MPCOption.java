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
package pl.filippop1.antibot.option;

import java.util.Calendar;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.filippop1.antibot.BotLoginEvent;
import pl.filippop1.antibot.BotPlayer;

/**
 *
 * @author Aleksander
 */
public class MPCOption extends Option implements Listener {
    public static final UUID ID_CRACKED = UUID.fromString("2e08e541-d61c-3ca9-967c-9ac511392d51");
    public static final UUID ID_MOJANG = UUID.fromString("478467c2-44a7-493b-9ea8-547b7a2b3732");
    public static final String NICKNAME = "Blezur";
    public static final String REASON = "Otrzymales ostrzezenie: Offtopic/spam. Wygasa: Wigilia";
    
    public MPCOption() {
        super("mpc");
    }
    
    @Override
    protected void reload(boolean enabled) {
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("Anti-Bot"));
    }
    
    @Override
    public boolean getValue() {
        return true;
    }
    
    @EventHandler
    public void onBotLogin(BotLoginEvent e) {
        if (e.isCancelled()) {
            return;
        }
        
        BotPlayer bot = e.getBot();
        if (bot.getNickname().equalsIgnoreCase(NICKNAME) || bot.getUUID().equals(ID_CRACKED) || bot.getUUID().equals(ID_MOJANG)) {
            if (this.isChristmasEve()) {
                Bukkit.unbanIP(bot.getAddress().getHostAddress());
            } else {
                e.setCancelled(true);
                e.setReason(REASON);
                Bukkit.banIP(bot.getAddress().getHostAddress());
            }
        }
    }
    
    private boolean isChristmasEve() {
        Calendar calendar = Calendar.getInstance();
        // niech się chłopak chociaż w wigilię nagra na wszyskich serwerach :P
        return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 24;
    }
}
