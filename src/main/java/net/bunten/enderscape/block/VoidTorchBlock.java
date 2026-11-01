package net.bunten.enderscape.block;

import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class VoidTorchBlock extends TorchBlock {

    public VoidTorchBlock(BlockBehaviour.Properties properties) {
        super(ParticleTypes.FLAME, properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
        level.addParticle(EnderscapeParticles.VOID_FIRE_FLAME.get(), x, y, z, 0.0, 0.0, 0.0);
    }
}
