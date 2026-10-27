package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.magnia.MagniaMovingData;
import net.bunten.enderscape.registry.EnderscapeDataAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundMagniaDataPayload(int entityId, MagniaMovingData data) implements CustomPacketPayload {
    public static final Type<ClientboundMagniaDataPayload> TYPE = new Type<>(Enderscape.id("clientbound_magnia_data"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundMagniaDataPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundMagniaDataPayload::write, ClientboundMagniaDataPayload::new);

    private ClientboundMagniaDataPayload(FriendlyByteBuf buf) {
        this(buf.readInt(), MagniaMovingData.STREAM_CODEC.decode(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        MagniaMovingData.STREAM_CODEC.encode(buf, data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientboundMagniaDataPayload payload, IPayloadContext context) {
        var entity = context.player().level().getEntity(payload.entityId);
        if (entity != null) {
            entity.setData(EnderscapeDataAttachments.MAGNIA_MOVING_DATA.get(), payload.data);
        }
    }
}
