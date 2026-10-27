package net.bunten.enderscape.datagen;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class EnderscapeLootProvider extends LootTableProvider {

    protected EnderscapeLootProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(EnderscapeBlockLootSubProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(EnderscapeChestLootSubProvider::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(EnderscapeVaultLootSubProvider::new, LootContextParamSets.VAULT),
                new LootTableProvider.SubProviderEntry(EnderscapeEntityLootSubProvider::new, LootContextParamSets.ENTITY)
        ), event.getLookupProvider());
    }
}
