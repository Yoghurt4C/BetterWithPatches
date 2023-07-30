package mods.betterwithpatches.craft.anvil;

import betterwithmods.craft.OreStack;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ShapedSteelRecipe implements IRecipe {
    public final int width, height;
    public final Object[] ingredients;
    public final ItemStack output;

    public ShapedSteelRecipe(int width, int height, Object[] ingredients, ItemStack result) {
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.output = result;
    }

    @Override
    public boolean matches(InventoryCrafting matrix, World world) {
        for (int i = 0; i <= 4 - this.width; ++i) {
            for (int j = 0; j <= 4 - this.height; ++j) {
                if (this.checkMatch(matrix, i, j, true)) {
                    return true;
                }

                if (this.checkMatch(matrix, i, j, false)) {
                    return true;
                }
            }
        }

        return false;

    }

    private boolean checkMatch(InventoryCrafting matrix, int x, int y, boolean mirrored) {
        for (int k = 0; k < 4; ++k) {
            for (int l = 0; l < 4; ++l) {
                int i1 = k - x;
                int j1 = l - y;
                Object ingr = null;

                if (i1 >= 0 && j1 >= 0 && i1 < this.width && j1 < this.width) {
                    if (mirrored) {
                        ingr = this.ingredients[this.width - i1 - 1 + j1 * this.width];
                    } else {
                        ingr = this.ingredients[i1 + j1 * this.width];
                    }
                }
                ItemStack slotStack = matrix.getStackInRowAndColumn(k, l);

                if (ingr instanceof ItemStack) {
                    ItemStack itemStack = (ItemStack) ingr;
                    if (!BWPConstants.stacksMatch(itemStack, slotStack)) return false;
                } else if (ingr instanceof OreStack) {
                    OreStack oreStack = (OreStack) ingr;
                    if (!BWPConstants.presentInOD(slotStack, oreStack.getOreName())) return false;
                } else return false;
            }
        }

        return true;
    }


    @Override
    public ItemStack getCraftingResult(InventoryCrafting matrix) {
        return this.getRecipeOutput();
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output.copy();
    }
}
