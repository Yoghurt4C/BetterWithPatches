package mods.betterwithpatches.craft.anvil;

import betterwithmods.craft.OreStack;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.ListIterator;

public class ShapelessSteelRecipe implements IRecipe {
    public final ItemStack output;
    /**
     * Is a List of ItemStack that composes the recipe.
     */
    public final Object[] ingredients;

    public ShapelessSteelRecipe(ItemStack result, Object... ingredients) {
        this.output = result;
        this.ingredients = new Object[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            Object in = ingredients[i];
            if (in instanceof ItemStack) {
                this.ingredients[i] = ((ItemStack) in).copy();
            } else if (in instanceof Item) {
                this.ingredients[i] = new ItemStack((Item) in);
            } else if (in instanceof Block) {
                this.ingredients[i] = new ItemStack((Block) in);
            } else if (in instanceof String) {
                this.ingredients[i] = new OreStack((String) in);
            } else {
                StringBuilder ret = new StringBuilder("Invalid shapeless ore recipe: ");
                for (Object tmp : ingredients) {
                    ret.append(tmp).append(", ");
                }
                ret.append(output);
                throw new RuntimeException(ret.toString());
            }
        }
    }

    public ItemStack getRecipeOutput() {
        return this.output.copy();
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting matrix, World world) {
        List<Object> stacks = Arrays.asList(this.ingredients);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                ItemStack slotStack = matrix.getStackInRowAndColumn(j, i);

                if (slotStack != null) {
                    boolean flag = true;
                    ListIterator<Object> iter = stacks.listIterator();
                    while (iter.hasNext()) {
                        Object o = iter.next();
                        if (o instanceof ItemStack) {
                            ItemStack itemStack = (ItemStack) o;
                            if (slotStack.getItem() == itemStack.getItem() && (itemStack.getItemDamage() == 32767 || slotStack.getItemDamage() == itemStack.getItemDamage())) {
                                flag = false;
                                iter.remove();
                                break;
                            }
                        } else if (o instanceof OreStack) {
                            OreStack ore = (OreStack) o;
                            if (BWPUtils.presentInOD(slotStack, ore.getOreName())) {
                                flag = false;
                                iter.remove();
                                break;
                            }
                        }
                    }

                    if (flag) {
                        return false;
                    }
                }
            }
        }

        return stacks.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting matrix) {
        return this.output;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return this.ingredients.length;
    }

}
