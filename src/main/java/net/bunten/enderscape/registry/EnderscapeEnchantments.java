package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderscapeEnchantments {

    public static final List<ResourceKey<Enchantment>> ENCHANTMENTS = new ArrayList<>();

    public static final ResourceKey<Enchantment> BUNDLING = register("bundling");
    public static final ResourceKey<Enchantment> RESONANCE = register("resonance");
    public static final ResourceKey<Enchantment> REBOUND = register("rebound");
    public static final ResourceKey<Enchantment> TRANSDIMENSIONAL = register("transdimensional");

    public void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(context, BUNDLING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(net.minecraft.tags.ItemTags.create(Enderscape.id("enchantable/magnia_attractor"))), 4, 1, Enchantment.constantCost(30), Enchantment.constantCost(50), 8, EquipmentSlotGroup.ANY
                        )
                ).withSpecialEffect(EnderscapeEnchantmentEffectComponents.MAGNET_ENABLE_DEPOSIT_INTO_BUNDLES.get(), new net.minecraft.world.item.enchantment.effects.SetValue(LevelBasedValue.constant(1.0F)))
        );

        register(context, RESONANCE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(net.minecraft.tags.ItemTags.create(Enderscape.id("enchantable/nebulite_tool"))), 10, 3, Enchantment.dynamicCost(1, 15), Enchantment.dynamicCost(20, 40), 2, EquipmentSlotGroup.ANY
                        )
                ).withSpecialEffect(EnderscapeEnchantmentEffectComponents.MIRROR_DISTANCE_FOR_COST_INCREASE.get(), new AddValue(LevelBasedValue.perLevel(250)))
        );

        register(context, REBOUND,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.ELYTRA_ENCHANTABLE), 1, 1, Enchantment.constantCost(30), Enchantment.constantCost(80), 8, EquipmentSlotGroup.CHEST
                        )
                )
        );

        register(context, TRANSDIMENSIONAL,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.MIRROR_ENCHANTABLE), 1, 1, Enchantment.constantCost(30), Enchantment.constantCost(80), 8, EquipmentSlotGroup.HAND
                        )
                ).withSpecialEffect(EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_ENABLE_TRANSDIMENSIONAL.get(), new net.minecraft.world.item.enchantment.effects.SetValue(LevelBasedValue.constant(1.0F)))
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    private static ResourceKey<Enchantment> register(String name) {
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, Enderscape.id(name));
        ENCHANTMENTS.add(key);
        return key;
    }

    public static boolean hasRebound(Level level, ItemStack stack) {
        try {
            HolderLookup.RegistryLookup<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Optional<Holder.Reference<Enchantment>> enchantment = registry.get(EnderscapeEnchantments.REBOUND);
            return EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), stack) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}