package mods.betterwithpatches.mixins;

import mods.betterwithpatches.util.BWPMixinLoader;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class BWPMixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        try {
            Class.forName("com.gtnewhorizon.gtnhmixins.LateMixin", false, this.getClass().getClassLoader());
            return null;
        } catch (ClassNotFoundException e) {
            return new BWPMixinLoader(true).getMixins();
        }
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }
}
