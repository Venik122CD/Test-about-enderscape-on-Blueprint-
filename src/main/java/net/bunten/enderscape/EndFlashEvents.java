package net.bunten.enderscape;

import net.bunten.enderscape.registry.EnderscapeEventSounds;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Occasional End flashes (sound only; the 1.21.11 sky visuals rely on renderer features that 1.21.1 does not have)
 * and the low gravity apply sound.
 */
@EventBusSubscriber(modid = Enderscape.MOD_ID)
public class EndFlashEvents {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level) || level.dimension() != Level.END) return;
        if (!EnderscapeConfig.getInstance().flashEnabled) return;
        if (level.getGameTime() % 20 != 0 || level.getRandom().nextInt(480) != 0) return;

        for (ServerPlayer player : level.players()) {
            level.playSound(null, player.blockPosition(), EnderscapeEventSounds.END_FLASH, SoundSource.AMBIENT, 4.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().is(EnderscapeMobEffects.LOW_GRAVITY) && !event.getEntity().level().isClientSide()) {
            event.getEntity().level().playSound(null, event.getEntity().blockPosition(), EnderscapeEventSounds.APPLY_EFFECT_LOW_GRAVITY, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
