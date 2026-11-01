package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.registry.EnderscapeDataComponents;
import net.bunten.enderscape.registry.EnderscapeEnchantmentEffectComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

                                                                                                                                   
public record ThresholdCounter(int value, int threshold) {
    public static final Codec<ThresholdCounter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("value", 0).forGetter(ThresholdCounter::value),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("threshold").forGetter(ThresholdCounter::threshold)
    ).apply(instance, ThresholdCounter::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ThresholdCounter> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ThresholdCounter::value,
            ByteBufCodecs.INT, ThresholdCounter::threshold,
            ThresholdCounter::new
    );

    public static ThresholdCounter of(int threshold) {
        return new ThresholdCounter(0, threshold);
    }

    public static boolean is(ItemStack stack) {
        return stack.has(EnderscapeDataComponents.THRESHOLD_COUNTER);
    }

    public static ThresholdCounter get(ItemStack stack) {
        return stack.get(EnderscapeDataComponents.THRESHOLD_COUNTER);
    }

    public static int value(ItemStack stack) {
        return get(stack).value();
    }

    public static int threshold(ServerLevel level, ItemStack stack) {
        return modifyThreshold(level, stack, get(stack).threshold);
    }

    public static void set(ItemStack stack, int value) {
        ThresholdCounter counter = get(stack);
        stack.set(EnderscapeDataComponents.THRESHOLD_COUNTER, new ThresholdCounter(value, counter.threshold()));
    }

    public static void increment(ItemStack stack, int count) {
        set(stack, value(stack) + count);
    }

    public static boolean pastThreshold(ServerLevel level, ItemStack stack) {
        return get(stack).value() >= threshold(level, stack);
    }

    public static int modifyThreshold(ServerLevel level, ItemStack stack, float value) {
        MutableFloat mutable = new MutableFloat(value);
        EnchantmentHelper.runIterationOnItem(stack, (holder, enchantmentLevel) -> holder.value().modifyItemFilteredCount(EnderscapeEnchantmentEffectComponents.INTEGER_COUNTER_THRESHOLD.get(), level, enchantmentLevel, stack, mutable));
        return Math.max(0, (int) mutable.floatValue());
    }

       
                                                                                                                       
                                                                                                             
       
    public static boolean useFuel(ServerLevel level, LivingEntity user, ItemStack stack) {
        if (!NebuliteToolItem.is(stack)) return false;

        boolean hasCounter = is(stack);
        if (hasCounter) increment(stack, 1);

        if (!hasCounter || pastThreshold(level, stack)) {
            NebuliteToolItem.useFuel(new NebuliteToolContext(stack, level, user));
            if (hasCounter) set(stack, 0);
            return true;
        }

        return false;
    }
}
