package mods.betterwithpatches.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mods.betterwithpatches.Config;

import java.io.File;
import java.util.*;

public class BWPMixinLoader {
    private final boolean hax;
    private final List<String> list, loadedMods;

    public BWPMixinLoader(boolean hax) {
        this.hax = hax;
        this.list = new ArrayList<>();
        this.loadedMods = new ArrayList<>();
    }

    public List<String> getMixins() {
        Config.tryInit();
        final String bwm = "Better With Mods";
        load(bwm, Config.enableNEICompat, "CraftingManagerBulkMixin");
        load(bwm, Config.genericFixes, "fixes.BlockMechMachinesMixin", "fixes.TileEntityMechGeneratorMixin", "fixes.BlockGearboxMixin", "fixes.TileEntityTurntableMixin");
        load(bwm, Config.patchKiln, "kiln.KilnInteractionMixin", "kiln.BlockKilnMixin", "kiln.BWCraftingMixin");
        load(bwm, Config.patchTurntable, "turntable.BWCraftingMixin", "turntable.TileEntityTurntableMixin", "turntable.TurntableInteractionMixin");
        load(bwm, Config.patchHCWood, "hcwood.BWModMixin", "hcwood.ItemBarkMixin", "hcwood.compat.NaturaCompatMixin", "hcwood.BWCraftingMixin");
        load(bwm, Config.dirtyStokedFlameFix, "fixes.dirty.BlockFireStokedMixin");
        if (FMLCommonHandler.instance().getSide().equals(Side.CLIENT)) {
            load(bwm, Config.patchHCWood, "hcwood.client.ItemBarkMixin");
        }
        return list;
    }


    private void load(String mod, boolean cfg, String... mixins) {
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

    @SuppressWarnings("deprecation")
    private boolean canLoad(List<String> mods, String modname) throws Exception {
        if (mods.contains(modname)) return false;
        Set<String> names = getModNames(modname);
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

    private Set<String> getModNames(String mod) {
        Set<String> mutated = new HashSet<>();
        Collections.addAll(mutated, mod, mod.toLowerCase(Locale.ROOT), mod.toUpperCase(Locale.ROOT));
        if (mod.contains(" ")) {
            String trimmed = mod.replace(" ", "");
            String scored = mod.replace(" ", "_");
            Collections.addAll(mutated, trimmed, trimmed.toLowerCase(Locale.ROOT), trimmed.toUpperCase(Locale.ROOT), scored, scored.toLowerCase(Locale.ROOT), scored.toUpperCase(Locale.ROOT));
        }
        return mutated;
    }
}
