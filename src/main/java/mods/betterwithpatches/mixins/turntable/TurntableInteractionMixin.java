package mods.betterwithpatches.mixins.turntable;

import betterwithmods.craft.TurntableInteraction;
import mods.betterwithpatches.craft.TurntableInteractionExtensions;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Hashtable;

@Mixin(TurntableInteraction.class)
public abstract class TurntableInteractionMixin {

    @Shadow(remap = false)
    private static Hashtable<String, ItemStack> spinnables;

    /**
     * @reason Impl if he modern? You can call these directly, or use {@link TurntableInteractionExtensions}.
     */
    @Inject(method = "addBlockRecipe(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockRecipeFix(Block block, ItemStack output, CallbackInfo ctx) {
        ctx.cancel();
        TurntableInteractionExtensions.addBlockRecipe(block, output);
    }

    @Inject(method = "addBlockRecipe(Lnet/minecraft/block/Block;ILnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockRecipeFix(Block block, int meta, ItemStack output, CallbackInfo ctx) {
        ctx.cancel();
        TurntableInteractionExtensions.addBlockRecipe(block, meta, output);
    }

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cont(Block block, int meta, CallbackInfoReturnable<Boolean> ctx) {
        ctx.setReturnValue(TurntableInteractionExtensions.contains(block, meta));
    }

    /**
     * @see TurntableInteractionExtensions#getProducts(Block, int)
     */
    @Inject(method = "getProduct", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getFirstProduct(Block block, int meta, CallbackInfoReturnable<ItemStack> ctx) {
        ctx.setReturnValue(TurntableInteractionExtensions.getProducts(block, meta)[0]);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void free(CallbackInfo ctx) {
        spinnables = null;
    }
}
