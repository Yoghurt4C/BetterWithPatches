package mods.betterwithpatches.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

import java.util.*;

public interface RecipeUtils {
    static void removeRecipes(Object... objects) {
        List<ItemStack> list = new ArrayList<>();
        for (Object obj : objects) {
            if (obj instanceof Item) list.add(new ItemStack((Item) obj));
            else if (obj instanceof Block) list.add(new ItemStack((Block) obj));
        }
        removeMatchingRecipes(list.toArray(new ItemStack[0]));
    }

    static void removeMatchingRecipes(ItemStack... stacks) {
        ListIterator<IRecipe> recipes = CraftingManager.getInstance().getRecipeList().listIterator();
        while (recipes.hasNext()) {
            IRecipe recipe = recipes.next();
            for (ItemStack stack : stacks) {
                if (BWPUtils.stacksMatch(recipe.getRecipeOutput(), stack)) {
                    recipes.remove();
                }
            }
        }
    }

    static void removeSmeltingRecipesByInput(ItemStack... stacks) {
        Iterator<Map.Entry<ItemStack, ItemStack>> iter = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<ItemStack, ItemStack> entry = iter.next();
            for (ItemStack stack : stacks) {
                if (BWPUtils.stacksMatch(entry.getKey(), stack)) {
                    iter.remove();
                }
            }
        }
    }

    static void removeSmeltingRecipesByOutput(ItemStack... stacks) {
        Iterator<Map.Entry<ItemStack, ItemStack>> iter = FurnaceRecipes.smelting().getSmeltingList().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<ItemStack, ItemStack> entry = iter.next();
            for (ItemStack stack : stacks) {
                if (BWPUtils.stacksMatch(entry.getValue(), stack)) {
                    iter.remove();
                }
            }
        }
    }
}
