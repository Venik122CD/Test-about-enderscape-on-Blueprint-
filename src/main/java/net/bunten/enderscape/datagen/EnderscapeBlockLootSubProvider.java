package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.block.VeiledLeafPileBlock;
import net.bunten.enderscape.block.VoidShaleBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Function;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.item.Items.END_STONE;

public class EnderscapeBlockLootSubProvider extends BlockLootSubProvider {
    protected EnderscapeBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.registryKeySet().stream()
                .filter(k -> k.location().getNamespace().equals(Enderscape.MOD_ID))
                .map(BuiltInRegistries.BLOCK::get)
                .toList();
    }

    @Override
    public void generate() {
        HolderLookup.RegistryLookup<Enchantment> enchantment = registries.lookupOrThrow(Registries.ENCHANTMENT);

        dropSelf(DRIFT_JELLY_BLOCK.get());

        add(VEILED_END_STONE.get(), block -> createSingleItemTableWithSilkTouch(block, END_STONE));
        add(CELESTIAL_OVERGROWTH.get(), block -> createSingleItemTableWithSilkTouch(block, END_STONE));
        add(CORRUPT_OVERGROWTH.get(), block -> createSingleItemTableWithSilkTouch(block, MIRESTONE.get()));

        dropOther(CELESTIAL_PATH_BLOCK.get(), END_STONE);
        dropOther(CORRUPT_PATH_BLOCK.get(), END_STONE);

        add(DRY_END_GROWTH.get(), BlockLootSubProvider::createShearsOnlyDrop);
        dropPottedContents(POTTED_DRY_END_GROWTH.get());

        add(WISP_SPROUTS.get(), BlockLootSubProvider::createShearsOnlyDrop);
        add(WISP_GROWTH.get(), BlockLootSubProvider::createShearsOnlyDrop);
        dropPottedContents(POTTED_WISP_GROWTH.get());

        add(WISP_FLOWER.get(), block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));

        dropSelf(VEILED_SAPLING.get());
        dropPottedContents(POTTED_VEILED_SAPLING.get());

        add(CHORUS_SPROUTS.get(), BlockLootSubProvider::createShearsOnlyDrop);
        dropPottedContents(POTTED_CHORUS_SPROUTS.get());

        add(CELESTIAL_GROWTH.get(), BlockLootSubProvider::createShearsOnlyDrop);
        dropPottedContents(POTTED_CELESTIAL_GROWTH.get());

        add(CORRUPT_GROWTH.get(), BlockLootSubProvider::createShearsOnlyDrop);
        dropPottedContents(POTTED_CORRUPT_GROWTH.get());

        dropSelf(BULB_FLOWER.get());
        dropPottedContents(POTTED_BULB_FLOWER.get());

        dropSelf(END_LAMP.get());
        dropSelf(BULB_LANTERN.get());
        dropSelf(BLINKLAMP.get());

        add(FLANGER_BERRY_FLOWER.get(), BlockLootSubProvider::createShearsOnlyDrop);
        add(FLANGER_BERRY_VINE.get(), BlockLootSubProvider::createShearsOnlyDrop);
        add(UNRIPE_FLANGER_BERRY_BLOCK.get(), BlockLootSubProvider::createShearsOnlyDrop);
        add(RIPE_FLANGER_BERRY_BLOCK.get(), block -> createSilkTouchOrShearsDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(FLANGER_BERRY.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                        .apply(ApplyBonusCount.addUniformBonusCount(getFortune()))
                        .apply(LimitCount.limitCount(IntRange.upperBound(6)))
                )
        ));

        dropOther(BLINKLIGHT_VINES_BODY.get(), BLINKLIGHT.get());
        dropOther(BLINKLIGHT_VINES_HEAD.get(), BLINKLIGHT.get());
        dropPottedContents(POTTED_BLINKLIGHT.get());

        dropSelf(VEILED_LOG.get());
        dropSelf(STRIPPED_VEILED_LOG.get());
        dropSelf(VEILED_WOOD.get());
        dropSelf(STRIPPED_VEILED_WOOD.get());

        add(VEILED_LEAVES.get(), block -> createLeavesDrops(block, VEILED_SAPLING.get()));
        add(VEILED_LEAF_PILE.get(), createVeiledLeafPile());
        add(VEILED_VINES.get(), block -> createSilkTouchOrShearsDispatchTable(block, LootItem.lootTableItem(block).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantment.getOrThrow(Enchantments.FORTUNE), 0.33F, 0.55F, 0.77F, 1.0F))));

        dropSelf(VEILED_PLANKS.get());
        dropSelf(VEILED_STAIRS.get());
        dropSelf(VEILED_SLAB.get());
        dropSelf(VEILED_FENCE.get());
        dropSelf(VEILED_FENCE_GATE.get());
        add(VEILED_DOOR.get(), this::createDoorTable);
        dropSelf(VEILED_TRAPDOOR.get());
        dropSelf(VEILED_PRESSURE_PLATE.get());
        dropSelf(VEILED_BUTTON.get());
        dropOther(VEILED_SIGN.get(), VEILED_SIGN_ITEM.get());
        dropOther(VEILED_WALL_SIGN.get(), VEILED_SIGN_ITEM.get());
        dropOther(VEILED_HANGING_SIGN.get(), VEILED_HANGING_SIGN_ITEM.get());
        dropOther(VEILED_WALL_HANGING_SIGN.get(), VEILED_HANGING_SIGN_ITEM.get());

        dropSelf(CELESTIAL_CHANTERELLE.get());
        dropPottedContents(POTTED_CELESTIAL_CHANTERELLE.get());

        dropSelf(CELESTIAL_CAP.get());
        dropSelf(CELESTIAL_BRICKS.get());
        dropSelf(CELESTIAL_BRICK_STAIRS.get());
        dropSelf(CELESTIAL_BRICK_SLAB.get());
        dropSelf(CELESTIAL_BRICK_WALL.get());

        dropSelf(DUSK_PURPUR_BLOCK.get());
        dropSelf(DUSK_PURPUR_PILLAR.get());
        dropSelf(DUSK_PURPUR_STAIRS.get());
        dropSelf(DUSK_PURPUR_SLAB.get());
        dropSelf(DUSK_PURPUR_WALL.get());
        dropSelf(CHISELED_DUSK_PURPUR.get());

        dropSelf(PURPUR_TILES.get());
        dropSelf(PURPUR_TILE_STAIRS.get());
        dropSelf(PURPUR_TILE_SLAB.get());

        dropSelf(CELESTIAL_STEM.get());
        dropSelf(STRIPPED_CELESTIAL_STEM.get());
        dropSelf(CELESTIAL_HYPHAE.get());
        dropSelf(STRIPPED_CELESTIAL_HYPHAE.get());
        dropSelf(CELESTIAL_PLANKS.get());
        dropSelf(CELESTIAL_STAIRS.get());
        dropSelf(CELESTIAL_SLAB.get());
        dropSelf(CELESTIAL_FENCE.get());
        dropSelf(CELESTIAL_FENCE_GATE.get());
        add(CELESTIAL_DOOR.get(), this::createDoorTable);
        dropSelf(CELESTIAL_TRAPDOOR.get());
        dropSelf(CELESTIAL_PRESSURE_PLATE.get());
        dropSelf(CELESTIAL_BUTTON.get());
        dropOther(CELESTIAL_SIGN.get(), CELESTIAL_SIGN_ITEM.get());
        dropOther(CELESTIAL_WALL_SIGN.get(), CELESTIAL_SIGN_ITEM.get());
        dropOther(CELESTIAL_HANGING_SIGN.get(), CELESTIAL_HANGING_SIGN_ITEM.get());
        dropOther(CELESTIAL_WALL_HANGING_SIGN.get(), CELESTIAL_HANGING_SIGN_ITEM.get());

        dropOther(MURUBLIGHT_SHELF.get(), MURUBLIGHT_SHELF_ITEM.get());

        dropSelf(MURUBLIGHT_CHANTERELLE.get());
        dropPottedContents(POTTED_MURUBLIGHT_CHANTERELLE.get());

        dropSelf(MURUBLIGHT_CAP.get());
        dropSelf(MURUBLIGHT_BRICKS.get());
        dropSelf(MURUBLIGHT_BRICK_STAIRS.get());
        dropSelf(MURUBLIGHT_BRICK_SLAB.get());
        dropSelf(MURUBLIGHT_BRICK_WALL.get());

        dropSelf(MURUBLIGHT_STEM.get());
        dropSelf(STRIPPED_MURUBLIGHT_STEM.get());
        dropSelf(MURUBLIGHT_HYPHAE.get());
        dropSelf(STRIPPED_MURUBLIGHT_HYPHAE.get());
        dropSelf(MURUBLIGHT_PLANKS.get());
        dropSelf(MURUBLIGHT_STAIRS.get());
        dropSelf(MURUBLIGHT_SLAB.get());
        dropSelf(MURUBLIGHT_FENCE.get());
        dropSelf(MURUBLIGHT_FENCE_GATE.get());
        add(MURUBLIGHT_DOOR.get(), this::createDoorTable);
        dropSelf(MURUBLIGHT_TRAPDOOR.get());
        dropSelf(MURUBLIGHT_PRESSURE_PLATE.get());
        dropSelf(MURUBLIGHT_BUTTON.get());
        dropOther(MURUBLIGHT_SIGN.get(), MURUBLIGHT_SIGN_ITEM.get());
        dropOther(MURUBLIGHT_WALL_SIGN.get(), MURUBLIGHT_SIGN_ITEM.get());
        dropOther(MURUBLIGHT_HANGING_SIGN.get(), MURUBLIGHT_HANGING_SIGN_ITEM.get());
        dropOther(MURUBLIGHT_WALL_HANGING_SIGN.get(), MURUBLIGHT_HANGING_SIGN_ITEM.get());

        dropSelf(ALLURING_MAGNIA.get());
        dropSelf(ALLURING_MAGNIA_SPROUT.get());
        dropSelf(REPULSIVE_MAGNIA.get());
        dropSelf(REPULSIVE_MAGNIA_SPROUT.get());

        dropSelf(ETCHED_ALLURING_MAGNIA.get());
        dropSelf(ETCHED_REPULSIVE_MAGNIA.get());

        dropPottedContents(POTTED_ALLURING_MAGNIA_SPROUT.get());
        dropPottedContents(POTTED_REPULSIVE_MAGNIA_SPROUT.get());

        add(VOID_SHALE.get(), block -> {
            LootItemBlockStatePropertyCondition.Builder isNatural = LootItemBlockStatePropertyCondition.hasBlockStateProperties(VOID_SHALE.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(VoidShaleBlock.NATURAL, true));

            return applyExplosionDecay(block, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .when(entityPresent().invert())
                            .when(isNatural)
                            .when(LootItemRandomChanceCondition.randomChance(0.2F))
                            .add(LootItem.lootTableItem(block)))
                    .withPool(LootPool.lootPool()
                            .when(entityPresent().invert())
                            .when(isNatural.invert())
                            .add(LootItem.lootTableItem(block)))
                    .withPool(LootPool.lootPool()
                            .when(entityPresent())
                            .add(LootItem.lootTableItem(block)))
            );
        });

        Function<Block, LootTable.Builder> nebuliteOreDrops = block -> createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(NEBULITE_SHARDS.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(ApplyBonusCount.addOreBonusCount(enchantment.getOrThrow(Enchantments.FORTUNE)))
                                .apply(LimitCount.limitCount(IntRange.upperBound(8)))
                )
        );

        add(NEBULITE_ORE.get(), nebuliteOreDrops);
        add(MIRESTONE_NEBULITE_ORE.get(), nebuliteOreDrops);

        dropSelf(NEBULITE_BLOCK.get());

        Function<Block, LootTable.Builder> shadolineOreDrops = block -> createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(RAW_SHADOLINE.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                                .apply(ApplyBonusCount.addOreBonusCount(enchantment.getOrThrow(Enchantments.FORTUNE)))
                )
        );

        add(SHADOLINE_ORE.get(), shadolineOreDrops);
        add(MIRESTONE_SHADOLINE_ORE.get(), shadolineOreDrops);

        dropSelf(RAW_SHADOLINE_BLOCK.get());
        dropSelf(SHADOLINE_BLOCK.get());
        dropSelf(SHADOLINE_BLOCK_STAIRS.get());
        dropSelf(SHADOLINE_BLOCK_SLAB.get());
        dropSelf(SHADOLINE_BLOCK_WALL.get());
        dropSelf(CUT_SHADOLINE.get());
        dropSelf(CUT_SHADOLINE_STAIRS.get());
        dropSelf(CUT_SHADOLINE_SLAB.get());
        dropSelf(CUT_SHADOLINE_WALL.get());
        dropSelf(CHISELED_SHADOLINE.get());
        dropSelf(SHADOLINE_PILLAR.get());

        dropSelf(END_STONE_STAIRS.get());
        dropSelf(END_STONE_SLAB.get());
        dropSelf(END_STONE_WALL.get());
        dropSelf(POLISHED_END_STONE.get());
        dropSelf(POLISHED_END_STONE_STAIRS.get());
        dropSelf(POLISHED_END_STONE_SLAB.get());
        dropSelf(POLISHED_END_STONE_WALL.get());
        dropSelf(POLISHED_END_STONE_BUTTON.get());
        dropSelf(POLISHED_END_STONE_PRESSURE_PLATE.get());
        dropSelf(CHISELED_END_STONE.get());

        dropSelf(CHISELED_PURPUR.get());
        dropSelf(PURPUR_WALL.get());

        dropSelf(MIRESTONE.get());
        dropSelf(MIRESTONE_STAIRS.get());
        dropSelf(MIRESTONE_SLAB.get());
        dropSelf(MIRESTONE_WALL.get());
        dropSelf(POLISHED_MIRESTONE.get());
        dropSelf(POLISHED_MIRESTONE_STAIRS.get());
        dropSelf(POLISHED_MIRESTONE_SLAB.get());
        dropSelf(POLISHED_MIRESTONE_WALL.get());
        dropSelf(POLISHED_MIRESTONE_BUTTON.get());
        dropSelf(POLISHED_MIRESTONE_PRESSURE_PLATE.get());
        dropSelf(MIRESTONE_BRICKS.get());
        dropSelf(MIRESTONE_BRICK_STAIRS.get());
        dropSelf(MIRESTONE_BRICK_SLAB.get());
        dropSelf(MIRESTONE_BRICK_WALL.get());
        dropSelf(CHISELED_MIRESTONE.get());

        dropSelf(VERADITE.get());
        dropSelf(VERADITE_STAIRS.get());
        dropSelf(VERADITE_SLAB.get());
        dropSelf(VERADITE_WALL.get());
        dropSelf(POLISHED_VERADITE.get());
        dropSelf(POLISHED_VERADITE_STAIRS.get());
        dropSelf(POLISHED_VERADITE_SLAB.get());
        dropSelf(POLISHED_VERADITE_WALL.get());
        dropSelf(POLISHED_VERADITE_BUTTON.get());
        dropSelf(POLISHED_VERADITE_PRESSURE_PLATE.get());
        dropSelf(VERADITE_BRICKS.get());
        dropSelf(VERADITE_BRICK_STAIRS.get());
        dropSelf(VERADITE_BRICK_SLAB.get());
        dropSelf(VERADITE_BRICK_WALL.get());
        dropSelf(CHISELED_VERADITE.get());

        dropSelf(KURODITE.get());
        dropSelf(KURODITE_STAIRS.get());
        dropSelf(KURODITE_SLAB.get());
        dropSelf(KURODITE_WALL.get());
        dropSelf(POLISHED_KURODITE.get());
        dropSelf(POLISHED_KURODITE_STAIRS.get());
        dropSelf(POLISHED_KURODITE_SLAB.get());
        dropSelf(POLISHED_KURODITE_WALL.get());
        dropSelf(POLISHED_KURODITE_BUTTON.get());
        dropSelf(POLISHED_KURODITE_PRESSURE_PLATE.get());
        dropSelf(KURODITE_BRICKS.get());
        dropSelf(KURODITE_BRICK_STAIRS.get());
        dropSelf(KURODITE_BRICK_SLAB.get());
        dropSelf(KURODITE_BRICK_WALL.get());
        dropSelf(CHISELED_KURODITE.get());

        add(END_VAULT.get(), noDrop());
        add(END_TRIAL_SPAWNER.get(), noDrop());
        add(CHORUS_CAKE_ROLL.get(), noDrop());
    }

    private Function<Block, LootTable.Builder> createVeiledLeafPile() {
        return block -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(VeiledLeafPileBlock.LAYERS.getPossibleValues(), integer -> integer == 8 ? LootItem.lootTableItem(VEILED_LEAVES.get()) : LootItem.lootTableItem(VEILED_LEAF_PILE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(integer))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, integer))))));
    }

    public LootTable.Builder createLeavesDrops(Block leaves, ItemLike plant) {
        HolderLookup.RegistryLookup<Enchantment> enchantment = registries.lookupOrThrow(Registries.ENCHANTMENT);
        return createSilkTouchOrShearsDispatchTable(
                leaves,
                applyExplosionCondition(leaves, LootItem.lootTableItem(plant)).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantment.getOrThrow(Enchantments.FORTUNE), 0.05F, 0.0625F, 0.083333336F, 0.1F))
        )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .when(HAS_SHEARS.or(this.hasSilkTouch()).invert())
                                .add(applyExplosionDecay(
                                        leaves, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantment.getOrThrow(Enchantments.FORTUNE), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))
                                )
                );
    }


    @NotNull
    private Holder.Reference<Enchantment> getFortune() {
        return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
    }

    public LootItemCondition.Builder entityPresent() {
        return LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS);
    }
}