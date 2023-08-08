package mods.betterwithpatches.util;

import betterwithmods.BWRegistry;
import net.minecraft.item.ItemStack;

public interface BWMaterials {
    String[] materialNames = new String[]{"Gear", "Nethercoal", "Hemp", "HempFibers", "HempCloth", "Dung", "TannedLeather", "ScouredLeather", "LeatherStrap", "LeatherBelt", "WoodBlade", "WindmillBlade", "Glue", "Tallow", "IngotSteel", "GroundNetherrack", "HellfireDust", "ConcentratedHellfire", "CoalDust", "Filament", "PolishedLapis", "Potash", "Sawdust", "SoulDust", "Screw", "Brimstone", "Niter", "Element", "Fuse", "BlastingOil", "NuggetIron", "NuggetSteel", "LeatherCut", "TannedLeatherCut", "ScouredLeatherCut", "RedstoneLatch", "NetherSludge", "Flour", "Haft", "CharcoalDust", "SharpeningStone", "KnifeBlade", "SoulFlux", "EnderSlag", "EnderOcular", "Padding", "ArmorPlate", "Broadhead", "CocoaPowder", "DiamondIngot", "Chainmail"};

    short GEAR = 0,
            NETHERCOAL = 1,
            HEMP = 2,
            HEMP_FIBERS = 3,
            HEMP_CLOTH = 4,
            DUNG = 5,
            TANNED_LEATHER = 6,
            SCOURED_LEATHER = 7,
            LEATHER_STRAP = 8,
            LEATHER_BELT = 9,
            WOOD_BLADE = 10,
            WINDMILL_BLADE = 11,
            GLUE = 12,
            TALLOW = 13,
            INGOT_STEEL = 14,
            GROUND_NETHERRACK = 15,
            HELLFIRE_DUST = 16,
            CONCENTRATED_HELLFIRE = 17,
            COAL_DUST = 18,
            FILAMENT = 19,
            POLISHED_LAPIS = 20,
            POTASH = 21,
            SAWDUST = 22,
            SOUL_DUST = 23,
            SCREW = 24,
            BRIMSTONE = 25,
            NITER = 26,
            ELEMENT = 27,
            FUSE = 28,
            BLASTING_OIL = 29,
            NUGGET_IRON = 30,
            NUGGET_STEEL = 31,
            LEATHER_CUT = 32,
            TANNED_LEATHER_CUT = 33,
            SCOURED_LEATHER_CUT = 34,
            REDSTONE_LATCH = 35,
            NETHER_SLUDGE = 36,
            FLOUR = 37,
            HAFT = 38,
            CHARCOAL_DUST = 39,
            SHARPENING_STONE = 40,
            KNIFE_BLADE = 41,
            SOUL_FLUX = 42,
            ENDER_SLAG = 43,
            ENDER_OCULAR = 44,
            PADDING = 45,
            ARMOR_PLATE = 46,
            BROADHEAD = 47,
            COCOA_POWDER = 48,
            DIAMOND_INGOT = 49,
            CHAINMAIL = 50;

    static ItemStack getMaterial(short meta) {
        return getMaterial(meta, 1);
    }

    static ItemStack getMaterial(short meta, int stackSize) {
        return new ItemStack(BWRegistry.material, stackSize, meta);
    }
}
