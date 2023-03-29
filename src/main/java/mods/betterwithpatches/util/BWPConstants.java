package mods.betterwithpatches.util;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface BWPConstants {
    String MODID = "betterwithpatches", MODNAME = "BetterWithPatches";
    Logger L = LogManager.getLogger(MODNAME);

    static String getId(Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }
}
