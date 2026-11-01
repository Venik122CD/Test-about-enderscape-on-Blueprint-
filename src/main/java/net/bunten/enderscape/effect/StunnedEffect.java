package net.bunten.enderscape.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** Stunned entities cannot think, move, turn, attack or use items; that behaviour lives in the stun mixins and events. */
public class StunnedEffect extends MobEffect {
    public StunnedEffect() {
        super(MobEffectCategory.HARMFUL, 0x061914);
    }
}
