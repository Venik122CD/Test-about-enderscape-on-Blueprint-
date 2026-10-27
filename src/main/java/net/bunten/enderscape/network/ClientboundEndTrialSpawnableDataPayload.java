package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.EndTrialSpawnableData;
import net.bunten.enderscape.registry.EnderscapeDataAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundEndTrialSpawnableDataPayload(int entityId, EndTrialSpawnableData data) implements CustomPacketPayload {
    public static final Type<ClientboundEndTrialSpawnableDataPayload> TYPE = new Type<>(Enderscape.id("clientbound_end_trial_spawnable_data"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundEndTrialSpawnableDataPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundEndTrialSpawnableDataPayload::write, ClientboundEndTrialSpawnableDataPayload::new);

    private ClientboundEndTrialSpawnableDataPayload(FriendlyByteBuf buf) {
        this(buf.readInt(), EndTrialSpawnableData.STREAM_CODEC.decode(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        EndTrialSpawnableData.STREAM_CODEC.encode(buf, data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientboundEndTrialSpawnableDataPayload payload, IPayloadContext context) {
        var entity = context.player().level().getEntity(payload.entityId);
        if (entity != null) {
            entity.setData(EnderscapeDataAttachments.END_TRIAL_SPAWNABLE_DATA.get(), payload.data);
        }
    }
}
