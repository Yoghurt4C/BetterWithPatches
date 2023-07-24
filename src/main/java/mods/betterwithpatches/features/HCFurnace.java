package mods.betterwithpatches.features;

import mods.betterwithpatches.Config;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public interface HCFurnace {
    Hashtable<ItemStack, Integer> COOKING_TIMINGS = new Hashtable<>();

    static int getCookingTime(ItemStack input) {
        for (Map.Entry<ItemStack, Integer> e : COOKING_TIMINGS.entrySet()) {
            if (BWPConstants.areStacksSameTypeCrafting(e.getKey(), input)) return e.getValue();
        }
        return Config.hcFurnaceDefaultCookTime;
    }

    static void overrideCookingTime(ItemStack stack, int ticks) {
        COOKING_TIMINGS.put(stack, ticks);
    }

    static void overrideCookingTime(String od, int ticks) {
        List<ItemStack> ores = OreDictionary.getOres(od, false);
        for (ItemStack ore : ores) {
            overrideCookingTime(ore, ticks);
        }
    }

    static void removeOverride(String od) {
        List<ItemStack> ores = OreDictionary.getOres(od, false);
        for (ItemStack ore : ores) {
            removeOverride(ore);
        }
    }

    static void removeOverride(ItemStack stack) {
        COOKING_TIMINGS.remove(stack);
    }
}
