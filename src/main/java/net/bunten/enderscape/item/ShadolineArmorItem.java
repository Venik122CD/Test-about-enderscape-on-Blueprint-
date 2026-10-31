package net.bunten.enderscape.item;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/** Shadoline armor: normal armor stats plus a stealth bonus that makes the wearer harder for mobs to notice. */
public class ShadolineArmorItem extends ArmorItem {
    private ItemAttributeModifiers cachedModifiers;

    public ShadolineArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    private static double stealthBonus(Type type) {
        return switch (type) {
            case BOOTS, HELMET -> 0.15;
            case LEGGINGS, CHESTPLATE -> 0.2;
            case BODY -> 0.4;
        };
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        if (cachedModifiers == null) {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            for (ItemAttributeModifiers.Entry entry : super.getDefaultAttributeModifiers().modifiers()) {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
            builder.add(EnderscapeAttributes.STEALTH, new AttributeModifier(Enderscape.id("armor." + getType().getName()), stealthBonus(getType()), AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.bySlot(getType().getSlot()));
            cachedModifiers = builder.build();
        }
        return cachedModifiers;
    }
}
