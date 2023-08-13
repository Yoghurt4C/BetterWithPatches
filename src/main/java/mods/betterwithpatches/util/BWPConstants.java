package mods.betterwithpatches.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public interface BWPConstants {
    String MODID = "betterwithpatches", MODNAME = "BetterWithPatches";
    Logger L = LogManager.getLogger(MODNAME);
    Random RANDOM = new Random();
    byte[] snakeX = new byte[]{1, 0, -1, -1, 0, 0, 1, 1};
    byte[] snakeZ = new byte[]{0, 1, 0, 0, -1, -1, 0, 0};
    int[] dyeOreIds = new int[16];
    float div180byPi = 0.01745329251994329576923690768489f;
}
