package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeEntities.*;
import static net.bunten.enderscape.registry.tag.EnderscapeEntityTags.*;
import static net.minecraft.world.entity.EntityType.*;

public class EnderscapeEntityTagProvider extends EntityTypeTagsProvider {

    public EnderscapeEntityTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(AFFECTED_BY_MAGNIA).add(IRON_GOLEM, MINECART);
        tag(BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION).add(ELDER_GUARDIAN, ENDER_DRAGON, GHAST, RAVAGER, WARDEN, WITHER);
        tag(CREATES_VOID_PARTICLES_UPON_DEATH).add(ENDERMAN, ENDERMITE, RUBBLEMITE.get());
        tag(DRIFTERS).add(DRIFTER.get(), DRIFTLET.get());
        tag(DRIFTERS_INTIMIDATED_BY).add(RUBBLEMITE.get(), SLIME);
        tag(RUBBLEMITE_HOSTILE_TOWARDS).add(IRON_GOLEM);

        tag(EntityTypeTags.ARTHROPOD).add(RUBBLEMITE.get());
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).addTag(DRIFTERS);
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(RUBBLEMITE.get());
    }
}
