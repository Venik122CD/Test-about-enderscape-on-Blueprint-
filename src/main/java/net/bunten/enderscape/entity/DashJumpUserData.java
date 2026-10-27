package net.bunten.enderscape.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DashJumpUserData(boolean dashed, int dashTicks) {
    public static final Codec<DashJumpUserData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.fieldOf("dashed").forGetter(DashJumpUserData::dashed),
            Codec.INT.fieldOf("dash_ticks").forGetter(DashJumpUserData::dashTicks)
    ).apply(i, DashJumpUserData::new));
    public static final StreamCodec<FriendlyByteBuf, DashJumpUserData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            DashJumpUserData::dashed,
            ByteBufCodecs.VAR_INT,
            DashJumpUserData::dashTicks,
            DashJumpUserData::new
    );
}
