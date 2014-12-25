/*
 * Copyright 2014 Filip.
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

package pl.filippop1.antibot.command.def;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.filippop1.antibot.AntiBotPlugin;
import pl.filippop1.antibot.command.Command;

public class VersionCommand extends Command {
    public VersionCommand() {
        super("version", "Przydatne informacje oraz linki",
                new String[] {"ver", "about", "author", "authors"});
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        String git = AntiBotPlugin.SOURCE_URL;
        sender.sendMessage(ChatColor.GOLD + "AntiBot wersja " + ChatColor.YELLOW + AntiBotPlugin.VERSION
                + ChatColor.GOLD + " by " + ChatColor.YELLOW + AntiBotPlugin.AUTHORS);
        sender.sendMessage(ChatColor.GOLD + "Otwarty kod (GitHub): " + ChatColor.YELLOW + git);
        sender.sendMessage(ChatColor.GOLD + "Bledy oraz propozycje: " + ChatColor.YELLOW + git + "/issues");
    }
}
