package mods.betterwithpatches.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface BWPConstants {
    String MODID = "betterwithpatches", MODNAME = "BetterWithPatches";
    Logger L = LogManager.getLogger(MODNAME);
    byte[] snakeX = new byte[]{1, 0, -1, -1, 0, 0, 1, 1};
    byte[] snakeZ = new byte[]{0, 1, 0, 0, -1, -1, 0, 0};
}
