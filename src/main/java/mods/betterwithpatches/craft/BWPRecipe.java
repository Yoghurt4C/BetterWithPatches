package mods.betterwithpatches.craft;

import mods.betterwithpatches.data.recipe.ChanceStack;
import mods.betterwithpatches.data.recipe.RecipeOutput;
import mods.betterwithpatches.data.recipe.WeightedStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface BWPRecipe {
    RecipeOutput[] getRawOutputs();

    default List<ItemStack> getOutputs(int iterations) {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < getRawOutputs().length; i++) {
            RecipeOutput entry = this.getRawOutputs()[i];
            if (entry.chanced()) {
                for (int j = 0; j < iterations; j++) {
                    list.add(entry.getResult().copy());
                }
            } else {
                ItemStack stack = entry.getResult().copy();
                stack.stackSize = iterations;
                list.add(stack);
            }
        }
        return list;
    }

    //for use in NEI
    int getOreId();
}
