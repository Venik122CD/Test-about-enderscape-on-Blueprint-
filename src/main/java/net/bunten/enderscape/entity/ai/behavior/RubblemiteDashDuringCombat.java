package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

/** Starts the wind-up that precedes a dash. */
public class RubblemiteDashDuringCombat extends Behavior<Rubblemite> {
    public RubblemiteDashDuringCombat() {
        super(ImmutableMap.of(
                EnderscapeMemory.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                EnderscapeMemory.RUBBLEMITE_PREPARING_DASH.get(), MemoryStatus.VALUE_ABSENT,
                EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Rubblemite mob) {
        if (mob.getFlags() == Rubblemite.DEFAULT_FLAG && !mob.isVehicle() && mob.onGround() && !mob.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            double distance = mob.distanceToSqr(EnderscapeMemory.getAttackTarget(mob));
            if (distance >= 6 && distance <= 35) return mob.getRandom().nextInt(10) == 0;
        }
        return false;
    }

    @Override
    protected void start(ServerLevel level, Rubblemite mob, long time) {
        mob.prepareDash();
    }
}
