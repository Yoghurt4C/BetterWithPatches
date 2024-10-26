package mods.betterwithpatches.data.recipe;

import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WeightedStack implements RecipeOutput {
    public final ChanceStack[] stacks;

    /**
     * @param obj one or more inputs that will be converted into {@link ChanceStack}
     *            While ChanceStack on its own does a single roll for a single item using {@link ChanceStack#chance},
     *            this class uses those values to randomly compute a single item from a collection of ChanceStacks.
     */
    public WeightedStack(Object... obj) {
        this.stacks = new ChanceStack[obj.length];
        float avgWeight = 1f / obj.length;
        for (int i = 0; i < obj.length; i++) {
            Object o = obj[i];
            if (o instanceof ChanceStack) {
                this.stacks[i] = (ChanceStack) o;
            } else if (o instanceof ItemStack) {
                this.stacks[i] = new ChanceStack((ItemStack) o, avgWeight);
            } else if (o instanceof Item) {
                this.stacks[i] = new ChanceStack(new ItemStack((Item) o), avgWeight);
            } else if (o instanceof Block) {
                this.stacks[i] = new ChanceStack(new ItemStack((Block) o), avgWeight);
            }
        }
    }

    @Override
    public List<ItemStack> getDisplayStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (ChanceStack stack : this.stacks) {
            stacks.add(stack.stack);
        }
        return stacks;
    }

    @Override
    public ItemStack getResult() {
        ItemStack result = null;
        double bestValue = Double.MAX_VALUE;
        for (ChanceStack element : stacks) {
            double value = -Math.log(BWPConstants.RANDOM.nextDouble()) / element.chance;
            if (value < bestValue) {
                bestValue = value;
                result = element.stack;
            }
        }
        return result;

    }

    @Override
    public boolean chanced() {
        return true;
    }
}
