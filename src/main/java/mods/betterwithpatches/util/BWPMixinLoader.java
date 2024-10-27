package mods.betterwithpatches.util;

import mods.betterwithpatches.Config;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BWPMixinLoader {
    public final boolean early;
    public final List<String> list;

    public BWPMixinLoader(boolean early) {
        this.early = early;
        this.list = new ArrayList<>();
    }

    public List<String> getMixins(Set<String> loadedMods) {
        Config.tryInit();
        if (this.early) {
            load(Config.patchHCBuckets, "hcbuckets.ItemBucketMixin");
            load(Config.furnaceHCGunpowder, "hcgunpowder.TileEntityFurnaceMixin");
            load(Config.HCFurnace, "hcfurnace.TileEntityFurnaceMixin", "hcfurnace.ContainerFurnaceMixin");
            load(Config.enablePenalties, "penalty.EntityPlayerMixin");
            if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
                load(Config.HCFurnace, "hcfurnace.client.TileEntityFurnaceMixin");
                load(true, "client.RenderBipedMixin", "client.RenderPlayerMixin");
            }
        } else {
            load(true, "BWCraftingMixin", "BWRegistryMixin");
            load(Config.enableNEICompat, "CraftingManagerBulkMixin");
            load(Config.genericFixes, "fixes.BlockMechMachinesMixin", "fixes.TileEntityMechGeneratorMixin", "fixes.BlockGearboxMixin", "fixes.TileEntityTurntableMixin", "fixes.BulkRecipeMixin", "fixes.BlockPlanterMixin", "fixes.ItemMaterialMixin");
            load(Config.patchCreativeTabs, "fixes.creativetab.BWRegistryMixin");
            load(Config.patchKiln, "kiln.KilnInteractionMixin", "kiln.BlockKilnMixin", "kiln.BWCraftingMixin");
            load(Config.genericFixes && Config.patchTurntable, "turntable.BWCraftingMixin", "turntable.TileEntityTurntableMixin", "turntable.TurntableInteractionMixin");
            load(Config.patchHCWood, "hcwood.BWModMixin", "hcwood.ItemBarkMixin", "hcwood.compat.NaturaCompatMixin", "hcwood.BWCraftingMixin", "hcwood.HardcoreWoodInteractionMixin");
            load(Config.patchHCWood && Config.patchSaw, "saw.BWModMixin", "saw.BlockSawMixin", "saw.SawInteractionMixin");
            load(Config.dirtyStokedFlameFix, "fixes.dirty.BlockFireStokedMixin");
            load(Config.patchCookingPot, "cauldron.ContainerCookingPotMixin", "cauldron.TileEntityCookingPotMixin");
            load(Config.patchFilteredHopper, "filteredhopper.BlockMechMachinesMixin", "filteredhopper.TileEntityFilteredHopperMixin");
            load(Config.patchHCBuckets, "hcbuckets.BWModMixin");
            if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
                load(true, "client.RenderTileEntitiesMixin");
                load(Config.genericFixes, "fixes.client.ItemMaterialMixin");
                load(Config.patchHCWood, "hcwood.client.ItemBarkMixin");
                load(Config.patchCookingPot, "cauldron.GuiCookingPotMixin");
                load(Config.patchFilteredHopper, "filteredhopper.client.BlockMechMachinesMixin");
                if (loadedMods.contains("lwjgl3ify") && loadedMods.contains("signpic"))
                    load(Config.patchSignPicForLwjglify, "compat.signpic.GuiTaskAccessor", "compat.signpic.GuiTaskMixin", "compat.signpic.GuiTask$1$1Mixin", "compat.signpic.CoreHandlerMixin");
                load(loadedMods.contains("angelica"), "client.BlockBTWPaneMixin");
            }
        }
        return list;
    }

    private void load(boolean cfg, String... mixins) {
        if (cfg) Collections.addAll(list, mixins);
    }
}
