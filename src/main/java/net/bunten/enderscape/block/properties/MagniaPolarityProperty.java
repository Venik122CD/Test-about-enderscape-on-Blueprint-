package net.bunten.enderscape.block.properties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.MapColor;

import java.util.Arrays;
import java.util.Optional;

public enum MagniaPolarityProperty implements StringRepresentable {
    ALLURING("alluring", MapColor.DIAMOND, MagniaType.ALLURING),
    REPULSIVE("repulsive", MapColor.TERRACOTTA_PINK, MagniaType.REPULSIVE);

    private final String name;
    private final MapColor color;
    private final MagniaType polarity;

    MagniaPolarityProperty(String name, MapColor color, MagniaType polarity) {
        this.name = name;
        this.color = color;
        this.polarity = polarity;
    }

    public static MagniaPolarityProperty of(MagniaType value) {
        return Arrays.stream(values()).filter(property -> property.polarity == value).findFirst().orElseThrow();
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public MagniaType get() {
        return polarity;
    }

    public MapColor getMapColor() {
        return color;
    }
}
