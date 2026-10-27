package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.biome.util.SkyParameters;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeRegistries {
    public static final ResourceKey<Registry<SkyParameters>> SKY_PARAMETERS_KEY = ResourceKey.createRegistryKey(Enderscape.id("end_sky_parameters"));
    
    @SubscribeEvent
    public static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(SKY_PARAMETERS_KEY, SkyParameters.CODEC, SkyParameters.CODEC);
    }
}
