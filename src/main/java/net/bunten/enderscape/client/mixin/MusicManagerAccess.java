package net.bunten.enderscape.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;

@Mixin(MusicManager.class)
public interface MusicManagerAccess {

    @Accessor
    SoundInstance getCurrentMusic();

    @Accessor
    int getNextSongDelay();
}