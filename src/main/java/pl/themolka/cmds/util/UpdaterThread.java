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

import org.apache.commons.lang.Validate;

/**
 *
 * @author Aleksander
 */
public class UpdaterThread implements Runnable {
    private final UpdateFuture future;
    private final PluginUpdater updater;
    
    public UpdaterThread(UpdateFuture future, PluginUpdater updater) {
        Validate.notNull(future, "future can not be null");
        Validate.notNull(updater, "updater can not be null");
        this.future = future;
        this.updater = updater;
    }
    
    public UpdateFuture getFuture() {
        return this.future;
    }
    
    public PluginUpdater getUpdater() {
        return this.updater;
    }
    
    @Override
    public synchronized void run() {
        this.updater.run();
        this.future.handle(this.updater);
    }
}
