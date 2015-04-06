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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aleksander
 */
public class Updater implements Runnable {
    private URL url;
    private final List<String> versions = new ArrayList<>();
    
    public URL getURL() {
        return this.url;
    }
    
    public List<String> getVersions() {
        return this.versions;
    }
    
    public boolean isURLSet() {
        return this.url != null;
    }
    
    public void setURL(URL url) {
        this.url = url;
    }
    
    @Override
    public synchronized void run() {
        try {
            Scanner scanner = new Scanner(this.getURL().openStream());
            while (scanner.hasNextLine()) {
                this.versions.add(scanner.nextLine());
            }
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
