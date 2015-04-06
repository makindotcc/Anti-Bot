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
package pl.themolka.cmds.command;

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Aleksander
 */
public class CommandOptions {
    private final SortedMap<String, String> options = new TreeMap<>();
    
    public void addOption(String name, String description) {
        Validate.notNull(name, "name can not be null");
        Validate.notNull(description, "description can not be null");
        this.options.put(name, description);
    }
    
    public SortedMap<String, String> getOptions() {
        return this.options;
    }
}
