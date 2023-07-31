package mods.betterwithpatches.mixins;

import betterwithmods.BWCrafting;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BWCrafting.class)
public abstract class BWCraftingMixin {

    @Redirect(method = "addVanillaRecipes", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/registry/GameRegistry;addRecipe(Lnet/minecraft/item/crafting/IRecipe;)V", ordinal = 14), remap = false)
    private static void poober(IRecipe recipe) {}
    @Redirect(method = "addVanillaRecipes", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/registry/GameRegistry;addRecipe(Lnet/minecraft/item/crafting/IRecipe;)V", ordinal = 15), remap = false)
    private static void goober(IRecipe recipe) {}
}
