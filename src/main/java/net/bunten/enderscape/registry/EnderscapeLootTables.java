package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeLootTables {

    public static final List<ResourceKey<LootTable>> LOOT_TABLES = new ArrayList<>();

    public static final ResourceKey<LootTable> END_CITY_SPAWNER_BASIC = register("end_city/spawner/basic");
    public static final ResourceKey<LootTable> END_CITY_SPAWNER_KEY = register("end_city/spawner/key");

    public static final ResourceKey<LootTable> END_CITY_VAULT = register("end_city/vault");
    public static final ResourceKey<LootTable> END_CITY_ELYTRA_VAULT = register("end_city/elytra_vault");

    public static final ResourceKey<LootTable> END_CITY_CHEST = register("end_city/chest");
    public static final ResourceKey<LootTable> END_CITY_TREASURE_SUPPLEMENTS = register("supplements/end_city_treasure");
    public static final ResourceKey<LootTable> STRONGHOLD_LIBRARY_CHEST_SUPPLEMENTS = register("supplements/stronghold_library");
    public static final ResourceKey<LootTable> END_CITY_TREASURE_POST_SUPPLEMENTS = register("post_supplements/end_city_treasure");

    public static final ResourceKey<LootTable> MIRESTONE_RUINS_CHEST = register("mirestone_ruins/chest");

    private static ResourceKey<LootTable> register(String name) {
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, Enderscape.id(name));
        LOOT_TABLES.add(key);
        return key;
    }
}