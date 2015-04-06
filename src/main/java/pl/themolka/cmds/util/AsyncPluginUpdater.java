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
package pl.themolka.cmds.util;

import org.bukkit.plugin.Plugin;

/**
 *
 * @author Aleksander
 */
public class AsyncPluginUpdater extends PluginUpdater {
    public AsyncPluginUpdater(Plugin plugin) {
        super(plugin);
    }
    
    public Thread asyncCheckForUpdates() {
        return this.asyncCheckForUpdates(null);
    }
    
    public Thread asyncCheckForUpdates(UpdateFuture future) {
        if (future == null) {
            future = new UpdateFuture() {
                @Override
                public void handle(PluginUpdater updater) {}
            };
        }
        
        Thread thread = new Thread(new UpdaterThread(future, this));
        thread.start();
        return thread;
    }
}
