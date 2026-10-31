package net.bunten.enderscape;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

/** Stunned players can neither attack nor use items. */
@EventBusSubscriber(modid = Enderscape.MOD_ID)
public class StunEvents {

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (EnderscapeMobEffects.isStunned(event.getEntity())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onUseItem(LivingEntityUseItemEvent.Start event) {
        if (EnderscapeMobEffects.isStunned(event.getEntity())) event.setCanceled(true);
    }
}
