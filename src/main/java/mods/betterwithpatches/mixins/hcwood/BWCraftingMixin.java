package mods.betterwithpatches.mixins.hcwood;

import betterwithmods.BWCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BWCrafting.class)
public abstract class BWCraftingMixin {

    /**
     * @reason Quote-unquote graceful cancellation of the existing recipes involving Bark.
     */
    @ModifyVariable(method = "addCauldronRecipes", at = @At(value = "LOAD", ordinal = 0), remap = false)
    private static int noTanHere(int theNumberOf0) {
        return 6;
    }

}