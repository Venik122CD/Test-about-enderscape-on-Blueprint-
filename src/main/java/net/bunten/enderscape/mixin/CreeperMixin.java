package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin {
    @Unique
    private final Creeper Enderscape$creeper = (Creeper) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    private void Enderscape$stopSwellingWhenStunned(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(Enderscape$creeper)) Enderscape$creeper.setSwellDir(-1);
    }
}
