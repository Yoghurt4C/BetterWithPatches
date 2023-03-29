package mods.betterwithpatches.mixins.turntable;

import betterwithmods.BWCrafting;
import mods.betterwithpatches.craft.TurntableInteractionExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BWCrafting.class)
public abstract class BWCraftingMixin {
    /**
     * @reason Changed default recipes to contain (formerly) hardcoded byproducts.
     */
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lbetterwithmods/BWCrafting;addTurntableRecipes()V", remap = false), remap = false)
    private static void tweakedTurntableRecipes() {
        TurntableInteractionExtensions.addTurntableRecipes();
    }
}
