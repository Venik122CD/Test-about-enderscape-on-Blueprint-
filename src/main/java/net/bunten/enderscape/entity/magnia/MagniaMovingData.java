package net.bunten.enderscape.entity.magnia;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.network.ClientboundMagniaDataPayload;
import net.bunten.enderscape.registry.EnderscapeDataAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public record MagniaMovingData(long targetTick) {
    public static final Codec<MagniaMovingData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.LONG.fieldOf("target_tick").forGetter(MagniaMovingData::targetTick)
    ).apply(i, MagniaMovingData::new));
    public static final StreamCodec<FriendlyByteBuf, MagniaMovingData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            MagniaMovingData::targetTick,
            MagniaMovingData::new
    );

    public static boolean wasMovedByMagnia(Entity entity) {
        return entity.getData(EnderscapeDataAttachments.MAGNIA_MOVING_DATA.get()).targetTick > entity.level().getGameTime();
    }

    public static void setMovedByMagnia(Entity entity, boolean value) {
        if (entity instanceof MagniaMoveable) {
            var data = new MagniaMovingData(
                    entity.level().getGameTime() + (value ? 20 : 0)
            );
            entity.setData(EnderscapeDataAttachments.MAGNIA_MOVING_DATA.get(), data);
            if (!entity.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) entity.level();
                for (var player : serverLevel.getChunkSource().chunkMap.getPlayers(entity.chunkPosition(), false)) {
                    player.connection.send(new ClientboundMagniaDataPayload(entity.getId(), data));
                }
            }
        }
    }
}
