package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Stunned living entities cannot attack or walk. */
@Mixin(LivingEntity.class)
public abstract class StunLivingEntityMixin {
    @Unique
    private final LivingEntity Enderscape$stunnedEntity = (LivingEntity) (Object) this;

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void Enderscape$canAttack(LivingEntity target, CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeMobEffects.isStunned(Enderscape$stunnedEntity)) info.setReturnValue(false);
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    public Vec3 Enderscape$cancelTravelInput(Vec3 travelVector) {
        return EnderscapeMobEffects.isStunned(Enderscape$stunnedEntity) ? Vec3.ZERO : travelVector;
    }

    @Inject(method = "travel", at = @At("TAIL"))
    public void Enderscape$stopPathing(Vec3 travelVector, CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(Enderscape$stunnedEntity) && Enderscape$stunnedEntity instanceof Mob mob && mob.getNavigation().getPath() != null) {
            mob.getNavigation().stop();
        }
    }
}
