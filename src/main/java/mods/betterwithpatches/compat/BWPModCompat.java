package mods.betterwithpatches.compat;

import cpw.mods.fml.common.Loader;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;

public interface BWPModCompat {
    static void addNatureBarkOverrides() {
        if (Loader.isModLoaded("Natura")) {
            String[] ids = new String[]{"bloodwood", "redwood", "willow"};
            for (String id : ids) {
                HardcoreWoodInteractionExtensions.overrideLogMeta("Natura", id, 0);
            }
            HardcoreWoodInteractionExtensions.overrideLogMeta("Natura", "Dark Tree", 0, 1);
        }
    }

    static void addEBXLBarkOverrides() {
        if (Loader.isModLoaded("ExtrabiomesXL")) {
            String[] ids = new String[]{"mini_log_1", "cornerlog_baldcypress", "cornerlog_rainboweucalyptus", "cornerlog_oak", "cornerlog_fir", "cornerlog_redwood", "log_elbow_baldcypress", "log_elbow_rainbow_eucalyptus"};
            for (String id : ids) {
                HardcoreWoodInteractionExtensions.overrideLogMeta("ExtrabiomesXL", id, 0);
            }
        }
    }
}
