package net.bunten.enderscape;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * Simplified port of the 1.21.11 dagger: hitting an enemy from behind deals bonus damage and stuns it for longer.
 */
@EventBusSubscriber(modid = Enderscape.MOD_ID)
public class DaggerEvents {

    public static final float BACKSTAB_BONUS_DAMAGE = 3.0F;
    public static final int BACKSTAB_ANGLE = 110;
    public static final int STUN_TICKS = 20;
    public static final float BACKSTAB_STUN_MULTIPLIER = 4.0F;

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker) || event.getSource().getDirectEntity() != attacker) return;
        if (!attacker.getMainHandItem().is(EnderscapeItems.DAGGER.get())) return;

        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;

        Vec3 facing = Vec3.directionFromRotation(0, victim.getYHeadRot()).multiply(1, 0, 1).normalize();
        Vec3 toAttacker = attacker.position().subtract(victim.position()).multiply(1, 0, 1).normalize();
        boolean backstab = facing.dot(toAttacker) < -Math.cos(Math.toRadians(BACKSTAB_ANGLE / 2.0D));

        if (attacker instanceof net.minecraft.server.level.ServerPlayer serverPlayer) net.bunten.enderscape.registry.EnderscapeCriteria.STUN_ATTACK.trigger(serverPlayer, backstab);

        if (backstab) {
            event.setAmount(event.getAmount() + (float) net.bunten.enderscape.registry.EnderscapeAttributes.getBackstabDamage(attacker));
            victim.addEffect(new MobEffectInstance(EnderscapeMobEffects.STUNNED, (int) (STUN_TICKS * BACKSTAB_STUN_MULTIPLIER)));
            victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(), EnderscapeItemSounds.DAGGER_BACKSTAB.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        } else {
            victim.addEffect(new MobEffectInstance(EnderscapeMobEffects.STUNNED, STUN_TICKS));
            victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(), EnderscapeItemSounds.DAGGER_STUN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
