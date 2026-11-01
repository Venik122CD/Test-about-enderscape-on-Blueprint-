package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class StunMobMixin {
    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    public void Enderscape$stunServerAiStep(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned((Mob) (Object) this)) info.cancel();
    }
}
