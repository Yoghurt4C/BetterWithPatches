package mods.betterwithpatches.mixins.hcwood;

import betterwithmods.BWCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BWCrafting.class)
public abstract class BWCraftingMixin {

    @ModifyVariable(method = "addCauldronRecipes", at = @At(value = "LOAD", ordinal = 0), remap = false)
    private static int noTanHere(int theNumberOf0) {
        return 6;
    }
}
