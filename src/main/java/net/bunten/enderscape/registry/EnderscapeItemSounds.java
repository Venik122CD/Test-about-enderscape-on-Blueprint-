package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class EnderscapeItemSounds {

    public static final Holder<SoundEvent> DRIFT_JELLY_BOTTLE_DRINK = registerHolder("drift_jelly_bottle.drink");
    public static final Holder<SoundEvent> DRIFT_JELLY_BOTTLE_FINISH = registerHolder("drift_jelly_bottle.finish");
    public static final Holder<SoundEvent> DRIFT_LEGGINGS_EQUIP = registerHolder("drift_leggings.equip");
    public static final Holder<SoundEvent> ELYTRA_EQUIP = registerHolder("elytra.equip");
    public static final Holder<SoundEvent> RUBBLE_SHIELD_COOLDOWN_OVER = registerHolder("rubble_shield.cooldown_over");
    public static final Holder<SoundEvent> RUBBLE_SHIELD_DASH = registerHolder("rubble_shield.dash");
    public static final Holder<SoundEvent> SHULKER_SHELL_EQUIP = registerHolder("shulker_shell.equip");

    public static final Supplier<SoundEvent> CRACKED_MIRROR_TRY_LINK = register("cracked_mirror.try_link");
    public static final Supplier<SoundEvent> CRACKED_MIRROR_TRY_TELEPORT = register("cracked_mirror.try_teleport");
    public static final Supplier<SoundEvent> ELYTRA_BREAK = register("elytra.break");
    public static final Supplier<SoundEvent> ELYTRA_GLIDING = register("elytra.gliding");
    public static final Supplier<SoundEvent> ELYTRA_LAND = register("elytra.land");
    public static final Supplier<SoundEvent> ELYTRA_START_GLIDING = register("elytra.start_gliding");
    public static final Supplier<SoundEvent> ELYTRA_STOP_GLIDING = register("elytra.stop_gliding");
    public static final Supplier<SoundEvent> ENDER_PEARL_LAND = register("ender_pearl.land");
    public static final Supplier<SoundEvent> ENDER_PEARL_THROW = register("ender_pearl.throw");
    public static final Supplier<SoundEvent> MAGNIA_ATTRACTOR_MOVE = register("magnia_attractor.move");
    public static final Supplier<SoundEvent> MAGNIA_ATTRACTOR_POWER_OFF = register("magnia_attractor.power_off");
    public static final Supplier<SoundEvent> MAGNIA_ATTRACTOR_POWER_ON = register("magnia_attractor.power_on");
    public static final Supplier<SoundEvent> MAGNIA_ATTRACTOR_USE_FUEL = register("magnia_attractor.use_fuel");
    public static final Supplier<SoundEvent> MIRROR_FAILURE = register("mirror.failure");
    public static final Supplier<SoundEvent> MIRROR_LINK = register("mirror.link");
    public static final Supplier<SoundEvent> MIRROR_TELEPORT = register("mirror.teleport");
    public static final Supplier<SoundEvent> MIRROR_TRANSDIMENSIONAL_TRAVEL = register("mirror.transdimensional_travel");
    public static final Supplier<SoundEvent> NEBULITE_TOOL_ADD_FUEL = register("nebulite_tool.add_fuel");
    public static final Supplier<SoundEvent> NEBULITE_TOOL_FUEL_FULL = register("nebulite_tool.fuel_full");
    public static final Supplier<SoundEvent> RUBBLE_SHIELD_BLOCK = register("rubble_shield.block");
    public static final Supplier<SoundEvent> RUSTLE_BUCKET_EMPTY = register("rustle_bucket.empty");
    public static final Supplier<SoundEvent> RUSTLE_BUCKET_FILL = register("rustle_bucket.fill");
    public static final Supplier<SoundEvent> TRIDENT_WARP = register("trident.warp");

    private static Supplier<SoundEvent> register(String name) {
        var event = Enderscape.registerSoundEvent("item." + name);
        return () -> event;
    }

    private static Holder<SoundEvent> registerHolder(String name) {
        return Enderscape.registerSoundEventHolder("item." + name);
    }
}