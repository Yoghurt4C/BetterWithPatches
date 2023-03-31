package mods.betterwithpatches.mixins.hcwood;

import betterwithmods.BWMod;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BWMod.class)
public abstract class BWModMixin {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lbetterwithmods/BWRegistry;registerWood()V", remap = false), remap = false)
    public void registerBarkBetterdly() {
        HardcoreWoodInteractionExtensions.addVanillaLogOverrides();
        HardcoreWoodInteractionExtensions.addVanillaTanninOverrides();
    }
}
