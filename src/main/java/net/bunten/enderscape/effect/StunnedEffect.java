package net.bunten.enderscape.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class StunnedEffect extends MobEffect {
    public StunnedEffect() {
        super(MobEffectCategory.HARMFUL, 0x061914);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Mob mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);
        }
        if (entity.onGround()) entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
        return true;
    }
}
