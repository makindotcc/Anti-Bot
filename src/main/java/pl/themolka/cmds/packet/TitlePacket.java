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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Aleksander
 */
public final class TitlePacket extends Packet {
    private String title;
    private TitleType type;
    
    public TitlePacket(String title, TitleType type) {
        this.setTitle(title);
        this.setType(type);
    }
    
    @Override
    public Class<?> getPacketClass() {
        return Packet.getNMSClass("PacketPlayOutTitle");
    }
    
    @Override
    public Object handle() {
        try {
            String json = "{\"text\": \"" + this.getTitle() + "\"}";
            Class<?> titleEnum = Packet.getNMSClass("EnumTitleAction");
            Class<?> jsonParser = Packet.getNMSClass("ChatSerializer");
            
            Object[] values = titleEnum.getEnumConstants();
            Object value = null;
            switch (this.getType()) {
                case SUBTITLE:
                    value = values[1];
                    break;
                case TITLE:
                    value = values[0];
                    break;
            }
            
            Object component = jsonParser.getDeclaredMethod("a", new Class[] {String.class}).invoke(null, new Object[] {json});
            Constructor<? extends Object> constructor = this.getPacketClass().getConstructor(new Class[] {titleEnum, Packet.getNMSClass("IChatBaseComponent")});
            return constructor.newInstance(titleEnum.cast(value), component);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            Logger.getLogger(TitlePacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public TitleType getType() {
        return this.type;
    }
    
    public void setTitle(String title) {
        Validate.notNull(title, "title can not be null");
        this.title = title;
    }
    
    public void setType(TitleType type) {
        Validate.notNull(type, "type can not be null");
        this.type = type;
    }
    
    public static enum TitleType {
        SUBTITLE, TITLE;
    }
}
