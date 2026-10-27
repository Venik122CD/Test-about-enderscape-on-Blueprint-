package net.bunten.enderscape.entity;

import net.bunten.enderscape.entity.magnia.MagniaMovingData;
import net.bunten.enderscape.network.ClientboundDashJumpDataPayload;
import net.bunten.enderscape.network.ClientboundMagniaDataPayload;
import net.bunten.enderscape.registry.EnderscapeDataAttachments;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public interface DashJumpUser {

    static boolean is(Entity entity) {
        return entity instanceof DashJumpUser;
    }

    static boolean dashed(Entity entity) {
        return entity.getData(EnderscapeDataAttachments.DASH_JUMP_USER_DATA.get()).dashed();
    }

    static void setDashed(Entity entity, boolean value) {
        if (is(entity)) {
            var existing = entity.getData(EnderscapeDataAttachments.DASH_JUMP_USER_DATA.get());
            var data = new DashJumpUserData(
                    value, value ? existing.dashTicks() : 0
            );
            entity.setData(EnderscapeDataAttachments.DASH_JUMP_USER_DATA.get(), data);
            if (!entity.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) entity.level();
                for (var player : serverLevel.getChunkSource().chunkMap.getPlayers(entity.chunkPosition(), false)) {
                    player.connection.send(new ClientboundDashJumpDataPayload(entity.getId(), data));
                }
            }
        }
    }

    static int dashTicks(Entity entity) {
        return entity.getData(EnderscapeDataAttachments.DASH_JUMP_USER_DATA.get()).dashTicks();
    }

    static void setDashTicks(Entity entity, int value) {
        if (is(entity)) {
            var existing = entity.getData(EnderscapeDataAttachments.DASH_JUMP_USER_DATA.get());
            var data = new DashJumpUserData(
                    existing.dashed(), value
            );
            entity.setData(EnderscapeDataAttachments.DASH_JUMP_USER_DATA.get(), data);
            if (!entity.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) entity.level();
                for (var player : serverLevel.getChunkSource().chunkMap.getPlayers(entity.chunkPosition(), false)) {
                    player.connection.send(new ClientboundDashJumpDataPayload(entity.getId(), data));
                }
            }
        }
    }
}