package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.registry.EnderscapeEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class EnderscapeEntityRenderData {
    public static final ModelLayerLocation DRIFTER = makeModelLayer(EnderscapeEntities.DRIFTER);
    public static final ModelLayerLocation DRIFTLET = makeModelLayer(EnderscapeEntities.DRIFTLET);
    public static final ModelLayerLocation RUBBLEMITE = makeModelLayer(EnderscapeEntities.RUBBLEMITE);
    public static final ModelLayerLocation RUSTLE = makeModelLayer(EnderscapeEntities.RUSTLE);

    private static ModelLayerLocation makeModelLayer(DeferredHolder<EntityType<?>, ?> type) {
        return new ModelLayerLocation(type.getId(), type.getId().getPath());
    }
}