package net.bunten.enderscape;

import net.bunten.enderscape.registry.EnderscapeLootTables;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;

/** Adds Enderscape loot to the vanilla stronghold library chests. */
@EventBusSubscriber(modid = Enderscape.MOD_ID)
public class LootSupplementEvents {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (EnderscapeConfig.getInstance().supplementVanillaStrongholdLibraryLoot && event.getName().equals(BuiltInLootTables.STRONGHOLD_LIBRARY.location())) {
            event.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(NestedLootTable.lootTableReference(EnderscapeLootTables.STRONGHOLD_LIBRARY_CHEST_SUPPLEMENTS).setWeight(1)).build());
        }
    }
}
