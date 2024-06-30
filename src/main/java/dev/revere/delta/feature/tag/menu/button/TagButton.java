package dev.revere.delta.feature.tag.menu.button;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.feature.tag.menu.TagsMenu;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:43
 */
@AllArgsConstructor
public class TagButton extends Button {

    private Tag tag;

    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        String selectLore = tagService.hasSelected(profile, tag) ? "&cYou already have this tag selected!" : "&f► Click to " + tag.getColor() + "select &fthis tag!";

        return new ItemBuilder(Material.LIME_WOOL)
                .name(tag.getColor() + tag.getName())
                .lore(Arrays.asList(
                        "&8&m----------------------",
                        CC.translate(" &fPrefix: " + tag.getPrefix()),
                        "",
                        selectLore,
                        "&8&m----------------------"))
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (tagService.hasSelected(profile, tag)) {
            player.sendMessage(CC.translate("&cYou already have this tag selected!"));
            return;
        }

        profile.setTagName(tag.getName());
        profile.saveProfile();

        player.sendMessage(CC.translate("&fYou have selected the tag " + tag.getColor() + tag.getName() + "&f!"));

        new TagsMenu().openMenu(player);
    }
}
