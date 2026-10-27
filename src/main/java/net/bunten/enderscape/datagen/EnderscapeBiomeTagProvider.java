package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeBiomes.*;
import static net.bunten.enderscape.registry.tag.EnderscapeBiomeTags.*;
import static net.minecraft.tags.BiomeTags.HAS_END_CITY;
import static net.minecraft.tags.BiomeTags.IS_END;
import static net.minecraft.world.level.biome.Biomes.*;

public class EnderscapeBiomeTagProvider extends BiomeTagsProvider {

    public EnderscapeBiomeTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(ENDERSCAPE_BIOMES).add(VEILED_WOODLANDS, MAGNIA_CRAGS, CELESTIAL_GROVE, CORRUPT_BARRENS, VOID_SKIES, VOID_SKY_ISLANDS, VOID_DEPTHS);

        tag(EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS).add(THE_END).addTag(ENDERSCAPE_BIOMES);
        tag(HAS_END_CITY).add(VEILED_WOODLANDS, MAGNIA_CRAGS, CELESTIAL_GROVE);
        tag(HAS_GATEWAYS).addTag(HAS_END_CITY).add(CORRUPT_BARRENS);
        tag(HAS_MIRESTONE_RUINS).add(CORRUPT_BARRENS);
        tag(HAS_MODIFIED_END_AMBIENCE).add(THE_END, END_HIGHLANDS, END_MIDLANDS, END_BARRENS, SMALL_END_ISLANDS);
        tag(INCLUDES_NEW_BARRENS_CONTENT).add(END_HIGHLANDS, END_MIDLANDS);
        tag(IS_END).addTag(ENDERSCAPE_BIOMES);

        tag(DOES_NOT_SPAWN_END_STONE_RUBBLEMITES).add(CORRUPT_BARRENS);
        tag(DOES_NOT_SPAWN_KURODITE_RUBBLEMITES).add(THE_END, END_HIGHLANDS, END_MIDLANDS, END_BARRENS, SMALL_END_ISLANDS, VEILED_WOODLANDS, MAGNIA_CRAGS, CELESTIAL_GROVE);
        tag(DOES_NOT_SPAWN_MIRESTONE_RUBBLEMITES);
        tag(DOES_NOT_SPAWN_VERADITE_RUBBLEMITES).add(CORRUPT_BARRENS);
    }
}