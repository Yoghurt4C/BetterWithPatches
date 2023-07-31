package mods.betterwithpatches.craft.anvil;

import betterwithmods.craft.OreStack;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.List;

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

    private boolean checkMatch(InventoryCrafting matrix, int startX, int startY, boolean mirrored) {
        for (int k = 0; k < 4; ++k) {
            for (int l = 0; l < 4; ++l) {
                int subX = k - startX;
                int subY = l - startY;
                Object ingr = null;

                if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
                    if (mirrored) {
                        ingr = this.ingredients[this.width - subX - 1 + subY * this.width];
                    } else {
                        ingr = this.ingredients[subX + subY * this.width];
                    }
                }
                ItemStack slotStack = matrix.getStackInRowAndColumn(k, l);

                if (ingr instanceof ItemStack) {
                    ItemStack itemStack = (ItemStack) ingr;
                    if (!BWPUtils.stacksMatch(itemStack, slotStack)) return false;
                } else if (ingr instanceof OreStack) {
                    OreStack oreStack = (OreStack) ingr;
                    if (!BWPUtils.presentInOD(slotStack, oreStack.getOreName())) return false;
                } else if (ingr == null && slotStack != null) return false;
            }
        }

        return true;
    }

    public static boolean matchVanillaRecipe(InventoryCrafting matrix, int width, int height, List<?> ingredients) {
        for (int i = 0; i <= 4 - width; ++i) {
            for (int j = 0; j <= 4 - height; ++j) {
                if (checkVanillaMatch(matrix, i, j, width, height, ingredients, true)) {
                    return true;
                }

                if (checkVanillaMatch(matrix, i, j, width, height, ingredients, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private static boolean checkVanillaMatch(InventoryCrafting matrix, int startX, int startY, int width, int height, List<?> ingredients, boolean mirrored) {
        for (int k = 0; k < 4; ++k) {
            for (int l = 0; l < 4; ++l) {
                int subX = k - startX;
                int subY = l - startY;
                Object ingr = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirrored) {
                        ingr = ingredients.get(width - subX - 1 + subY * width);
                    } else {
                        ingr = ingredients.get(subX + subY * width);
                    }
                }
                ItemStack slotStack = matrix.getStackInRowAndColumn(k, l);

                if (ingr instanceof ItemStack) {
                    ItemStack itemStack = (ItemStack) ingr;
                    if (!BWPUtils.stacksMatch(itemStack, slotStack)) return false;
                } else if (ingr instanceof List) {
                    List<ItemStack> list = (List<ItemStack>) ingr;
                    boolean unmatched = true;
                    for (ItemStack itemStack : list) {
                        if (BWPUtils.stacksMatch(slotStack, itemStack)) unmatched = false;
                    }
                    if (unmatched) return false;
                } else if (ingr == null && slotStack != null) return false;
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
