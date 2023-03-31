package mods.betterwithpatches.compat;

import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;

public interface BWPNatura {
    static void addBarkOverrides() {
        String[] ids = new String[]{"bloodwood", "redwood", "willow"};
        for (String id : ids) {
            HardcoreWoodInteractionExtensions.overrideLogMeta("Natura:" + id, 0);
        }
        HardcoreWoodInteractionExtensions.overrideLogMeta("Natura:Dark Tree", 0, 1);
    }
}
