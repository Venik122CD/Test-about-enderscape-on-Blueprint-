package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Stunned entities cannot turn (including the camera of a stunned player) and do not kick up sprint particles. */
@Mixin(Entity.class)
public abstract class StunEntityMixin {
    @Unique
    private final Entity Enderscape$stunSelf = (Entity) (Object) this;

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void Enderscape$cancelTurning(double yaw, double pitch, CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(Enderscape$stunSelf)) info.cancel();
    }

    @Inject(method = "canSpawnSprintParticle", at = @At("RETURN"), cancellable = true)
    public void Enderscape$cancelSprintParticles(CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeMobEffects.isStunned(Enderscape$stunSelf)) info.setReturnValue(false);
    }
}
