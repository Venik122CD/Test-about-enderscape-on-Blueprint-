package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeDamageTypes.STOMP;
import static net.bunten.enderscape.registry.tag.EnderscapeDamageTypeTags.RUBBLEMITES_CAN_BLOCK;
import static net.minecraft.tags.DamageTypeTags.DAMAGES_HELMET;
import static net.minecraft.tags.DamageTypeTags.NO_KNOCKBACK;
import static net.minecraft.world.damagesource.DamageTypes.*;

public class EnderscapeDamageTypeTagProvider extends TagsProvider<DamageType> {

    public EnderscapeDamageTypeTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Registries.DAMAGE_TYPE, event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(RUBBLEMITES_CAN_BLOCK).add(ARROW, EXPLOSION, FALLING_ANVIL, FALLING_BLOCK, FALLING_STALACTITE, MOB_ATTACK, MOB_ATTACK_NO_AGGRO, MOB_PROJECTILE, PLAYER_ATTACK, PLAYER_EXPLOSION, SPIT, STING, TRIDENT, UNATTRIBUTED_FIREBALL, WIND_CHARGE, WITHER_SKULL, STOMP);
        tag(DAMAGES_HELMET).add(STOMP);
        tag(NO_KNOCKBACK).add(STOMP);
    }
}