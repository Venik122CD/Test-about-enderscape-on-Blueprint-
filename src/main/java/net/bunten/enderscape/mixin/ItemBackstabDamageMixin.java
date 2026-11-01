package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

                                                                                                                               
@Mixin(Item.class)
public abstract class ItemBackstabDamageMixin {
    @Inject(method = "getAttackDamageBonus", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getAttackDamageBonus(Entity target, float damage, DamageSource source, CallbackInfoReturnable<Float> info) {
        if (source.getDirectEntity() instanceof LivingEntity attacker && EnderscapeAttributes.isBackstab(source.getWeaponItem(), attacker.position(), target)) {
            info.setReturnValue((float) (info.getReturnValue() + EnderscapeAttributes.getBackstabDamage(attacker)));
        }
    }
}
