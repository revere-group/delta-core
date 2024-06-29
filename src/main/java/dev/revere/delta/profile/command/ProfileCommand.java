package dev.revere.delta.profile.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.profile.menu.ProfileMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/29/2024
 */
public class ProfileCommand extends BaseCommand {
    @Command(name = "profile", aliases = {"menu"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new ProfileMenu().openMenu(player);
    }
}
