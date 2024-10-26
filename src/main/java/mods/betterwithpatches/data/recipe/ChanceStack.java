package mods.betterwithpatches.data.recipe;

import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ChanceStack extends OutputStack {
    public final float chance;

    public ChanceStack(Block block, float chance) {
        this(new ItemStack(block), chance);
    }

    public ChanceStack(Item item, float chance) {
        this(new ItemStack(item), chance);
    }

    /**
     * @param stack  produced by this recipe output
     * @param chance to successfully produce the aforementioned stack, just pretend it's on some sort of relative scale
     * @see OutputStack for a guaranteed single item output;
     * @see WeightedStack for a single output plucked from a pool of items
     */
    public ChanceStack(ItemStack stack, float chance) {
        super(stack);
        this.chance = MathHelper.clamp_float(chance, 0, 1);
    }

    @Override
    public ItemStack getResult() {
        return BWPConstants.RANDOM.nextFloat() < this.chance ? stack : null;
    }

    @Override
    public boolean chanced() {
        return true;
    }
}
