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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.themolka.cmds.menu.Menu;

/**
 *
 * @author Aleksander
 */
public class Listeners implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for (Menu menu : Menu.getRegistered()) {
            if (menu.getName().equals(e.getInventory().getTitle())) {
                e.setCancelled(true);
                menu.onClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getClick());
                return;
            }
        }
    }
}
