package net.bunten.enderscape.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record EndTrialSpawnableData(boolean spawnable) {
    public static final Codec<EndTrialSpawnableData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.fieldOf("spawnable").forGetter(EndTrialSpawnableData::spawnable)
    ).apply(i, EndTrialSpawnableData::new));
    public static final StreamCodec<FriendlyByteBuf, EndTrialSpawnableData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            EndTrialSpawnableData::spawnable,
            EndTrialSpawnableData::new
    );
}
