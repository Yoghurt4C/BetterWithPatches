package mods.betterwithpatches.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mods.betterwithpatches.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BWPMixinLoader {
    private final boolean early;
    private final List<String> list;

    public BWPMixinLoader(boolean early) {
        this.early = early;
        this.list = new ArrayList<>();
    }

    public List<String> getMixins() {
        Config.tryInit();
        if (this.early) {
            load(Config.patchHCBuckets, "hcbuckets.ItemBucketMixin");
        } else {
            load(Config.enableNEICompat, "CraftingManagerBulkMixin");
            load(Config.genericFixes, "fixes.BlockMechMachinesMixin", "fixes.TileEntityMechGeneratorMixin", "fixes.BlockGearboxMixin", "fixes.TileEntityTurntableMixin", "fixes.BulkRecipeMixin", "fixes.BlockPlanterMixin");
            load(Config.patchKiln, "kiln.KilnInteractionMixin", "kiln.BlockKilnMixin", "kiln.BWCraftingMixin");
            load(Config.genericFixes && Config.patchTurntable, "turntable.BWCraftingMixin", "turntable.TileEntityTurntableMixin", "turntable.TurntableInteractionMixin");
            load(Config.patchHCWood, "hcwood.BWModMixin", "hcwood.ItemBarkMixin", "hcwood.compat.NaturaCompatMixin", "hcwood.BWCraftingMixin", "hcwood.HardcoreWoodInteractionMixin");
            load(Config.patchHCWood && Config.patchSaw, "saw.BWModMixin", "saw.BlockSawMixin", "saw.SawInteractionMixin");
            load(Config.dirtyStokedFlameFix, "fixes.dirty.BlockFireStokedMixin");
            load(Config.patchCookingPot, "cauldron.ContainerCookingPotMixin", "cauldron.TileEntityCookingPotMixin");
            load(Config.patchHCBuckets, "hcbuckets.BWModMixin");
            if (FMLCommonHandler.instance().getSide().equals(Side.CLIENT)) {
                load(Config.patchHCWood, "hcwood.client.ItemBarkMixin");
                load(Config.patchCookingPot, "cauldron.GuiCookingPotMixin");
            }
        }
        return list;
    }


    private void load(boolean cfg, String... mixins) {
        if (cfg) Collections.addAll(list, mixins);
    }
}
