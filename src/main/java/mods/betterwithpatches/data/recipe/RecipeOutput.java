package mods.betterwithpatches.data.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface RecipeOutput {
    /**
     * @return the actual output stack of this RecipeOutput.
     */
    ItemStack getResult();

    /**
     * @return whether this RecipeOutput is guaranteed to produce one item, essentially a flag for expensive stacksize computations.
     */
    boolean chanced();

    /**
     * @return all possible permutations of this RecipeOutput, for use specifically in things like NEI handlers.
     */
    List<ItemStack> getDisplayStacks();
}
