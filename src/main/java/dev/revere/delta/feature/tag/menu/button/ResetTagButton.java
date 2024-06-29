package dev.revere.delta.feature.tag.menu.button;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
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
public class ResetTagButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lReset Tag")
                .lore(Arrays.asList(
                        "&8&m----------------------",
                        " &fSelected Tag: " + (profile.getTag() != null ? profile.getTag().getColor() + profile.getTag().getName() : "&cNone"),
                        "",
                        " &fClick to &cremove &fyour tag.",
                        "&8&m----------------------"))
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        profile.setTagName("");
        profile.saveProfile();

        player.sendMessage(CC.translate("&aYou have successfully removed your tag!"));
    }
}
