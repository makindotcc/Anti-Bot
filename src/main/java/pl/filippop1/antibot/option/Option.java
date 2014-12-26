/*
 * Copyright 2014 Aleksander.
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

import org.apache.commons.lang.Validate;
import pl.filippop1.antibot.AntiBotPlugin;

public abstract class Option {
    private final String id;
    private boolean value;
    
    public Option(String id) {
        this(id, false);
    }
    
    public Option(String id, boolean value) {
        Validate.notNull(id, "id can not be null");
        this.id = id.toLowerCase();
        this.value = value;
    }
    
    public String getID() {
        return this.id;
    }
    
    public boolean getValue() {
        return this.value;
    }
    
    public void reload() {
        this.reload(this.getValue() && AntiBotPlugin.isPluginEnabled());
    }
    
    protected abstract void reload(boolean enabled);
    
    public void setValue(boolean value) {
        this.value = value;
    }
}
