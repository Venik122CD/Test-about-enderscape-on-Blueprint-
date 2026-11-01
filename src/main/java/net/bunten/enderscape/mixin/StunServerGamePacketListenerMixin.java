package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

                                                               
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class StunServerGamePacketListenerMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handleInteract", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelStunnedInteract(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) info.cancel();
    }
}
