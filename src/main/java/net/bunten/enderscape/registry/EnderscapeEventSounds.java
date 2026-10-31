package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.sounds.SoundEvent;

public class EnderscapeEventSounds {
    public static final SoundEvent APPLY_EFFECT_LOW_GRAVITY = register("event.mob_effect.low_gravity");
    public static final SoundEvent END_FLASH = register("ambient.weather.end_flash");

    private static SoundEvent register(String name) {
        return Enderscape.registerSoundEvent(name);
    }
}
