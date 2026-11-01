package net.bunten.enderscape.mixin;

import net.bunten.enderscape.item.component.AttackSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

                                                                                                              
@Mixin(Player.class)
public abstract class PlayerAttackSoundsMixin {
    @Unique
    private final Player Enderscape$attackSoundPlayer = (Player) (Object) this;

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
            ),
            index = 4
    )
    private SoundEvent Enderscape$changeAttackSound(SoundEvent original) {
        ItemStack stack = Enderscape$attackSoundPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.isEmpty() || !AttackSounds.is(stack)) return original;

        AttackSounds sounds = AttackSounds.get(stack);

        if (original == SoundEvents.PLAYER_ATTACK_KNOCKBACK) return sounds.knockback().value();
        if (original == SoundEvents.PLAYER_ATTACK_SWEEP) return sounds.sweep().value();
        if (original == SoundEvents.PLAYER_ATTACK_CRIT) return sounds.crit().value();
        if (original == SoundEvents.PLAYER_ATTACK_STRONG) return sounds.strong().value();
        if (original == SoundEvents.PLAYER_ATTACK_WEAK) return sounds.weak().value();
        if (original == SoundEvents.PLAYER_ATTACK_NODAMAGE) return sounds.noDamage().value();

        return original;
    }
}
