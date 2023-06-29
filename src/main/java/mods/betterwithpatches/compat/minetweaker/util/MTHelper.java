package mods.betterwithpatches.compat.minetweaker.util;

import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.OreStack;
import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.mc1710.item.MCItemStack;
import minetweaker.mc1710.oredict.MCOreDictEntry;
import mods.betterwithpatches.util.BWMRecipeAccessor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface MTHelper {
    static Object toBWMStack(IIngredient ingr) {
        if (ingr instanceof IOreDictEntry) {
            IOreDictEntry od = (IOreDictEntry) ingr;
            return new OreStack(od.getName(), od.getAmount());
        } else {
            return MineTweakerMC.getItemStack(ingr);
        }
    }

    static Object[] toBWMStacks(IIngredient[] ingr) {
        Object[] obj = new Object[ingr.length];
        for (int i = 0; i < ingr.length; i++) {
            obj[i] = toBWMStack(ingr[i]);
        }
        return obj;
    }

    static IIngredient toMTIngredient(Object obj) {
        if (obj instanceof OreStack) {
            return new MCOreDictEntry(((OreStack) obj).getOreName()).amount(((OreStack) obj).getStackSize());
        } else {
            return new MCItemStack((ItemStack) obj);
        }
    }

    static IIngredient[] toMTIngredients(Object[] obj) {
        IIngredient[] ingr = new IIngredient[obj.length];
        for (int i = 0; i < obj.length; i++) {
            ingr[i] = toMTIngredient(obj[i]);
        }
        return ingr;
    }

    static boolean stacksMatch(ItemStack stack1, ItemStack stack2) {
        return stack1 != null && stack2 != null &&
                stack1.getItem() == stack2.getItem() &&
                (stack1.getItemDamage() == stack2.getItemDamage() || stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack1.getItem().isDamageable());
    }

    static List<BulkRecipe> copyBulkRecipeList(CraftingManagerBulk instance) {
        return new ArrayList<>(((BWMRecipeAccessor) instance).getRecipes());
    }

    static String addRecipeDescription(String handlerName, Object[] inputs, Object[] outputs) {
        return String.format("[BWP] Adding %s recipe: %s -> %s", handlerName, Arrays.toString(inputs), Arrays.toString(outputs));
    }

    static String addRecipeDescription(String handlerName, Object input, Object[] outputs) {
        return String.format("[BWP] Adding %s recipe: [%s] -> %s", handlerName, input, Arrays.toString(outputs));
    }

    static String removeRecipeDescription(String handlerName, Object[] inputs, Object[] outputs) {
        return String.format("[BWP] Removing %s recipe: %s -> %s", handlerName, Arrays.toString(inputs), Arrays.toString(outputs));
    }

    static String removeRecipeDescription(String handlerName, Object input, Object[] outputs) {
        return String.format("[BWP] Removing %s recipe: [%s] -> %s", handlerName, input, Arrays.toString(outputs));
    }
}
