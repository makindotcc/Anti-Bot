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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

public class OptionsManager {
    private final FileConfiguration file;
    private List<Option> options;
    
    public OptionsManager(FileConfiguration file) {
        Validate.notNull(file, "file can not be null");
        this.file = file;
    }
    
    public void addOption(Option option) {
        Validate.notNull(option, "option can not be null");
        this.options.add(option);
        option.setValue(this.file.getBoolean("options." + option.getID(), false));
        if (option.getValue()) {
            option.reload();
        }
    }
    
    public void addDefaultOptions() {
        this.options = new ArrayList<>();
        this.addOption(new AccountsOption());
        this.addOption(new BlockCmdOption());
    }
    
    public Option findOption(String query) {
        Validate.notNull(query, "query can not be null");
        for (Option option : this.getOptions()) {
            if (option.getID().contains(query.toLowerCase())) {
                return option;
            }
        }
        return null;
    }
    
    public Option getOption(String name) {
        Validate.notNull(name, "name can not be null");
        for (Option option : this.getOptions()) {
            if (option.getID().equals(name.toLowerCase())) {
                return option;
            }
        }
        return null;
    }
    
    public List<Option> getOptions() {
        return this.options;
    }
    
    public void reloadAll() {
        for (Option option : this.getOptions()) {
            if (option.getValue()) {
                option.reload();
            }
        }
    }
}
