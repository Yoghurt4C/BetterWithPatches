package mods.betterwithpatches.data.recipe;

import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ChanceStack extends OutputStack {
    public final float chance;

    public ChanceStack(Block block, float chance) {
        this(new ItemStack(block), chance);
    }

    public ChanceStack(Item item, float chance) {
        this(new ItemStack(item), chance);
    }

    public ChanceStack(ItemStack stack, float chance) {
        super(stack);
        this.chance = chance;
    }

    @Override
    public ItemStack getResult() {
        return BWPConstants.RANDOM.nextFloat() > this.chance ? stack : null;
    }

    @Override
    public boolean chanced() {
        return true;
    }
}
