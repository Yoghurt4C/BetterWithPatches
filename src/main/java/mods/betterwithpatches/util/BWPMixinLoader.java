package mods.betterwithpatches.util;

import mods.betterwithpatches.Config;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BWPMixinLoader {
    private final boolean early;
    private final List<String> list;

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

            if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
                load(Config.HCFurnace, "hcfurnace.client.TileEntityFurnaceMixin");
            }
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
            if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
                load(Config.patchHCWood, "hcwood.client.ItemBarkMixin");
                load(Config.patchCookingPot, "cauldron.GuiCookingPotMixin");
                if (loadedMods.contains("lwjgl3ify") && loadedMods.contains("signpic"))
                    load(Config.patchSignPicForLwjglify, "compat.signpic.GuiTaskAccessor", "compat.signpic.GuiTaskMixin", "compat.signpic.GuiTask$1$1Mixin", "compat.signpic.CoreHandlerMixin");
            }
        }
        return list;
    }

    private void load(boolean cfg, String... mixins) {
        if (cfg) Collections.addAll(list, mixins);
    }
}
