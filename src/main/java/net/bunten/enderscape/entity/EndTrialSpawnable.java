package net.bunten.enderscape.entity;

import net.bunten.enderscape.network.ClientboundEndTrialSpawnableDataPayload;
import net.bunten.enderscape.network.ClientboundMagniaDataPayload;
import net.bunten.enderscape.registry.EnderscapeDataAttachments;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public interface EndTrialSpawnable {

    static boolean is(Entity entity) {
        return entity instanceof EndTrialSpawnable;
    }

    static boolean spawnedFromEndTrialSpawner(Entity entity) {
        if (entity instanceof EndTrialSpawnable user) {
            return entity.getData(EnderscapeDataAttachments.END_TRIAL_SPAWNABLE_DATA.get()).spawnable();
        }
        return false;
    }

    static void setSpawnedFromEndTrialSpawner(Entity entity, boolean value) {
        if (is(entity)) {
            var data = new EndTrialSpawnableData(value);
            entity.setData(EnderscapeDataAttachments.END_TRIAL_SPAWNABLE_DATA.get(), data);
            if (!entity.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) entity.level();
                for (var player : serverLevel.getChunkSource().chunkMap.getPlayers(entity.chunkPosition(), false)) {
                    player.connection.send(new ClientboundEndTrialSpawnableDataPayload(entity.getId(), data));
                }
            }
        }
    }
}