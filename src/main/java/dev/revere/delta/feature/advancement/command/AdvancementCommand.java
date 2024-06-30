package dev.revere.delta.feature.advancement.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.advancement.menu.AdvancementsMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
public class AdvancementCommand extends BaseCommand {
    @Command(name = "advancements", aliases = {"quests", "quest", "achievements", "achievement"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new AdvancementsMenu().openMenu(player);
    }
}
