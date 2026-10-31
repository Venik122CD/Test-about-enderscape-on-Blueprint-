package net.bunten.enderscape.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.MapColor;

import java.util.Arrays;
import java.util.Optional;

public enum OptionalMagniaPolarityProperty implements StringRepresentable {
    NONE("none", MapColor.SNOW, Optional.empty()),
    ALLURING("alluring", MapColor.DIAMOND, Optional.of(MagniaType.ALLURING)),
    REPULSIVE("repulsive", MapColor.TERRACOTTA_PINK, Optional.of(MagniaType.REPULSIVE));

    private final String name;
    private final MapColor color;
    private final Optional<MagniaType> polarity;

    OptionalMagniaPolarityProperty(String name, MapColor color, Optional<MagniaType> polarity) {
        this.name = name;
        this.color = color;
        this.polarity = polarity;
    }

    public static OptionalMagniaPolarityProperty of(MagniaType value) {
        return Arrays.stream(values()).filter(property -> property.polarity.filter(type -> type == value).isPresent()).findFirst().orElse(NONE);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Optional<MagniaType> optional() {
        return polarity;
    }

    public MapColor getMapColor() {
        return color;
    }
}
