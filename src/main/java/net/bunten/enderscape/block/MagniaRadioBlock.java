package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Magnia radio. In 1.21.11 the radio can also play songs from a datapack registry, but that registry ships empty,
 * so this port only implements the part that is actually used: toggling it and its random ambient noise.
 */
public class MagniaRadioBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<MagniaRadioBlock> CODEC = simpleCodec(MagniaRadioBlock::new);
    public static final BooleanProperty ENABLED = StateProperties.ENABLED;
    public static final BooleanProperty POWERED = StateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = StateProperties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = StateProperties.HORIZONTAL_FACING;

    public MagniaRadioBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ENABLED, false).setValue(POWERED, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<MagniaRadioBlock> codec() {
        return CODEC;
    }

    public static boolean isEnabled(BlockState state) {
        return state.getValue(ENABLED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED, POWERED, WATERLOGGED, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean water = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return defaultBlockState().setValue(ENABLED, true).setValue(WATERLOGGED, water).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape base = box(0, 0, 3, 16, 12, 13);
        return switch (state.getValue(FACING)) {
            case EAST, WEST -> Shapes.box(3, 0, 0, 13, 12, 16);
            default -> base;
        };
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (!level.isClientSide()) {
            BlockState updated = state.cycle(ENABLED);
            level.setBlockAndUpdate(pos, updated);
            level.playSound(null, pos, isEnabled(updated) ? EnderscapeBlockSounds.MAGNIA_RADIO_POWER_ON : EnderscapeBlockSounds.MAGNIA_RADIO_POWER_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isEnabled(state) && random.nextInt(32) == 0) {
            level.playSound(null, pos, EnderscapeBlockSounds.MAGNIA_RADIO_AMBIENT, SoundSource.RECORDS, 1.0F, 1.0F);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moved) {
        boolean signal = level.hasNeighborSignal(pos);
        if (signal != state.getValue(POWERED)) level.setBlock(pos, state.setValue(POWERED, signal), 3);
    }
}
