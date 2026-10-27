package net.bunten.enderscape.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;
import static net.minecraft.world.item.Items.*;

public class EnderscapeRecipeProvider extends RecipeProvider {

    public EnderscapeRecipeProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider());
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        final ImmutableList<ItemLike> nebuliteSmeltables = ImmutableList.of(EnderscapeBlocks.NEBULITE_ORE.get(), EnderscapeBlocks.MIRESTONE_NEBULITE_ORE.get());
        final ImmutableList<ItemLike> shadolineSmeltables = ImmutableList.of(EnderscapeBlocks.SHADOLINE_ORE.get(), EnderscapeBlocks.MIRESTONE_SHADOLINE_ORE.get(), EnderscapeItems.RAW_SHADOLINE.get());
        
        EnderscapeBlockFamilies.getAllFamilies().forEach((family) -> generateRecipes(output, family, FeatureFlagSet.of(FeatureFlags.VANILLA)));

        VanillaRecipeProvider.TrimTemplate stasisTrim = new VanillaRecipeProvider.TrimTemplate(EnderscapeItems.STASIS_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ResourceLocation.fromNamespaceAndPath(Enderscape.MOD_ID, getItemName(EnderscapeItems.STASIS_ARMOR_TRIM_SMITHING_TEMPLATE.get()) + "_smithing_trim"));
        trimSmithing(output, stasisTrim.template(), stasisTrim.id());

        shaped(RecipeCategory.MISC, EnderscapeItems.MUSIC_DISC_BLISS.get())
                .define('D', EnderscapeItems.MUSIC_DISC_DECAY.get())
                .define('N', EnderscapeItems.NEBULITE_SHARDS.get())
                .pattern(" N ")
                .pattern("NDN")
                .pattern(" N ")
                .unlockedBy("has_music_disc_decay", has(EnderscapeItems.MUSIC_DISC_DECAY.get()))
                .save(output);

        shapeless(RecipeCategory.FOOD, EnderscapeItems.DRIFT_JELLY_BOTTLE.get(), 4)
                .requires(EnderscapeBlocks.DRIFT_JELLY_BLOCK.get(), 1)
                .requires(GLASS_BOTTLE, 4)
                .unlockedBy("has_drift_jelly_block", has(EnderscapeBlocks.DRIFT_JELLY_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.MISC, EnderscapeBlocks.DRIFT_JELLY_BLOCK.get())
                .define('#', EnderscapeItems.DRIFT_JELLY_BOTTLE.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_drift_jelly_bottle", has(EnderscapeItems.DRIFT_JELLY_BOTTLE.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, EnderscapeItems.DRIFT_LEGGINGS.get())
                .define('N', EnderscapeItems.NEBULITE.get())
                .define('D', EnderscapeItems.DRIFT_JELLY_BOTTLE.get())
                .pattern("NNN")
                .pattern("D D")
                .pattern("D D")
                .unlockedBy("has_drift_jelly_bottle", has(EnderscapeItems.DRIFT_JELLY_BOTTLE.get()))
                .save(output);

        rubbleShield(output, END_STONE, EnderscapeItems.END_STONE_RUBBLE_SHIELD.get());
        rubbleShield(output, EnderscapeBlocks.VERADITE.get(), EnderscapeItems.VERADITE_RUBBLE_SHIELD.get());
        rubbleShield(output, EnderscapeBlocks.MIRESTONE.get(), EnderscapeItems.MIRESTONE_RUBBLE_SHIELD.get());
        rubbleShield(output, EnderscapeBlocks.KURODITE.get(), EnderscapeItems.KURODITE_RUBBLE_SHIELD.get());

        shaped(RecipeCategory.TOOLS, EnderscapeItems.MAGNIA_ATTRACTOR.get())
                .define('A', EnderscapeBlocks.ALLURING_MAGNIA_SPROUT.get())
                .define('R', EnderscapeBlocks.REPULSIVE_MAGNIA_SPROUT.get())
                .define('S', EnderscapeItems.SHADOLINE_INGOT.get())
                .pattern("A R")
                .pattern("SSS")
                .pattern(" S ")
                .unlockedBy("has_magnia_block", has(EnderscapeItemTags.MAGNIA_SPROUTS))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.END_STONE_SLAB.get(), END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.END_STONE_STAIRS.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.END_STONE_WALL.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_END_STONE.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_END_STONE_WALL.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_END_STONE_SLAB.get(), END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_END_STONE_STAIRS.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_END_STONE.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_END_STONE_SLAB.get(), EnderscapeBlocks.POLISHED_END_STONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_END_STONE_STAIRS.get(), EnderscapeBlocks.POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICKS, EnderscapeBlocks.POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_END_STONE_WALL.get(), EnderscapeBlocks.POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_SLAB, EnderscapeBlocks.POLISHED_END_STONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_STAIRS, EnderscapeBlocks.POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, END_STONE_BRICK_WALL, EnderscapeBlocks.POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_END_STONE.get(), EnderscapeBlocks.POLISHED_END_STONE.get());

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_SLAB.get(), EnderscapeBlocks.MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_STAIRS.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.MIRESTONE_WALL.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_MIRESTONE.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_MIRESTONE_WALL.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_MIRESTONE_SLAB.get(), EnderscapeBlocks.MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_MIRESTONE_STAIRS.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_MIRESTONE.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICKS.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICK_SLAB.get(), EnderscapeBlocks.MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICK_STAIRS.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.MIRESTONE_BRICK_WALL.get(), EnderscapeBlocks.MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_MIRESTONE_SLAB.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_MIRESTONE_STAIRS.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICKS.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_MIRESTONE_WALL.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICK_SLAB.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICK_STAIRS.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.MIRESTONE_BRICK_WALL.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_MIRESTONE.get(), EnderscapeBlocks.POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICK_SLAB.get(), EnderscapeBlocks.MIRESTONE_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MIRESTONE_BRICK_STAIRS.get(), EnderscapeBlocks.MIRESTONE_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.MIRESTONE_BRICK_WALL.get(), EnderscapeBlocks.MIRESTONE_BRICKS.get());

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_SLAB.get(), EnderscapeBlocks.VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_STAIRS.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.VERADITE_WALL.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_VERADITE.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_VERADITE_WALL.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_VERADITE_SLAB.get(), EnderscapeBlocks.VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_VERADITE_STAIRS.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_VERADITE.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICKS.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICK_SLAB.get(), EnderscapeBlocks.VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICK_STAIRS.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.VERADITE_BRICK_WALL.get(), EnderscapeBlocks.VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_VERADITE_SLAB.get(), EnderscapeBlocks.POLISHED_VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_VERADITE_STAIRS.get(), EnderscapeBlocks.POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICKS.get(), EnderscapeBlocks.POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_VERADITE_WALL.get(), EnderscapeBlocks.POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICK_SLAB.get(), EnderscapeBlocks.POLISHED_VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICK_STAIRS.get(), EnderscapeBlocks.POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.VERADITE_BRICK_WALL.get(), EnderscapeBlocks.POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_VERADITE.get(), EnderscapeBlocks.POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICK_SLAB.get(), EnderscapeBlocks.VERADITE_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VERADITE_BRICK_STAIRS.get(), EnderscapeBlocks.VERADITE_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.VERADITE_BRICK_WALL.get(), EnderscapeBlocks.VERADITE_BRICKS.get());

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_SLAB.get(), EnderscapeBlocks.KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_STAIRS.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.KURODITE_WALL.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_KURODITE.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_KURODITE_WALL.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_KURODITE_SLAB.get(), EnderscapeBlocks.KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_KURODITE_STAIRS.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_KURODITE.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICKS.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICK_SLAB.get(), EnderscapeBlocks.KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICK_STAIRS.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.KURODITE_BRICK_WALL.get(), EnderscapeBlocks.KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_KURODITE_SLAB.get(), EnderscapeBlocks.POLISHED_KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.POLISHED_KURODITE_STAIRS.get(), EnderscapeBlocks.POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICKS.get(), EnderscapeBlocks.POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.POLISHED_KURODITE_WALL.get(), EnderscapeBlocks.POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICK_SLAB.get(), EnderscapeBlocks.POLISHED_KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICK_STAIRS.get(), EnderscapeBlocks.POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.KURODITE_BRICK_WALL.get(), EnderscapeBlocks.POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_KURODITE.get(), EnderscapeBlocks.POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICK_SLAB.get(), EnderscapeBlocks.KURODITE_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.KURODITE_BRICK_STAIRS.get(), EnderscapeBlocks.KURODITE_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.KURODITE_BRICK_WALL.get(), EnderscapeBlocks.KURODITE_BRICKS.get());

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.ETCHED_ALLURING_MAGNIA.get(), 4)
                .define('#', EnderscapeBlocks.ALLURING_MAGNIA.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_alluring_magnia", has(EnderscapeBlocks.ALLURING_MAGNIA.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA.get(), 4)
                .define('#', EnderscapeBlocks.REPULSIVE_MAGNIA.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_repulsive_magnia", has(EnderscapeBlocks.REPULSIVE_MAGNIA.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.ETCHED_ALLURING_MAGNIA.get(), EnderscapeBlocks.ALLURING_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.ETCHED_REPULSIVE_MAGNIA.get(), EnderscapeBlocks.REPULSIVE_MAGNIA.get());

        oreSmelting(output, shadolineSmeltables, RecipeCategory.MISC, EnderscapeItems.SHADOLINE_INGOT.get(), 0.7F, 200, "shadoline_ingot");
        oreBlasting(output, shadolineSmeltables, RecipeCategory.MISC, EnderscapeItems.SHADOLINE_INGOT.get(), 0.7F, 100, "shadoline_ingot");

        shapeless(RecipeCategory.MISC, EnderscapeItems.RAW_SHADOLINE.get(), 9)
                .requires(EnderscapeBlocks.RAW_SHADOLINE_BLOCK.get())
                .group("raw_shadoline")
                .unlockedBy("has_raw_shadoline_block", has(EnderscapeBlocks.RAW_SHADOLINE_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.RAW_SHADOLINE_BLOCK.get())
                .define('#', EnderscapeItems.RAW_SHADOLINE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("raw_shadoline_block")
                .unlockedBy("has_raw_shadoline", has(EnderscapeItems.RAW_SHADOLINE.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, EnderscapeItems.SHADOLINE_INGOT.get(), 9)
                .requires(EnderscapeBlocks.SHADOLINE_BLOCK.get())
                .group("shadoline_ingot")
                .unlockedBy("has_shadoline_block", has(EnderscapeBlocks.SHADOLINE_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.SHADOLINE_BLOCK.get())
                .define('#', EnderscapeItems.SHADOLINE_INGOT.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("shadoline_block")
                .unlockedBy("has_shadoline_ingot", has(EnderscapeItems.SHADOLINE_INGOT.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.SHADOLINE_BLOCK_SLAB.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.SHADOLINE_BLOCK_STAIRS.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.SHADOLINE_BLOCK_WALL.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_SHADOLINE.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CUT_SHADOLINE.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CUT_SHADOLINE_SLAB.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get(), 8);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CUT_SHADOLINE_STAIRS.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.CUT_SHADOLINE_WALL.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CUT_SHADOLINE_SLAB.get(), EnderscapeBlocks.CUT_SHADOLINE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CUT_SHADOLINE_STAIRS.get(), EnderscapeBlocks.CUT_SHADOLINE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.CUT_SHADOLINE_WALL.get(), EnderscapeBlocks.CUT_SHADOLINE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.SHADOLINE_PILLAR.get(), EnderscapeBlocks.SHADOLINE_BLOCK.get(), 4);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.SHADOLINE_PILLAR.get())
                .define('#', EnderscapeBlocks.SHADOLINE_BLOCK.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_shadoline_block", has(EnderscapeBlocks.SHADOLINE_BLOCK.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, EnderscapeItems.NEBULITE.get())
                .requires(EnderscapeItems.NEBULITE_SHARDS.get(), 4)
                .unlockedBy("has_nebulite_shards", has(EnderscapeItems.NEBULITE_SHARDS.get()))
                .save(output, "enderscape:nebulite_from_shards");

        oreSmelting(output, nebuliteSmeltables, RecipeCategory.MISC, EnderscapeItems.NEBULITE.get(), 1.0F, 200, "nebulite");
        oreBlasting(output, nebuliteSmeltables, RecipeCategory.MISC, EnderscapeItems.NEBULITE.get(), 1.0F, 100, "nebulite");

        shapeless(RecipeCategory.MISC, EnderscapeItems.NEBULITE.get(), 9)
                .requires(EnderscapeBlocks.NEBULITE_BLOCK.get())
                .group("nebulite")
                .unlockedBy("has_nebulite_block", has(EnderscapeBlocks.NEBULITE_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.NEBULITE_BLOCK.get())
                .define('#', EnderscapeItems.NEBULITE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("nebulite_block")
                .unlockedBy("has_nebulite", has(EnderscapeItems.NEBULITE.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_PURPUR.get(), PURPUR_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.PURPUR_WALL.get(), PURPUR_BLOCK);

        shapeless(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.DUSK_PURPUR_BLOCK.get(), 4)
                .requires(POPPED_CHORUS_FRUIT, 2)
                .requires(EnderscapeItems.SHADOLINE_INGOT.get(), 2)
                .unlockedBy("has_popped_chorus_fruit", has(POPPED_CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.DUSK_PURPUR_PILLAR.get())
                .define('#', EnderscapeBlocks.DUSK_PURPUR_BLOCK.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_dusk_purpur_block", has(EnderscapeBlocks.DUSK_PURPUR_BLOCK.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.DUSK_PURPUR_SLAB.get(), EnderscapeBlocks.DUSK_PURPUR_BLOCK.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.DUSK_PURPUR_STAIRS.get(), EnderscapeBlocks.DUSK_PURPUR_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.DUSK_PURPUR_WALL.get(), EnderscapeBlocks.DUSK_PURPUR_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CHISELED_DUSK_PURPUR.get(), EnderscapeBlocks.DUSK_PURPUR_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.DUSK_PURPUR_PILLAR.get(), EnderscapeBlocks.DUSK_PURPUR_BLOCK.get());

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.PURPUR_TILES.get(), 4)
                .define('P', PURPUR_BLOCK)
                .define('D', EnderscapeBlocks.DUSK_PURPUR_BLOCK.get())
                .pattern("DP")
                .pattern("PD")
                .unlockedBy("has_purpur_block", has(PURPUR_BLOCK))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.PURPUR_TILE_SLAB.get(), EnderscapeBlocks.PURPUR_TILES.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.PURPUR_TILE_STAIRS.get(), EnderscapeBlocks.PURPUR_TILES.get());

        shaped(RecipeCategory.FOOD, EnderscapeItems.CHORUS_CAKE_ROLL_ITEM.get())
                .define('C', CHORUS_FRUIT)
                .define('S', SUGAR)
                .define('D', EnderscapeItems.DRIFT_JELLY_BOTTLE.get())
                .pattern("CCC")
                .pattern("SDS")
                .unlockedBy("has_chorus_fruit", has(CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.DECORATIONS, EnderscapeBlocks.END_LAMP.get())
                .define('B', BLAZE_ROD)
                .define('C', POPPED_CHORUS_FRUIT)
                .pattern(" C ")
                .pattern("CBC")
                .pattern(" C ")
                .unlockedBy("has_popped_chorus_fruit", has(POPPED_CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VEILED_LEAF_PILE.get(), 6)
                .define('#', EnderscapeBlocks.VEILED_LEAVES.get())
                .pattern("###")
                .unlockedBy("has_veiled_leaves", has(EnderscapeBlocks.VEILED_LEAVES.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, WHITE_DYE)
                .group("white_dye")
                .requires(EnderscapeBlocks.WISP_FLOWER.get(), 1)
                .unlockedBy("has_wisp_flower", has(EnderscapeBlocks.WISP_FLOWER.get()))
                .save(output, "enderscape:white_dye_from_wisp_flower");

        shapeless(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.VEILED_PLANKS.get(), 4)
                .requires(EnderscapeItemTags.VEILED_LOGS)
                .unlockedBy("has_veiled_logs", has(EnderscapeItemTags.VEILED_LOGS))
                .save(output);

        hangingSign(output, EnderscapeItems.VEILED_HANGING_SIGN_ITEM.get(), EnderscapeBlocks.STRIPPED_VEILED_LOG.get());

        shapeless(RecipeCategory.MISC, YELLOW_DYE)
                .group("yellow_dye")
                .requires(EnderscapeBlocks.CELESTIAL_GROWTH.get(), 1)
                .unlockedBy("has_celestial_growth", has(EnderscapeBlocks.CELESTIAL_GROWTH.get()))
                .save(output, "enderscape:yellow_dye_from_celestial_growth");

        shapeless(RecipeCategory.MISC, CYAN_DYE)
                .group("cyan_dye")
                .requires(EnderscapeBlocks.BULB_FLOWER.get(), 1)
                .unlockedBy("has_bulb_flower", has(EnderscapeBlocks.BULB_FLOWER.get()))
                .save(output, "enderscape:cyan_dye_from_bulb_flower");

        shaped(RecipeCategory.DECORATIONS, EnderscapeBlocks.BULB_LANTERN.get())
                .define('#', IRON_NUGGET)
                .define('@', EnderscapeBlocks.BULB_FLOWER.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("###")
                .unlockedBy("has_bulb_flower", has(EnderscapeBlocks.BULB_FLOWER.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CELESTIAL_BRICKS.get(), 2)
                .define('#', EnderscapeBlocks.CELESTIAL_CAP.get())
                .define('@', EnderscapeBlocks.CELESTIAL_GROWTH.get())
                .pattern("@#")
                .pattern("#@")
                .unlockedBy("has_celestial_cap", has(EnderscapeBlocks.CELESTIAL_CAP.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CELESTIAL_BRICK_SLAB.get(), EnderscapeBlocks.CELESTIAL_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CELESTIAL_BRICK_STAIRS.get(), EnderscapeBlocks.CELESTIAL_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.CELESTIAL_BRICK_WALL.get(), EnderscapeBlocks.CELESTIAL_BRICKS.get());

        shapeless(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.CELESTIAL_PLANKS.get(), 4)
                .requires(EnderscapeItemTags.CELESTIAL_STEMS)
                .unlockedBy("has_celestial_stems", has(EnderscapeItemTags.CELESTIAL_STEMS))
                .save(output);

        hangingSign(output, EnderscapeItems.CELESTIAL_HANGING_SIGN_ITEM.get(), EnderscapeBlocks.STRIPPED_CELESTIAL_STEM.get());

        shapeless(RecipeCategory.MISC, PURPLE_DYE)
                .group("purple_dye")
                .requires(EnderscapeBlocks.CORRUPT_GROWTH.get(), 1)
                .unlockedBy("has_corrupt_growth", has(EnderscapeBlocks.CORRUPT_GROWTH.get()))
                .save(output, "enderscape:purple_dye_from_corrupt_growth");

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.BLINKLAMP.get())
                .define('#', POPPED_CHORUS_FRUIT)
                .define('@', EnderscapeItems.BLINKLIGHT.get())
                .pattern("#@#")
                .pattern("@@@")
                .pattern("#@#")
                .unlockedBy("has_blinklight", has(EnderscapeItems.BLINKLIGHT.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MURUBLIGHT_BRICKS.get(), 2)
                .define('#', EnderscapeBlocks.MURUBLIGHT_CAP.get())
                .define('@', EnderscapeBlocks.CORRUPT_GROWTH.get())
                .pattern("@#")
                .pattern("#@")
                .unlockedBy("has_murublight_cap", has(EnderscapeBlocks.MURUBLIGHT_CAP.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MURUBLIGHT_BRICK_SLAB.get(), EnderscapeBlocks.MURUBLIGHT_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MURUBLIGHT_BRICK_STAIRS.get(), EnderscapeBlocks.MURUBLIGHT_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, EnderscapeBlocks.MURUBLIGHT_BRICK_WALL.get(), EnderscapeBlocks.MURUBLIGHT_BRICKS.get());

        shapeless(RecipeCategory.BUILDING_BLOCKS, EnderscapeBlocks.MURUBLIGHT_PLANKS.get(), 4)
                .requires(EnderscapeItemTags.MURUBLIGHT_STEMS)
                .unlockedBy("has_murublight_stems", has(EnderscapeItemTags.MURUBLIGHT_STEMS))
                .save(output);

        hangingSign(output, EnderscapeItems.MURUBLIGHT_HANGING_SIGN_ITEM.get(), EnderscapeBlocks.STRIPPED_MURUBLIGHT_STEM.get());
    }

    @Override
    protected CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider registries) {
        final Set<ResourceLocation> set = Sets.newHashSet();
        final List<CompletableFuture<?>> list = new ArrayList<>();
        this.buildRecipes(
                new RecipeOutput() {
                    @Override
                    public void accept(ResourceLocation location, Recipe<?> recipe, AdvancementHolder advancement, net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                        location = ResourceLocation.fromNamespaceAndPath(Enderscape.MOD_ID, location.getPath());
                        
                        if (!set.add(location)) {
                            throw new IllegalStateException("Duplicate recipe " + location);
                        } else {
                            list.add(DataProvider.saveStable(output, registries, Recipe.CONDITIONAL_CODEC, Optional.of(new net.neoforged.neoforge.common.conditions.WithConditions<>(recipe, conditions)), EnderscapeRecipeProvider.this.recipePathProvider.json(location)));
                            if (advancement != null) {
                                var modifiedAdvancement = advancement.value();
                                var builder = advancement();
                                
                                for (var entry : modifiedAdvancement.criteria().entrySet()) {
                                    if (!entry.getKey().equals("has_the_recipe")) {
                                        builder.addCriterion(entry.getKey(), entry.getValue());
                                    } else {
                                        builder.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location));
                                    }
                                }
                                builder.requirements(modifiedAdvancement.requirements());
                                var rewards = new AdvancementRewards.Builder();
                                rewards.addExperience(modifiedAdvancement.rewards().experience());
                                for (var lootTable : modifiedAdvancement.rewards().loot()) {
                                    rewards.addLootTable(lootTable);
                                }
                                rewards.addRecipe(location);
                                builder.rewards(rewards.build());
                                
                                modifiedAdvancement = builder.build(advancement.id()).value();
                                list.add(
                                        DataProvider.saveStable(
                                                output,
                                                registries,
                                                Advancement.CONDITIONAL_CODEC,
                                                Optional.of(new net.neoforged.neoforge.common.conditions.WithConditions<>(modifiedAdvancement, conditions)),
                                                EnderscapeRecipeProvider.this.advancementPathProvider.json(ResourceLocation.fromNamespaceAndPath(Enderscape.MOD_ID, advancement.id().getPath()))
                                        )
                                );
                            }
                        }
                    }

                    @SuppressWarnings("removal")
                    @Override
                    public Advancement.Builder advancement() {
                        return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
                    }
                }, registries
        );
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    private void rubbleShield(RecipeOutput output, ItemLike stone, ItemLike shield) {
        shaped(RecipeCategory.COMBAT, shield)
                .define('C', EnderscapeItems.RUBBLE_CHITIN.get())
                .define('S', EnderscapeItems.SHADOLINE_INGOT.get())
                .define('#', stone)
                .pattern("CSC")
                .pattern("C#C")
                .pattern(" C ")
                .unlockedBy("has_rubble_chitin", has(EnderscapeItems.RUBBLE_CHITIN.get()))
                .group("rubble_shield")
                .save(output);
    }
}