package mods.betterwithpatches.data.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class OutputStack implements RecipeOutput {
    public final ItemStack stack;

    public OutputStack(Block block) {
        this(new ItemStack(block));
    }

    public OutputStack(Item item) {
        this(new ItemStack(item));
    }

    public OutputStack(ItemStack output) {
        this.stack = output;
    }

    @Override
    public ItemStack getResult() {
        return this.stack;
    }

    @Override
    public List<ItemStack> getDisplayStacks() {
        return Collections.singletonList(this.stack);
    }

    @Override
    public boolean chanced() {
        return false;
    }
}
