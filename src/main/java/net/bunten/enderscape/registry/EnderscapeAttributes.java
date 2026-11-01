package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class EnderscapeAttributes {
    public static final int DEFAULT_BACKSTAB_ANGLE = 110;
    public static final ResourceLocation BASE_BACKSTAB_DAMAGE_ID = Enderscape.id("base_backstab_damage");

    public static final Holder<Attribute> BACKSTAB_DAMAGE = register("backstab_damage", new RangedAttribute("attribute.enderscape.backstab_damage", 0, 0, 2048).setSyncable(true));
    public static final Holder<Attribute> STEALTH = register("stealth", new RangedAttribute("attribute.enderscape.stealth", 1, 0, 2).setSyncable(true));

    private static Holder<Attribute> register(String name, Attribute attribute) {
        return RegistryHelper.registerForHolder(BuiltInRegistries.ATTRIBUTE, Enderscape.id(name), () -> attribute);
    }

    public static double getBackstabDamage(Entity entity) {
        if (entity instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(BACKSTAB_DAMAGE);
            if (instance != null) return instance.getValue();
        }
        return 1.0;
    }

    public static double getStealthValue(Entity entity) {
        if (entity instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(STEALTH);
            if (instance != null) return instance.getValue();
        }
        return 1.0;
    }

    /** Multiplier applied to how visible the entity is to mobs: more stealth means less visible. */
    public static double getStealthMultiplier(Entity entity) {
        return 2 - getStealthValue(entity);
    }

    /** True when the attacker stands behind the victim (the victim looks more than {@code angle} degrees away from the attacker). */
    public static boolean isBackstab(net.minecraft.world.item.ItemStack stack, net.minecraft.world.phys.Vec3 sourcePos, Entity victim) {
        int angle = stack != null && !stack.isEmpty() ? stack.getOrDefault(EnderscapeDataComponents.BACKSTAB_ANGLE, DEFAULT_BACKSTAB_ANGLE) : DEFAULT_BACKSTAB_ANGLE;
        net.minecraft.world.phys.Vec3 offset = sourcePos.subtract(victim.position());
        net.minecraft.world.phys.Vec3 direction = new net.minecraft.world.phys.Vec3(offset.x, 0, offset.z).normalize();
        net.minecraft.world.phys.Vec3 facing = victim.calculateViewVector(0.0F, victim.getYHeadRot()).normalize();
        return Math.acos(direction.dot(facing)) > Math.toRadians(angle);
    }
}
