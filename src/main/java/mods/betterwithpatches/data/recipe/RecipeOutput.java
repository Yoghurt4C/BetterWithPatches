package mods.betterwithpatches.data.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface RecipeOutput {
    ItemStack getResult();
    boolean chanced();
    List<ItemStack> getDisplayStacks();
}
