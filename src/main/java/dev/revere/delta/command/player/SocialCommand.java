package dev.revere.delta.command.player;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.social.SocialMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class SocialCommand extends BaseCommand {
    @Command(name = "social", aliases = {"socials"}, description = "View our social media links")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new SocialMenu().openMenu(player);
    }
}
