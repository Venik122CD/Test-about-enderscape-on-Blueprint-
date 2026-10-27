package net.bunten.enderscape.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.function.Supplier;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.minecraft.world.level.block.Blocks.*;

public class EnderscapePoi {

    public static final Supplier<PoiType> DRIFTER_HOME = RegistryHelper.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE,
            Enderscape.id("drifter_home"),
            () -> new PoiType(
                    ImmutableList.of(
                                    RIPE_FLANGER_BERRY_BLOCK.get(),
                                    UNRIPE_FLANGER_BERRY_BLOCK.get(),
                                    FLANGER_BERRY_FLOWER.get()
                            )
                            .stream()
                            .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
                            .collect(ImmutableSet.toImmutableSet()),
                    4,
                    8
            )
    );

    public static final Supplier<PoiType> RUSTLE_SLEEPING_SPOT = RegistryHelper.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE,
            Enderscape.id("rustle_sleeping_spot"),
            () -> new PoiType(
                    ImmutableList.of(
                                    VEILED_LEAF_PILE.get(),
                                    BLACK_CARPET,
                                    BLUE_CARPET,
                                    BROWN_CARPET,
                                    CYAN_CARPET,
                                    GRAY_CARPET,
                                    GREEN_CARPET,
                                    LIGHT_BLUE_CARPET,
                                    LIGHT_GRAY_CARPET,
                                    LIME_CARPET,
                                    MAGENTA_CARPET,
                                    ORANGE_CARPET,
                                    PINK_CARPET,
                                    PURPLE_CARPET,
                                    RED_CARPET,
                                    WHITE_CARPET,
                                    YELLOW_CARPET,
                                    MOSS_CARPET
                            )
                            .stream()
                            .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
                            .collect(ImmutableSet.toImmutableSet()),
                    1,
                    8
            )
    );
}