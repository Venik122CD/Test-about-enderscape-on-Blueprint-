package net.bunten.enderscape;

import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

@EventBusSubscriber(modid = Enderscape.MOD_ID)
public class StealthEvents {

    @SubscribeEvent
    public static void onVisibility(LivingEvent.LivingVisibilityEvent event) {
        event.modifyVisibility(EnderscapeAttributes.getStealthMultiplier(event.getEntity()));
    }
}
