package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.particle.*;
import net.minecraft.client.particle.FlyTowardsPositionParticle;
import net.minecraft.client.particle.TrialSpawnerDetectionParticle;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import static net.bunten.enderscape.registry.EnderscapeParticles.*;

public class EnderscapeParticleProviders {
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ALLURING_MAGNIA.get(), AlluringMagniaParticle.Provider::new);
        event.registerSpriteSet(BLINKLIGHT_SPORES.get(), BlinklightSporesParticle.Provider::new);
        event.registerSpriteSet(CELESTIAL_SPORES.get(), CelestialSporesParticle.Provider::new);
        event.registerSpriteSet(CHORUS_POLLEN.get(), ChorusPollenParticle.Provider::new);
        event.registerSpriteSet(CORRUPT_SPORES.get(), CorruptSporesParticle.Provider::new);
        event.registerSpriteSet(DASH_JUMP_SHOCKWAVE.get(), DashJumpShockwaveParticle.Provider::new);
        event.registerSpriteSet(DASH_JUMP_SPARKS.get(), DashJumpSparksParticle.Provider::new);
        event.registerSpriteSet(DRIFT_JELLY_DRIPPING.get(), DriftJellyDrippingParticle.Provider::new);
        event.registerSpriteSet(ENDER_PEARL.get(), EnderPearlParticle.Provider::new);
        event.registerSpriteSet(END_TRIAL_SPAWNER_DETECTION.get(), TrialSpawnerDetectionParticle.Provider::new);
        event.registerSpriteSet(END_TRIAL_SPAWNER_EXHALE.get(), EndTrialSpawnerExhaleParticle.Provider::new);
        event.registerSpriteSet(END_VAULT_CONNECTION.get(), FlyTowardsPositionParticle.VaultConnectionProvider::new);
        event.registerSpriteSet(END_PORTAL_STARS.get(), EndPortalStarParticle.Provider::new);
        event.registerSpriteSet(MIRROR_TELEPORT_IN.get(), MirrorTeleportInParticle.Provider::new);
        event.registerSpriteSet(MIRROR_TELEPORT_OUT.get(), MirrorTeleportOutParticle.Provider::new);
        event.registerSpriteSet(NEBULITE_ORE.get(), NebuliteOreParticle.Provider::new);
        event.registerSpriteSet(REPULSIVE_MAGNIA.get(), RepulsiveMagniaParticle.Provider::new);
        event.registerSpriteSet(RUSTLE_SLEEPING_BUBBLE.get(), RustleSleepingBubbleParticle.Provider::new);
        event.registerSpriteSet(RUSTLE_SLEEPING_BUBBLE_POP.get(), RustleSleepingBubblePopParticle.Provider::new);
        event.registerSpriteSet(VEILED_LEAVES.get(), VeiledLeavesParticle.Provider::new);
        event.registerSpriteSet(VOID_POOF.get(), VoidPoofParticle.Provider::new);
        event.registerSpriteSet(VOID_STARS.get(), VoidStarParticle.Provider::new);
    }
}
