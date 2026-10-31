package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EnderscapeEnchantmentEffectComponents {

    public static final Supplier<DataComponentType<EnchantmentValueEffect>> MIRROR_DISTANCE_FOR_COST_INCREASE = register("lodestone_teleportation_distance_to_increase_cost", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
    public static final Supplier<DataComponentType<EnchantmentValueEffect>> LODESTONE_TELEPORTATION_ENABLE_TRANSDIMENSIONAL = register("lodestone_teleportation_enable_transdimensional", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
    public static final Supplier<DataComponentType<EnchantmentValueEffect>> MAGNET_ENABLE_DEPOSIT_INTO_BUNDLES = register("magnet_enable_deposit_into_bundles", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
    public static final Supplier<DataComponentType<java.util.List<net.minecraft.world.item.enchantment.ConditionalEffect<EnchantmentValueEffect>>>> INTEGER_COUNTER_THRESHOLD = register("integer_counter_threshold", builder -> builder.persistent(net.minecraft.world.item.enchantment.ConditionalEffect.codec(EnchantmentValueEffect.CODEC, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENCHANTED_ITEM).listOf()));

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return RegistryHelper.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Enderscape.id(name), () -> operator.apply(DataComponentType.builder()).build());
    }
}