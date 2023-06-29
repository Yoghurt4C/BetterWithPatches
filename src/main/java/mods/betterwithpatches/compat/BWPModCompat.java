package mods.betterwithpatches.compat;

import cpw.mods.fml.common.Loader;
import mods.betterwithpatches.compat.minetweaker.*;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;

public interface BWPModCompat {
    static void addMineTweakerCompat() {
        Class<?>[] compat = {
                CauldronTweaker.class,
                CrucibleTweaker.class,
                HeatRegistryTweaker.class,
                KilnTweaker.class,
                MillTweaker.class,
                SawTweaker.class,
                TurntableTweaker.class
        };
        for (Class<?> cls : compat) {
            minetweaker.MineTweakerAPI.registerClass(cls);
        }
    }

    static void addNatureBarkOverrides() {
        if (Loader.isModLoaded("Natura")) {
            String[] ids = new String[]{"Rare Tree", "tree"};
            for (String id : ids) {
                HardcoreWoodInteractionExtensions.overrideLogMeta("Natura", id, 0, 1, 2, 3);
            }
            HardcoreWoodInteractionExtensions.overrideLogMeta("Natura", "Dark Tree", 0, 1);
        }
    }

    static void addEBXLBarkOverrides() {
        if (Loader.isModLoaded("ExtrabiomesXL")) {
            String[] ids = new String[]{"log1", "log2", ""};
            for (String id : ids) {
                HardcoreWoodInteractionExtensions.overrideLogMeta("ExtrabiomesXL", id, 0, 1, 2, 3);
            }
        }
    }

    static void addThaumcraftBarkOverrides() {
        if (Loader.isModLoaded("Thaumcraft")) {
            HardcoreWoodInteractionExtensions.overrideLogMeta("Thaumcraft", "blockMagicalLog", 0, 1);
        }
    }
}
