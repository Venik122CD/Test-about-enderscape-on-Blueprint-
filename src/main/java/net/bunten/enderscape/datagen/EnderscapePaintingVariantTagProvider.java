package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapePaintingVariants.GRAPE_STATIC;

public class EnderscapePaintingVariantTagProvider extends TagsProvider<PaintingVariant> {

    public EnderscapePaintingVariantTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Registries.PAINTING_VARIANT, event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(PaintingVariantTags.PLACEABLE).add(GRAPE_STATIC);
    }
}
