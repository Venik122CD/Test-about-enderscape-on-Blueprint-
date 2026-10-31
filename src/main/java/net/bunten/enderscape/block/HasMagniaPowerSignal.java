package net.bunten.enderscape.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface HasMagniaPowerSignal {
    static boolean has(Block block) {
        return block instanceof HasMagniaPowerSignal;
    }

    static boolean has(BlockState state) {
        return has(state.getBlock());
    }

    static int get(BlockState state, BlockState neighbor) {
        return has(state) ? ((HasMagniaPowerSignal) state.getBlock()).getMagniaPowerSignal(state, neighbor) : 0;
    }

    int getMagniaPowerSignal(BlockState state, BlockState neighbor);
}
