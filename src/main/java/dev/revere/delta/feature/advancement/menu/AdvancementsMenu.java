package dev.revere.delta.feature.advancement.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.advancement.AdvancementCategory;
import dev.revere.delta.feature.advancement.AdvancementService;
import dev.revere.delta.feature.advancement.button.AdvancementProfileButton;
import dev.revere.delta.profile.menu.ProfileMenu;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
public class AdvancementsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(3, new BackButton(new ProfileMenu()));

        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skullItem.setItemMeta(skullMeta);
        }

        buttons.put(5, new AdvancementProfileButton(CC.translate("&b&lYour Profile"), skullItem, Arrays.asList(
                "&8&m----------------------",
                " &fIncomplete: &b" + Delta.getInstance().getServiceManager().getService(AdvancementService.class).getIncompleteAdvancements(player).size(),
                " &fCompleted: &b" + Delta.getInstance().getServiceManager().getService(AdvancementService.class).getCompletedAdvancements(player).size(),
                " &fTotal: &b" + Delta.getInstance().getServiceManager().getService(AdvancementService.class).getAdvancements().size(),
                "",
                "&7Cannot interact with",
                "&8&m----------------------"
        )));

        int index = 19;
        for (AdvancementCategory category : AdvancementCategory.values()) {
            buttons.put(index++, new AdvancementMenuButton("&b&l" + category.getDisplayName(), new ItemStack(Material.BOOK), Arrays.asList(
                    "&8&m----------------------",
                    " &fView your " + category.getDisplayName() + " advancements",
                    " &fand complete them for rewards.",
                    "",
                    " &fIncomplete: &b" + Delta.getInstance().getServiceManager().getService(AdvancementService.class).getIncompleteAdvancements(player, category).size(),
                    " &fCompleted: &b" + Delta.getInstance().getServiceManager().getService(AdvancementService.class).getCompletedAdvancements(player, category).size(),
                    " &fTotal: &b" + Delta.getInstance().getServiceManager().getService(AdvancementService.class).getAdvancements(category).size(),
                    "",
                    "&aClick to view!",
                    "&8&m----------------------"
            ), category));
        }

        addBorder(buttons, (byte) 6, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
