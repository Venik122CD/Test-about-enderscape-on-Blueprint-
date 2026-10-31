package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.crafting.ToolFuelingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class EnderscapeRecipeSerializers {
    public static final Supplier<RecipeSerializer<ToolFuelingRecipe>> TOOL_FUELING = RegistryHelper.register(BuiltInRegistries.RECIPE_SERIALIZER, Enderscape.id("tool_fueling"), () -> new SimpleCraftingRecipeSerializer<>(ToolFuelingRecipe::new));
}
