package mods.betterwithpatches.mixins.kiln;

import betterwithmods.BWCrafting;
import mods.betterwithpatches.kiln.KilnInteractionExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BWCrafting.class)
public abstract class BWCraftingMixin {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lbetterwithmods/BWCrafting;addKilnRecipes()V", remap = false), remap = false)
    private static void addKilnWoodProperly() {
        KilnInteractionExtensions.addKilnRecipes();
    }

    @Inject(method = "addKilnWood", at = @At("HEAD"), cancellable = true, remap = false)
    private static void justInCase(CallbackInfo ctx) {
        ctx.cancel();
    }
}
