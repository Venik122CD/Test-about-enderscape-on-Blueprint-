package net.bunten.enderscape.network;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.DashJumpUserData;
import net.bunten.enderscape.registry.EnderscapeDataAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundDashJumpDataPayload(int entityId, DashJumpUserData data) implements CustomPacketPayload {
    public static final Type<ClientboundDashJumpDataPayload> TYPE = new Type<>(Enderscape.id("clientbound_dash_jump_data"));
    public static final StreamCodec<FriendlyByteBuf, ClientboundDashJumpDataPayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundDashJumpDataPayload::write, ClientboundDashJumpDataPayload::new);

    private ClientboundDashJumpDataPayload(FriendlyByteBuf buf) {
        this(buf.readInt(), DashJumpUserData.STREAM_CODEC.decode(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        DashJumpUserData.STREAM_CODEC.encode(buf, data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientboundDashJumpDataPayload payload, IPayloadContext context) {
        var entity = context.player().level().getEntity(payload.entityId);
        if (entity != null) {
            entity.setData(EnderscapeDataAttachments.DASH_JUMP_USER_DATA.get(), payload.data);
        }
    }
}
