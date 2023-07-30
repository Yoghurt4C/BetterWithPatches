package mods.betterwithpatches.features;

import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.InvUtilsExtensions;
import mods.betterwithpatches.util.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public interface HCOres {
    static void registerHCOres() {
        long start = System.currentTimeMillis();
        Set<ItemStack> removeIn = new HashSet<>();
        Map<ItemStack, ItemStack> xpMap = new HashMap<>();
        Map<ItemStack, ItemStack> map = new HashMap<>();
        for (String oreName : OreDictionary.getOreNames()) {
            if (oreName.startsWith("ore")) {
                List<ItemStack> nuggets = InvUtilsExtensions.getMatchingSuffix(oreName, "ore", "nugget");
                if (nuggets.size() > 0) {
                    for (ItemStack ore : OreDictionary.getOres(oreName, false)) {
                        removeIn.add(ore);
                        xpMap.put(ore, nuggets.get(0));
                    }
                    List<ItemStack> dusts = InvUtilsExtensions.getMatchingSuffix(oreName, "ore", "dust");
                    if (dusts.size() > 0) {
                        for (ItemStack dust : dusts) {
                            removeIn.add(dust);
                            map.put(dust, nuggets.get(0));
                        }
                    }
                }
            }
        }
        RecipeUtils.removeSmeltingRecipesByInput(removeIn.toArray(new ItemStack[0]));
        for (Map.Entry<ItemStack, ItemStack> recipe : xpMap.entrySet()) {
            FurnaceRecipes.smelting().func_151394_a(recipe.getKey(), recipe.getValue(), 0.1f);
        }
        for (Map.Entry<ItemStack, ItemStack> recipe : map.entrySet()) {
            FurnaceRecipes.smelting().getSmeltingList().put(recipe.getKey(), recipe.getValue());
        }
        long finish = System.currentTimeMillis();
        BWPConstants.L.info("HCOres took {} seconds to register.", String.format("%.2f", (finish - start) * 0.0001f));
    }
}
