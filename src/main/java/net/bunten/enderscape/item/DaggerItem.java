package net.bunten.enderscape.item;

import net.bunten.enderscape.item.component.ThresholdCounter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The dagger. It is a nebulite tool (fuel, fuel bar, refuelling by clicking nebulite onto it).
 * <p>
 * Stunning and the backstab sound are applied by {@code DaggerEnchantmentHelperMixin}, which hooks
 * {@code EnchantmentHelper.doPostAttackEffects} so that players and mobs are both covered. The backstab damage bonus comes
 * from {@code ItemBackstabDamageMixin}. Every hit counts towards the fuel threshold here; a stun counts once more.
 */
public class DaggerItem extends NebuliteToolItem {
    public DaggerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean displayHudWhen(NebuliteToolContext context) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) {
        if (attacker.level() instanceof ServerLevel level) ThresholdCounter.useFuel(level, attacker, stack);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }
}
