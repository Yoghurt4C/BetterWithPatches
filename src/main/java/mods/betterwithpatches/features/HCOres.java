package mods.betterwithpatches.features;

import betterwithmods.BWRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.InvUtilsExtensions;
import mods.betterwithpatches.util.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;

public interface HCOres {
    static void registerHCOres() {
        long start = System.currentTimeMillis();
        Set<ItemStack> toRemove = new HashSet<>();
        Map<ItemStack, ItemStack> xpMap = new HashMap<>();
        Map<ItemStack, ItemStack> map = new HashMap<>();
        for (String oreName : OreDictionary.getOreNames()) {
            if (oreName.startsWith("ore")) {
                List<ItemStack> nuggets = InvUtilsExtensions.getMatchingSuffix(oreName, "ore", "nugget");
                if (nuggets.size() > 0) {
                    for (ItemStack ore : OreDictionary.getOres(oreName, false)) {
                        toRemove.add(ore);
                        xpMap.put(ore, nuggets.get(0));
                    }
                    if (Config.hcOreDusts) {
                        List<ItemStack> dusts = InvUtilsExtensions.getMatchingSuffix(oreName, "ore", "dust");
                        if (dusts.size() > 0) {
                            for (ItemStack dust : dusts) {
                                toRemove.add(dust);
                                map.put(dust, nuggets.get(0));
                            }
                        }
                    }
                }
            }
        }
        RecipeUtils.removeSmeltingRecipesByInput(toRemove.toArray(new ItemStack[0]));
        for (Map.Entry<ItemStack, ItemStack> recipe : xpMap.entrySet()) {
            FurnaceRecipes.smelting().func_151394_a(recipe.getKey(), recipe.getValue(), 0.1f);
        }
        for (Map.Entry<ItemStack, ItemStack> recipe : map.entrySet()) {
            FurnaceRecipes.smelting().getSmeltingList().put(recipe.getKey(), recipe.getValue());
        }

        toRemove.clear();
        xpMap.clear();
        map.clear();

        if (OreDictionary.getOres("nuggetIron", false).size() == 1) {
            GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "NNN", "NNN", "NNN", 'N', new ItemStack(BWRegistry.material, 1, 30));
            GameRegistry.addShapelessRecipe(new ItemStack(BWRegistry.material, 9, 30), Items.iron_ingot);
        }
        RecipeUtils.removeRecipes(Items.compass, Items.clock, Items.bucket, Items.flint_and_steel);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.compass), " N ", "NRN", " N ", 'N', "nuggetIron", 'R', "dustRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.clock), " N ", "NQN", " N ", 'N', "nuggetGold", 'Q', "gemQuartz"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.bucket), "N N", " N ", 'N', "nuggetIron"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.flint_and_steel), "nuggetIron", Items.flint));

        long finish = System.currentTimeMillis();
        BWPConstants.L.info("HCOres took {} seconds to register.", String.format("%.2f", (finish - start) * 0.0001f));
    }
}
