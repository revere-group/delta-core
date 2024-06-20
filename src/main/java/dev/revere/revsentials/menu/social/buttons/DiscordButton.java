package dev.revere.revsentials.menu.social.buttons;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.service.ConfigService;
import dev.revere.revsentials.util.CC;
import dev.revere.revsentials.util.menu.Button;
import dev.revere.revsentials.util.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 06:59
 */
public class DiscordButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        Revsential plugin = Revsential.getInstance();
        FileConfiguration config = plugin.getServiceManager().getService(ConfigService.class).getConfigByName("menus/socials-menu.yml");
        return new ItemBuilder(Material.matchMaterial(Objects.requireNonNull(config.getString("menu.items.discord.material"))))
                .name(config.getString("menu.items.discord.name"))
                .durability((short) config.getInt("menu.items.discord.data"))
                .lore(config.getStringList("menu.items.discord.lore"))
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.sendMessage(CC.translate("&bDiscord link: https://discord.gg/QsdXsW92sq"));
    }
}
