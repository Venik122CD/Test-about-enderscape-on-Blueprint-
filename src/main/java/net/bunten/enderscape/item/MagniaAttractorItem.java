package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Predicate;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.*;

public class MagniaAttractorItem extends NebuliteToolItem {
    public MagniaAttractorItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean displayHudWhen(NebuliteToolContext context) {
        return is(context.stack()) && fuelExceedsCost(context) && isEnabled(context.stack());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return tryCyclePower(new NebuliteToolContext(player.getItemInHand(hand), level, player)) ? InteractionResultHolder.consume(player.getItemInHand(hand)) : super.use(level, player, hand);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.isEmpty()) return tryCyclePower(new NebuliteToolContext(stack, player.level(), player));
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    private boolean tryCyclePower(NebuliteToolContext context) {
        if (fuelExceedsCost(context)) {
            ItemStack stack = context.stack();
            setEnabled(stack, !isEnabled(stack));

            context.user().playSound(isEnabled(stack) ? EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_ON.get() : EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_OFF.get(), 1, 1);
            context.user().gameEvent(GameEvent.ITEM_INTERACT_FINISH);

            return true;
        } else {
            return false;
        }
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof MagniaAttractorItem;
    }

    public static ItemStack getValidAttractor(Inventory inventory) {
        Player player = inventory.player;
        Level level = player.level();

        Predicate<ItemStack> isValidAttractor = stack -> stack.getItem() instanceof MagniaAttractorItem && NebuliteToolItem.fuelExceedsCost(new NebuliteToolContext(stack, level, player));

        if (isValidAttractor.test(player.getMainHandItem())) return player.getMainHandItem();
        if (isValidAttractor.test(player.getOffhandItem())) return player.getOffhandItem();

        return inventory.items.stream().filter(isValidAttractor).findFirst().orElse(ItemStack.EMPTY);
    }

    public static void setEnabled(ItemStack stack, boolean value) {
        stack.set(ENABLED, value);
    }

    public static boolean isEnabled(ItemStack stack) {
        if (stack.has(ENABLED)) return stack.get(ENABLED);
        throw new IllegalStateException(stack.getItem() + " missing component of " + ENABLED);
    }

    public static void setEntitiesPulled(ItemStack stack, int value) {
        stack.set(ENTITIES_PULLED, value);
    }

    public static void incrementEntitiesPulled(ItemStack stack, int count) {
        stack.set(ENTITIES_PULLED, getEntitiesPulled(stack) + count);
    }

    public static int getEntitiesPulled(ItemStack stack) {
        if (stack.has(ENTITIES_PULLED)) return stack.get(ENTITIES_PULLED);
        return 0;
    }

    public static void setEntitiesPulledToUseFuel(ItemStack stack, int value) {
        stack.set(ENTITIES_PULLED_TO_USE_FUEL, value);
    }

    public static int getEntitiesPulledToUseFuel(ItemStack stack) {
        if (stack.has(ENTITIES_PULLED_TO_USE_FUEL)) return stack.get(ENTITIES_PULLED_TO_USE_FUEL);
        throw new IllegalStateException(stack.getItem() + " missing component of " + ENTITIES_PULLED_TO_USE_FUEL);
    }

    public static int getEntityPullRange(ItemStack stack) {
        if (stack.has(ENTITY_PULL_RANGE)) return stack.get(ENTITY_PULL_RANGE);
        return 0;
    }

    public static int getEntitiesPulledToUseFuel(ItemStack stack, Level level) {
        int base = getEntitiesPulledToUseFuel(stack);
        if (!(level instanceof net.minecraft.server.level.ServerLevel server)) return base;
        org.apache.commons.lang3.mutable.MutableFloat value = new org.apache.commons.lang3.mutable.MutableFloat(base);
        net.minecraft.world.item.enchantment.EnchantmentHelper.runIterationOnItem(stack, (holder, enchantmentLevel) -> holder.value().modifyItemFilteredCount(net.bunten.enderscape.registry.EnderscapeEnchantmentEffectComponents.INTEGER_COUNTER_THRESHOLD.get(), server, enchantmentLevel, stack, value));
        return (int) value.floatValue();
    }

    public static boolean shouldReduceFuel(ItemStack stack, Level level) {
        return getEntitiesPulled(stack) >= getEntitiesPulledToUseFuel(stack, level);
    }

    public static boolean canDepositIntoBundles(ItemStack stack) {
        org.apache.commons.lang3.mutable.MutableFloat value = new org.apache.commons.lang3.mutable.MutableFloat(0);
        net.minecraft.world.item.enchantment.EnchantmentHelper.runIterationOnItem(stack, (holder, enchantmentLevel) -> holder.value().modifyUnfilteredValue(net.bunten.enderscape.registry.EnderscapeEnchantmentEffectComponents.MAGNET_ENABLE_DEPOSIT_INTO_BUNDLES.get(), net.minecraft.util.RandomSource.create(), enchantmentLevel, value));
        return value.floatValue() > 0;
    }

    public static boolean tryUseFuel(NebuliteToolContext context, int count) {
        ItemStack stack = context.stack();
        LivingEntity user = context.user();

        if (MagniaAttractorItem.shouldReduceFuel(stack, user.level())) {
            NebuliteToolItem.useFuel(context);
            MagniaAttractorItem.setEntitiesPulled(stack, Math.max(0, count - MagniaAttractorItem.getEntitiesPulledToUseFuel(stack, user.level())));
            MagniaAttractorItem.setEnabled(stack, true);

            user.level().playSound(null, user.getX(), user.getY(), user.getZ(), EnderscapeItemSounds.MAGNIA_ATTRACTOR_USE_FUEL.get(), SoundSource.PLAYERS, 1, 1);
            return true;
        }

        return false;
    }
}