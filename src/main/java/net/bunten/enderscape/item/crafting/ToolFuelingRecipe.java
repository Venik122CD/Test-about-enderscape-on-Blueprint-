package net.bunten.enderscape.item.crafting;

import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/** Refuels a nebulite tool (mirror, magnia attractor) in the crafting grid with nebulite. */
public class ToolFuelingRecipe extends CustomRecipe {
    public ToolFuelingRecipe(CraftingBookCategory category) {
        super(category);
    }

    private static int findTool(CraftingInput input) {
        for (int i = 0; i < input.size(); i++) {
            if (NebuliteToolItem.is(input.getItem(i))) return i;
        }
        return -1;
    }

    private static int countFuel(CraftingInput input, int toolIndex) {
        int fuel = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty() || i == toolIndex) continue;
            if (!stack.is(EnderscapeItems.NEBULITE.get())) return -1;
            fuel++;
        }
        return fuel;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int toolIndex = findTool(input);
        if (toolIndex < 0) return false;
        int fuel = countFuel(input, toolIndex);
        if (fuel <= 0) return false;
        ItemStack tool = input.getItem(toolIndex);
        int missing = NebuliteToolItem.maxFuel(tool) - NebuliteToolItem.currentFuel(tool);
        return missing > 0 && fuel <= missing;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        int toolIndex = findTool(input);
        if (toolIndex < 0) return ItemStack.EMPTY;
        int fuel = countFuel(input, toolIndex);
        if (fuel <= 0) return ItemStack.EMPTY;
        ItemStack tool = input.getItem(toolIndex).copy();
        int added = Math.min(fuel, NebuliteToolItem.maxFuel(tool) - NebuliteToolItem.currentFuel(tool));
        NebuliteToolItem.setFuel(tool, NebuliteToolItem.currentFuel(tool) + added);
        return tool;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<ToolFuelingRecipe> getSerializer() {
        return EnderscapeRecipeSerializers.TOOL_FUELING.get();
    }
}
