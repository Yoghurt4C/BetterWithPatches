package mods.betterwithpatches.craft.filteredhopper;

import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import mods.betterwithpatches.craft.BWPRecipe;
import mods.betterwithpatches.data.recipe.OutputStack;
import mods.betterwithpatches.data.recipe.RecipeOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class HopperRecipe implements BWPRecipe {
    final RecipeOutput[] outputs;
    final int oreId;

    public HopperRecipe(Object... outputs) {
        this(-1, outputs);
    }

    public HopperRecipe(int oreId, Object... outputs) {
        this.outputs = new RecipeOutput[outputs.length];
        this.oreId = oreId;

        for (int i = 0; i < outputs.length; i++) {
            Object o = outputs[i];
            if (o instanceof RecipeOutput) {
                this.outputs[i] = (RecipeOutput) o;
            } else if (o instanceof ItemStack) {
                this.outputs[i] = new OutputStack((ItemStack) o);
            } else if (o instanceof Item) {
                this.outputs[i] = new OutputStack((Item) o);
            } else if (o instanceof Block) {
                this.outputs[i] = new OutputStack((Block) o);
            }
        }
    }

    public HopperRecipe(String oreName, Object... outputs) {
        this(OreDictionary.getOreID(oreName), outputs);
    }

    @Override
    public RecipeOutput[] getRawOutputs() {
        return this.outputs;
    }

    @Override
    public int getOreId() {
        return this.oreId;
    }

    public boolean canCraft(World world, int x, int y, int z, TileEntityFilteredHopper tile) {
        return tile.contents[0] == null || tile.contents[17] == null;
    }

    public void onCraft(World world, int x, int y, int z, TileEntityFilteredHopper tile) {
        world.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, "random.pop", 0.8F, world.rand.nextFloat() * 0.1F + 0.45F);
    }
}
