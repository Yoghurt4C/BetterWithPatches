package mods.betterwithpatches.util;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import mods.betterwithpatches.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@LateMixin
public class BWPLateMixinConfig implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.betterwithpatches.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> set) {
        Config.tryInit();
        List<String> list = new ArrayList<>();
        parse(Config.genericFixes, list, "fixes.BlockMechMachinesMixin", "fixes.TileEntityMechGeneratorMixin", "fixes.BlockGearboxMixin");
        parse(Config.patchKiln, list, "kiln.KilnInteractionMixin", "kiln.BlockKilnMixin", "kiln.BWCraftingMixin");
        return list;
    }

    private void parse(boolean configEntry, List<String> mx, String... mixins) {
        if (configEntry) Collections.addAll(mx, mixins);
    }
}
