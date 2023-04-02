package mods.betterwithpatches.mixins.saw;

import betterwithmods.craft.SawInteraction;
import mods.betterwithpatches.craft.SawInteractionExtensions;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Hashtable;

@Mixin(SawInteraction.class)
public abstract class SawInteractionMixin {
    @Shadow(remap = false)
    private static Hashtable<String, ItemStack> woodProduct;

    /**
     * @reason Impl if he modern? You can call these directly, or use {@link mods.betterwithpatches.craft.SawInteractionExtensions}.
     */
    @Inject(method = "addBlock(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockRecipeFix(Block block, ItemStack output, CallbackInfo ctx) {
        ctx.cancel();
        SawInteractionExtensions.setBlock(block, output);
    }

    @Inject(method = "addBlock(Lnet/minecraft/block/Block;ILnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockRecipeFix(Block block, int meta, ItemStack output, CallbackInfo ctx) {
        ctx.cancel();
        SawInteractionExtensions.setBlock(block, meta, output);
    }

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cont(Block block, int meta, CallbackInfoReturnable<Boolean> ctx) {
        ctx.setReturnValue(SawInteractionExtensions.containsBlock(block, meta));
    }

    /**
     * @see mods.betterwithpatches.craft.SawInteractionExtensions#getBlockOverrides(Block, int)
     */
    @Inject(method = "getProduct", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getFirstProduct(Block block, int meta, CallbackInfoReturnable<ItemStack> ctx) {
        ctx.setReturnValue(SawInteractionExtensions.getBlockOverrides(block, meta)[0]);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void free(CallbackInfo ctx) {
        woodProduct = null;
    }
}
