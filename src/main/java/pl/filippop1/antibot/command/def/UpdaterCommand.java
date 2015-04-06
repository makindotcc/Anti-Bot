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
package pl.filippop1.antibot.command.def;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.filippop1.antibot.AntiBotPlugin;
import pl.filippop1.antibot.command.Command;
import pl.filippop1.antibot.command.CommandException;
import pl.filippop1.antibot.command.PermissionException;
import pl.themolka.cmds.util.PluginUpdater;

/**
 *
 * @author Aleksander
 */
public class UpdaterCommand extends Command {
    public UpdaterCommand() {
        super("updater", "Sprawdz dostepne aktualizacje", "[check]");
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        if (args.length > 1 && args[1].equalsIgnoreCase("check")) {
            this.check(sender);
        } else {
            PluginUpdater updater = AntiBotPlugin.getUpdater();
            if (updater.getVersions().isEmpty()) {
                sender.sendMessage(ChatColor.RED + "Nie udalo sie pobrac aktualizacji. Host nie odpowiada?");
                sender.sendMessage(ChatColor.GREEN + "[Porada] Uzyj /anti-bot updater check, aby sprawdzic aktualizacje.");
            } else if (updater.isAvailable()) {
                sender.sendMessage(ChatColor.RED + "Znaleziono aktualizacje! Najnowsza wersja: " +
                        updater.getVersions().get(0) + ", zainstalowana: " + updater.getPluginVersion() + ".");
            } else {
                sender.sendMessage(ChatColor.GREEN + "Plugin jest aktualny z najnowsza wersja (" + updater.getPluginVersion() + ").");
            }
            sender.sendMessage(ChatColor.GREEN + "[Porada] Sprawdz aktualizacje uzywajac /anti-bot updater check");
        }
    }
    
    private void check(CommandSender sender) throws CommandException {
        if (!sender.hasPermission("antibot.updater.check")) {
            throw new PermissionException("antibot.updater.check");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Pobieranie najnowszych informacji o wersjach (moze zajac kilka chwil)...");
            AntiBotPlugin.checkForUpdates(sender);
        }
    }
}
