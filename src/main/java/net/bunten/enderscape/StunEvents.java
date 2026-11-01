package net.bunten.enderscape;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

                                                                                                    
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

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (EnderscapeMobEffects.isStunned(event.getEntity())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (EnderscapeMobEffects.isStunned(event.getPlayer())) event.setCanceled(true);
    }
}
