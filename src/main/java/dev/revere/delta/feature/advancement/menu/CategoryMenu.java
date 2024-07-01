package dev.revere.delta.feature.advancement.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.advancement.AdvancementCategory;
import dev.revere.delta.feature.advancement.AdvancementService;
import dev.revere.delta.feature.advancement.button.AdvancementButton;
import dev.revere.delta.feature.advancement.button.AdvancementRedeemButton;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.button.BackButton;
import dev.revere.delta.util.menu.pagination.PaginatedMenu;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
@RequiredArgsConstructor
public class CategoryMenu extends PaginatedMenu {

    private final AdvancementCategory category;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&8" + category.getDisplayName() + " Advancements | #" + getPage();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        addGlassHeader(buttons);

        buttons.put(3, new BackButton(new AdvancementsMenu()));
        buttons.put(5, new AdvancementRedeemButton("&b&lRedeem All", new ItemStack(Material.EMERALD), Arrays.asList(
                "&8&m----------------------",
                " &fRedeem all completed advancements",
                "",
                "&aClick to redeem all",
                "&8&m----------------------"
        ), category));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        AdvancementService advancementService = Delta.getInstance().getServiceManager().getService(AdvancementService.class);

        List<Advancement> completedAdvancements = advancementService.getCompletedAdvancements(player, category);
        List<Advancement> incompleteAdvancements = advancementService.getIncompleteAdvancements(player, category);

        completedAdvancements.sort((a1, a2) -> {
            boolean redeemed1 = advancementService.hasAdvancementRedeemed(player, a1.getKey().getKey());
            boolean redeemed2 = advancementService.hasAdvancementRedeemed(player, a2.getKey().getKey());

            if (redeemed1 && !redeemed2) {
                return 1;
            } else if (!redeemed1 && redeemed2) {
                return -1;
            } else {
                return 0;
            }
        });

        int slot = 0;

        for (Advancement advancement : completedAdvancements) {
            slot = validateSlot(slot);
            AdvancementDisplay display = advancement.getDisplay();
            AdvancementCategory category = advancementService.getAdvancementCategory(advancement.getKey().getKey());

            List<String> lore = new ArrayList<>();
            String title = category == AdvancementCategory.RECIPES ? StringUtils.capitalize(String.join(", ", advancement.getCriteria()).replace("_", " ")) : display != null ? display.getTitle() : "No title available";
            String description = display != null ? display.getDescription() : "No description available";
            String criteria = StringUtils.capitalize(String.join(", ", advancement.getCriteria()).replace("_", " ").replace("minecraft:", ""));

            boolean redeemed = advancementService.hasAdvancementRedeemed(player, advancement.getKey().getKey());
            if (!redeemed) {
                if (category == AdvancementCategory.RECIPES) {
                    lore.add("&8&m----------------------");
                    lore.add(" &fReward: &b" + category.getReward() + " coins");
                    lore.add("");
                    lore.add("&aClick to redeem");
                    lore.add("&8&m----------------------");
                } else {
                    lore.add("&8&m----------------------");
                    lore.add(" &fCriteria: &b" + criteria);
                    lore.add(" &fDescription: &b" + description);
                    lore.add(" &fReward: &b" + category.getReward() + " coins");
                    lore.add("");
                    lore.add("&aClick to redeem");
                    lore.add("&8&m----------------------");
                }

                AdvancementButton button = new AdvancementButton(
                        "&b" + title,
                        new ItemStack(Material.ENCHANTED_BOOK),
                        lore,
                        advancement
                );

                buttons.put(slot++, button);
            }
        }

        for (Advancement advancement : incompleteAdvancements) {
            slot = validateSlot(slot);
            AdvancementDisplay display = advancement.getDisplay();
            AdvancementCategory category = advancementService.getAdvancementCategory(advancement.getKey().getKey());


            List<String> lore = new ArrayList<>();
            String title = category == AdvancementCategory.RECIPES ? StringUtils.capitalize(String.join(", ", advancement.getCriteria()).replace("_", " ")) : display != null ? display.getTitle() : "No title available";
            String description = display != null ? display.getDescription() : "No description available";
            String criteria = StringUtils.capitalize(String.join(", ", advancement.getCriteria()).replace("_", " ").replace("minecraft:", ""));

            if (category == AdvancementCategory.RECIPES) {
                lore.add("&8&m----------------------");
                lore.add("");
                lore.add("&cYou have not completed this advancement yet");
                lore.add("&8&m----------------------");
            } else {
                lore.add("&8&m----------------------");
                lore.add(" &fCriteria: &b" + criteria);
                lore.add(" &fDescription: &b" + description);
                lore.add("");
                lore.add("&cYou have not completed this advancement yet");
                lore.add("&8&m----------------------");
            }

            AdvancementButton button = new AdvancementButton(
                    "&b" + title,
                    new ItemStack(Material.BOOK),
                    lore,
                    advancement
            );

            buttons.put(slot++, button);
        }

        for (Advancement advancement : completedAdvancements) {
            slot = validateSlot(slot);
            AdvancementDisplay display = advancement.getDisplay();
            AdvancementCategory category = advancementService.getAdvancementCategory(advancement.getKey().getKey());

            List<String> lore = new ArrayList<>();
            String title = category == AdvancementCategory.RECIPES ? StringUtils.capitalize(String.join(", ", advancement.getCriteria()).replace("_", " ")) : display != null ? display.getTitle() : "No title available";
            String description = display != null ? display.getDescription() : "No description available";
            String criteria = StringUtils.capitalize(String.join(", ", advancement.getCriteria()).replace("_", " ").replace("minecraft:", ""));

            boolean redeemed = advancementService.hasAdvancementRedeemed(player, advancement.getKey().getKey());
            if (redeemed) {
                if (category == AdvancementCategory.RECIPES) {
                    lore.add("&8&m----------------------");
                    lore.add("");
                    lore.add("&cAlready redeemed");
                    lore.add("&8&m----------------------");
                } else {
                    lore.add("&8&m----------------------");
                    lore.add(" &fCriteria: &b" + criteria);
                    lore.add(" &fDescription: &b" + description);
                    lore.add("");
                    lore.add("&cAlready redeemed");
                    lore.add("&8&m----------------------");
                }

                AdvancementButton button = new AdvancementButton(
                        "&b" + title,
                        new ItemStack(Material.WRITTEN_BOOK),
                        lore,
                        advancement
                );

                buttons.put(slot++, button);
            }
        }

        addGlassToAvoidedSlots(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
