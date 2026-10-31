package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface HasMagniaPolarity {
    static boolean has(Block block) {
        return block instanceof HasMagniaPolarity;
    }

    static boolean has(BlockState state) {
        return has(state.getBlock());
    }

    static HasMagniaPolarity cast(BlockState state) {
        return (HasMagniaPolarity) state.getBlock();
    }

    static Optional<MagniaType> optional(BlockState state) {
        return has(state) ? cast(state).getPolarity(state) : Optional.empty();
    }

    Optional<MagniaType> getPolarity(BlockState state);
}
