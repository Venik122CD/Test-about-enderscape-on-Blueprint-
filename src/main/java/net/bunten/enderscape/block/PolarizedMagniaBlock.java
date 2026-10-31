package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.MagniaPolarityProperty;
import net.bunten.enderscape.block.properties.MagniaType;
import net.bunten.enderscape.block.properties.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.util.MagniaUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;

import java.util.Optional;
import java.util.function.ToIntFunction;

/** Flips its polarity every time it receives a fresh redstone signal. */
public class PolarizedMagniaBlock extends Block implements HasMagniaPolarity, HasMagniaPowerSignal {
    public static final MapCodec<PolarizedMagniaBlock> CODEC = simpleCodec(PolarizedMagniaBlock::new);
    public static final BooleanProperty POWERED = StateProperties.POWERED;
    public static final EnumProperty<MagniaPolarityProperty> POLARITY = StateProperties.MAGNIA_POLARITY;

    @Override
    protected MapCodec<? extends PolarizedMagniaBlock> codec() {
        return CODEC;
    }

    public PolarizedMagniaBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POLARITY, MagniaPolarityProperty.ALLURING).setValue(POWERED, false));
    }

    public static MagniaPolarityProperty getPolarityProperty(BlockState state) {
        return state.getValue(POLARITY);
    }

    public static ToIntFunction<BlockState> getLightLevel() {
        return state -> 14;
    }

    public static MapColor getMapColor(BlockState state) {
        return getPolarityProperty(state).getMapColor();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POLARITY, POWERED);
    }

    @Override
    public Optional<MagniaType> getPolarity(BlockState state) {
        return Optional.of(getPolarityProperty(state).get());
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return MagniaUtil.isMatchingPolarity(state, neighbor) ? 15 : 0;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel server) checkAndFlip(state, server, pos);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moved) {
        if (level instanceof ServerLevel server) checkAndFlip(state, server, pos);
    }

    public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
        boolean powered = level.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            BlockState updated = state;
            if (!state.getValue(POWERED)) {
                updated = state.cycle(POLARITY);
                level.playSound(null, pos, EnderscapeBlockSounds.POLARIZED_MAGNIA_SWAP_POLARITY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            level.setBlock(pos, updated.setValue(POWERED, powered), 3);
        }
    }
}
