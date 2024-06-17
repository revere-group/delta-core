package dev.revere.revsentials.command.admin;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import dev.revere.revsentials.util.lang.Locale;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class InstanceCommand extends BaseCommand {
    @Command(name = "instance", permission = "revsentials.admin.instance", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        StringBuilder plugins = new StringBuilder();
        String separator = Revsential.getInstance().getConfig("messages.yml").getString("instance-command.separator-format", ", ");
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (plugins.length() > 0) {
                plugins.append(CC.translate(separator));
            }
            plugins.append(plugin.getName());
        }

        int pluginCount = Bukkit.getServer().getPluginManager().getPlugins().length;

        List<String> operators = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {
            if (offlinePlayer.isOp()) {
                operators.add(offlinePlayer.getName());
            }
        }

        for (String message : Revsential.getInstance().getConfig("messages.yml").getStringList("instance-command.lines")) {
            player.sendMessage(CC.translate(message)
                    .replace("%server-region%", Locale.SERVER_REGION)
                    .replace("%server-name%", Locale.SERVER_NAME)
                    .replace("%version%", Bukkit.getServer().getVersion())
                    .replace("%spigot%", Bukkit.getServer().getName())
                    .replace("%max-players%", String.valueOf(Bukkit.getServer().getMaxPlayers()))
                    .replace("%online-players%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))
                    .replace("%plugins-name%", plugins.toString())
                    .replace("%plugins%", String.valueOf(pluginCount))
                    .replace("%ops%", String.valueOf(operators.size()))
                    .replace("%ops-name%", String.join(CC.translate(separator), operators)));
        }
    }
}
