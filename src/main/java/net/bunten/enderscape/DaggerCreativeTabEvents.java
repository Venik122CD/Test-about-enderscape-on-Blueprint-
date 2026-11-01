package net.bunten.enderscape;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/** Puts the dagger into the vanilla Combat tab, right before the shield. */
@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DaggerCreativeTabEvents {

    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT && EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) {
            event.insertBefore(new ItemStack(Items.SHIELD), new ItemStack(EnderscapeItems.DAGGER.get()), net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
