package net.bunten.enderscape;

import net.bunten.enderscape.item.MagniaAttractorItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

/** Magnia attractors with the Bundling enchantment put picked-up items straight into bundles in the player's inventory. */
@EventBusSubscriber(modid = Enderscape.MOD_ID)
public class MagnetBundlingEvents {

    @SubscribeEvent
    public static void onPickup(ItemEntityPickupEvent.Pre event) {
        Player player = event.getPlayer();
        if (player.level().isClientSide()) return;

        ItemStack attractor = MagniaAttractorItem.getValidAttractor(player.getInventory());
        if (attractor.isEmpty() || !MagniaAttractorItem.isEnabled(attractor) || !MagniaAttractorItem.canDepositIntoBundles(attractor)) return;

        ItemEntity entity = event.getItemEntity();
        ItemStack stack = entity.getItem();
        int before = stack.getCount();

        for (ItemStack bundle : player.getInventory().items) {
            if (stack.isEmpty()) break;
            BundleContents contents = bundle.get(DataComponents.BUNDLE_CONTENTS);
            if (contents == null) continue;
            BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
            if (mutable.tryInsert(stack) > 0) bundle.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
        }

        if (stack.getCount() != before) {
            player.take(entity, before - stack.getCount());
            if (stack.isEmpty()) {
                entity.discard();
                event.setCanPickup(TriState.FALSE);
            }
        }
    }
}
