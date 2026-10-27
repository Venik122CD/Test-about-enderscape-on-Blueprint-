package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeBannerPatterns.CRESCENT;
import static net.bunten.enderscape.registry.tag.EnderscapeBannerPatternTags.PATTERN_ITEM_CRESCENT;

public class EnderscapeBannerPatternTagProvider extends TagsProvider<BannerPattern> {

    public EnderscapeBannerPatternTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Registries.BANNER_PATTERN, event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(PATTERN_ITEM_CRESCENT).add(CRESCENT);
    }
}