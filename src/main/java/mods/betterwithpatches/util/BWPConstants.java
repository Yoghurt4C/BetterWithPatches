package mods.betterwithpatches.util;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface BWPConstants {
    String MODID = "betterwithpatches", MODNAME = "BetterWithPatches";
    Logger L = LogManager.getLogger(MODNAME);

    static String getId(Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }

    static boolean presentInOD(ItemStack stack, String od) {
        return ArrayUtils.contains(OreDictionary.getOreIDs(stack), OreDictionary.getOreID(od));
    }
}
