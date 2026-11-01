package net.bunten.enderscape.client.registry;

import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import static net.bunten.enderscape.registry.EnderscapeItems.*;

public class EnderscapeItemColorProviders {

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        // Layer 1 of the dyed mirror model is the glass, layer 0 is the frame (untinted)
        event.register((stack, tintIndex) -> tintIndex == 1 ? DyedItemColor.getOrDefault(stack, -1) : -1, MIRROR.get());
    }
}
