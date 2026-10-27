package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapePoi;
import net.bunten.enderscape.registry.tag.EnderscapePoiTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class EnderscapePoiTagProvider extends TagsProvider<PoiType> {

    public EnderscapePoiTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Registries.POINT_OF_INTEREST_TYPE, event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(EnderscapePoiTags.DRIFTER_HOME).add(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(EnderscapePoi.DRIFTER_HOME.get()).orElseThrow());
        tag(EnderscapePoiTags.RUSTLE_SLEEPING_SPOT).add(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey(EnderscapePoi.RUSTLE_SLEEPING_SPOT.get()).orElseThrow());
    }
}