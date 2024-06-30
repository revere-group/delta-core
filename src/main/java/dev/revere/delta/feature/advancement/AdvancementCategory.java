package dev.revere.delta.feature.advancement;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
@Getter
@AllArgsConstructor
public enum AdvancementCategory {

    STORY("Story", "minecraft:story", 20),
    ADVENTURE("Adventure", "minecraft:adventure", 30),
    NETHER("Nether", "minecraft:nether", 25),
    END("End", "minecraft:end", 25),
    HUSBANDRY("Husbandry", "minecraft:husbandry", 15),
    RECIPES("Recipes", "minecraft:recipes", 5);

    private final String displayName;
    private final String namespace;
    private final int reward;

    /**
     * Get the key prefix for advancements in this category.
     *
     * @return the key prefix
     */
    public String getKeyPrefix() {
        return namespace + "/";
    }

    /**
     * Get the namespace without the "minecraft:" prefix.
     *
     * @return the namespace without the prefix
     */
    public String getNamespaceWithoutMinecraft() {
        return namespace.replace("minecraft:", "");
    }
}
