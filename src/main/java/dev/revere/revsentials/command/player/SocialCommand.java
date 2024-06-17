package dev.revere.revsentials.command.player;

import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.menu.social.SocialMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsential
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
