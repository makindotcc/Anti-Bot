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
package pl.filippop1.antibot;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;

public class LatestLog {
    private final List<LatestBot> latest;
    
    public LatestLog() {
        this.latest = new ArrayList<>();
    }
    
    public void add(LatestBot bot) {
        this.latest.add(bot);
        if (this.latest.size() > 20) {
            this.latest.remove(0);
        }
    }
    
    public List<LatestBot> get() {
        return this.latest;
    }
    
    public static class LatestBot {
        private final String ip;
        private final String uuid;
        private final String nickname;
        
        public LatestBot(String ip, String uuid, String nickname) {
            Validate.notNull(ip, "ip can not be null");
            Validate.notNull(uuid, "uuid can not be null");
            Validate.notNull(nickname, "nickname can not be null");
            this.ip = ip;
            this.uuid = uuid;
            this.nickname = nickname;
        }
        
        public String getIP() {
            return this.ip;
        }
        
        public String getUUID() {
            return this.uuid;
        }
        
        public String getNickname() {
            return this.nickname;
        }
    }
}
