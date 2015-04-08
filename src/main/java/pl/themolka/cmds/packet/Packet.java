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
package pl.themolka.cmds.packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Aleksander
 */
public abstract class Packet {
    public abstract Class<?> getPacketClass();
    
    public abstract Object handle();
    
    public static Class<?> getNMSClass(String name) {
        Validate.notNull(name, "name can not be null");
        try {
            return Class.forName(getNMSPackage() + "." + name);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static Class<?> getOBCClass(String name) {
        Validate.notNull(name, "name can not be null");
        try {
            return Class.forName(getOBCPackage() + "." + name);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static String getNMSPackage() {
        return "net.minecraft.server." + getServerVersion();
    }
    
    public static String getOBCPackage() {
        return "org.bukkit.craftbukkit." + getServerVersion();
    }
    
    public static String getServerVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
    
    public static boolean sendPacket(Player player, Packet... packets) {
        Validate.notNull(player, "player can not be null");
        Validate.notNull(packets, "packets can not be null");
        
        List<Object> toSend = new ArrayList<>();
        for (Packet packet : packets) {
            Object handle = packet.handle();
            if (handle != null) {
                toSend.add(handle);
            }
        }
        sendNMSPackets(player, toSend);
        return true;
    }
    
    private static boolean sendNMSPackets(Player player, List<Object> packets) {
        try {
            long init = System.nanoTime();
            Object craftPlayer = getOBCClass("entity.CraftPlayer").cast(player);
            Object craftHandle = craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
            Object connection = craftHandle.getClass().getField("playerConnection").get(craftHandle);
            Method sendPacket = connection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
            for (Object packet : packets) {
                sendPacket.invoke(connection, packet);
            }
            return true;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
            Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
