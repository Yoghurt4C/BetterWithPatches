package mods.betterwithpatches.util;

import mods.betterwithpatches.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public interface BWPConstants {
    String MODID = "betterwithpatches", MODNAME = "BetterWithPatches";
    Logger L = LogManager.getLogger(MODNAME);

    static List<String> getMixins(boolean hax) {
        Config.tryInit();
        List<String> list = new ArrayList<>();
        List<String> mods = new ArrayList<>();
        String bwm = "Better With Mods";
        load(hax, true, list, mods, bwm, "CraftingManagerBulkMixin");
        load(hax, Config.genericFixes, list, mods, bwm, "fixes.BlockMechMachinesMixin", "fixes.TileEntityMechGeneratorMixin", "fixes.BlockGearboxMixin");
        load(hax, Config.patchKiln, list, mods, bwm, "kiln.KilnInteractionMixin", "kiln.BlockKilnMixin", "kiln.BWCraftingMixin");
        load(hax, Config.dirtyStokedFlameFix, list, mods, bwm, "fixes.dirty.BlockFireStokedMixin");
        return list;
    }


    static void load(boolean hax, boolean cfg, List<String> list, List<String> loadedMods, String mod, String... mixins) {
        if (cfg) {
            try {
                if (hax && canLoad(loadedMods, mod)) {
                    loadedMods.add(mod);
                }
                Collections.addAll(list, mixins);
            } catch (Exception e) {
                BWPConstants.L.error(e.getMessage());
            }
        }
    }

    static boolean canLoad(List<String> mods, String modname) throws Exception {
        if (mods.contains(modname)) return false;
        String trimmed = modname.replace(" ", "");
        String[] names = new String[]{modname, modname.toLowerCase(Locale.ROOT), modname.toUpperCase(Locale.ROOT), trimmed, trimmed.toLowerCase(Locale.ROOT), trimmed.toUpperCase(Locale.ROOT)};
        for (String s : names) {
            File jar = ru.timeconqueror.spongemixins.MinecraftURLClassPath.getJarInModPath(s);
            if (jar != null && jar.exists()) {
                BWPConstants.L.info("Applying mixins to {}...", modname);
                ru.timeconqueror.spongemixins.MinecraftURLClassPath.addJar(jar);
                return true;
            }
        }
        return false;
    }
}
