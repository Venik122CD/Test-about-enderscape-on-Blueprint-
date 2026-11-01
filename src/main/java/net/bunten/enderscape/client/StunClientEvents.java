package net.bunten.enderscape.client;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

   
                                                                                                               
                                                                                      
   
@EventBusSubscriber(modid = Enderscape.MOD_ID, value = Dist.CLIENT)
public class StunClientEvents {

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (EnderscapeMobEffects.isStunned(event.getEntity())) {
            var input = event.getInput();
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.forwardImpulse = 0;
            input.leftImpulse = 0;
            input.jumping = false;
            input.shiftKeyDown = false;
        }
    }

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean stunned = EnderscapeMobEffects.isStunned(player);
        boolean daggerRecharging = event.isAttack() && player.getMainHandItem().is(EnderscapeItems.DAGGER.get()) && player.getAttackStrengthScale(0.0F) < 1;

        if (stunned || daggerRecharging) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }
}
