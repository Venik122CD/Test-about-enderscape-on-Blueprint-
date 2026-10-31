package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaType;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MagniaUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Optional;

public class MagniaBlock extends Block implements HasMagniaPolarity, HasMagniaPowerSignal {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    private final MagniaType magniaType;

    public MagniaBlock(MagniaType magniaType, Properties properties) {
        super(properties);
        this.magniaType = magniaType;
        registerDefaultState(stateDefinition.any().setValue(POWER, 0));
    }

    public static MagniaType getMagniaType(BlockState state) {
        return state.getBlock() instanceof MagniaBlock block ? block.magniaType : null;
    }

    public static int getPower(BlockState state) {
        return state.hasProperty(POWER) ? state.getValue(POWER) : 0;
    }

    @Override
    public Optional<MagniaType> getPolarity(BlockState state) {
        return Optional.of(magniaType);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getPower(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        return state.setValue(POWER, MagniaUtil.getStrongestPowerSignal(state, context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moved) {
        int power = MagniaUtil.getStrongestPowerSignal(state, level, pos);
        if (power != getPower(state)) level.setBlockAndUpdate(pos, state.setValue(POWER, power));
    }

    @Override
    protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int flags, int depth) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            mutable.setWithOffset(pos, dir);
            level.neighborShapeChanged(dir.getOpposite(), state, mutable, mutable.relative(dir.getOpposite()), flags, depth);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (!oldState.is(state.getBlock()) && !level.isClientSide()) {
            updateAllNeighbors(level, pos);
            updateNeighborsOfNeighboringMagnia(state, level, pos);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && !state.is(newState.getBlock())) {
            updateAllNeighbors(level, pos);
            updateNeighborsOfNeighboringMagnia(state, level, pos);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    private void updateNeighborsOfNeighboringMagnia(BlockState state, Level level, BlockPos pos) {
        for (Direction first : Direction.values()) {
            BlockPos neighbor = pos.relative(first);
            if (MagniaUtil.isMatchingPolarity(state, level.getBlockState(neighbor))) {
                level.updateNeighborsAt(neighbor, this);
                for (Direction second : Direction.values()) updateAllNeighbors(level, neighbor.relative(second));
            }
        }
    }

    private void updateAllNeighbors(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) level.updateNeighborsAt(pos.relative(direction), this);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (getPower(state) == 0 && random.nextInt(800) == 0 && !BlockUtil.isBlockObstructed(level, pos)) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, magniaType.getHumSound(), SoundSource.AMBIENT, 1, 1, false);
        }
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return MagniaUtil.isMatchingPolarity(state, neighbor) ? getPower(state) : 0;
    }
}
