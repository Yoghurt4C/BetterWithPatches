package mods.betterwithpatches.util;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import java.util.List;
import java.util.Set;

@LateMixin
public class BWPLateMixinConfig implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.betterwithpatches.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return new BWPMixinLoader(false).getMixins(loadedMods);
    }
}
