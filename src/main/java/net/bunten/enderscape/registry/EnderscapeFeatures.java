package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.feature.CeilingOreConfig;
import net.bunten.enderscape.feature.CeilingOreFeature;
import net.bunten.enderscape.feature.EnderscapeIslandFeature;
import net.bunten.enderscape.feature.GrowthConfig;
import net.bunten.enderscape.feature.GrowthFeature;
import net.bunten.enderscape.feature.LargeCelestialChanterelleConfig;
import net.bunten.enderscape.feature.LargeCelestialChanterelleFeature;
import net.bunten.enderscape.feature.LargeMurublightChanterelleConfig;
import net.bunten.enderscape.feature.LargeMurublightChanterelleFeature;
import net.bunten.enderscape.feature.MagniaArchFeature;
import net.bunten.enderscape.feature.MagniaTowerConfig;
import net.bunten.enderscape.feature.MagniaTowerFeature;
import net.bunten.enderscape.feature.MurublightShelfFeature;
import net.bunten.enderscape.feature.ScatteredOreConfig;
import net.bunten.enderscape.feature.ScatteredOreFeature;
import net.bunten.enderscape.feature.VeiledLeafPileConfig;
import net.bunten.enderscape.feature.VeiledLeafPileFeature;
import net.bunten.enderscape.feature.VeiledTreeConfig;
import net.bunten.enderscape.feature.VeiledTreeFeature;
import net.bunten.enderscape.feature.VoidShaleConfig;
import net.bunten.enderscape.feature.VoidShaleFeature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Supplier;

public abstract class EnderscapeFeatures {

    public static final Supplier<Feature<CeilingOreConfig>> CEILING_ORE = register("ceiling_ore", () -> new CeilingOreFeature(CeilingOreConfig.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> ENDERSCAPE_ISLAND = register("enderscape_island", () -> new EnderscapeIslandFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<GrowthConfig>> GROWTH = register("growth", () -> new GrowthFeature(GrowthConfig.CODEC));
    public static final Supplier<Feature<LargeCelestialChanterelleConfig>> LARGE_CELESTIAL_CHANTERELLE = register("large_celestial_chanterelle", () -> new LargeCelestialChanterelleFeature(LargeCelestialChanterelleConfig.CODEC));
    public static final Supplier<Feature<LargeMurublightChanterelleConfig>> LARGE_MURUBLIGHT_CHANTERELLE = register("large_murublight_chanterelle", () -> new LargeMurublightChanterelleFeature(LargeMurublightChanterelleConfig.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> MURUBLIGHT_SHELF = register("murublight_shelf", () -> new MurublightShelfFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> MAGNIA_ARCH = register("magnia_arch", () -> new MagniaArchFeature());
    public static final Supplier<Feature<MagniaTowerConfig>> MAGNIA_TOWER = register("magnia_tower", () -> new MagniaTowerFeature(MagniaTowerConfig.CODEC));
    public static final Supplier<Feature<ScatteredOreConfig>> SCATTERED_ORE = register("scattered_ore", () -> new ScatteredOreFeature(ScatteredOreConfig.CODEC));
    public static final Supplier<Feature<VeiledLeafPileConfig>> VEILED_LEAF_PILE = register("veiled_leaf_pile", () -> new VeiledLeafPileFeature(VeiledLeafPileConfig.CODEC));
    public static final Supplier<Feature<VeiledTreeConfig>> VEILED_TREE = register("veiled_tree", () -> new VeiledTreeFeature(VeiledTreeConfig.CODEC));
    public static final Supplier<Feature<VoidShaleConfig>> VOID_SHALE = register("void_shale", () -> new VoidShaleFeature(VoidShaleConfig.CODEC));

    private static <T extends Feature<?>> Supplier<T> register(String name, Supplier<T> entry) {
        return RegistryHelper.register(BuiltInRegistries.FEATURE, Enderscape.id(name), entry);
    }
}