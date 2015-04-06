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
package pl.themolka.cmds.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Aleksander
 */
public abstract class Menu implements IMenuListener {
    private static final List<Menu> menus = new ArrayList<>();
    
    private Inventory bukkit;
    private final UUID id;
    private final String name;
    private final int slots;
    
    public Menu(String name) {
        this(name, 1);
    }
    
    public Menu(String name, int rows) {
        Validate.notNull(name, "name can not be null");
        Validate.isTrue(rows > 0, "rows can not be zero or be negative");
        this.id = UUID.randomUUID();
        this.name = name;
        this.slots = rows * 9;
        this.bukkit = Bukkit.createInventory(null, this.slots, this.name);
    }
    
    public Inventory getBukkit() {
        return this.bukkit;
    }
    
    public UUID getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getRows() {
        return this.getSlots() / 9;
    }
    
    public int getSlots() {
        return this.slots;
    }
    
    public void register() {
        Menu.register(this);
    }
    
    public static Menu getMenu(UUID id) {
        for (Menu menu : getRegistered()) {
            if (menu.getID().equals(id)) {
                return menu;
            }
        }
        return null;
    }
    
    public static List<Menu> getRegistered() {
        return menus;
    }
    
    public static void open(Menu menu, Player player) {
        Validate.notNull(menu, "menu can not be null");
        Validate.notNull(player, "player can not be null");
        menu.onCreate(player);
        player.openInventory(menu.getBukkit());
    }
    
    public static void register(Menu menu) {
        Validate.notNull(menu, "menu can not be null");
        menus.add(menu);
    }
    
    public static boolean unregister(Menu menu) {
        Validate.notNull(menu, "menu can not be null");
        return menus.remove(menu);
    }
    
    public static void unregisterAll() {
        menus.clear();
    }
}
