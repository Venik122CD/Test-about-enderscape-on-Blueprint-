package net.bunten.enderscape.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** Nebulite blocks give off a full-strength magnia power signal. */
public class NebuliteBlock extends Block implements HasMagniaPowerSignal {
    public NebuliteBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return 15;
    }
}
