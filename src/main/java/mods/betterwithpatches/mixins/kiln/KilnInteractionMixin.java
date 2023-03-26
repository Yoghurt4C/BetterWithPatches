package mods.betterwithpatches.mixins.kiln;

import betterwithmods.craft.KilnInteraction;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Hashtable;

@Mixin(KilnInteraction.class)
public abstract class KilnInteractionMixin {
    @Shadow(remap = false)
    private static Hashtable<String, ItemStack> cookables;

    /**
     * @reason Impl if he modern? Enables easier usage of OD without the need for bloating the table with entries.
     */
    @Inject(method = "addBlockRecipe(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockRecipeFix(Block block, ItemStack output, CallbackInfo ctx) {
        ctx.cancel();
        String identifier = GameData.getBlockRegistry().getNameForObject(block);
        cookables.put(identifier, output);
    }

    @Inject(method = "addBlockRecipe(Lnet/minecraft/block/Block;ILnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockRecipeFix(Block block, int meta, ItemStack output, CallbackInfo ctx) {
        ctx.cancel();
        String identifier = GameData.getBlockRegistry().getNameForObject(block);
        cookables.put(identifier + "@" + meta, output);
    }

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cont(Block block, int meta, CallbackInfoReturnable<Boolean> ctx) {
        /*todo for (int i = 0; i < OreDictionary.getOreIDs(new ItemStack(block)).length; i++) {
            if (cookables.containsKey(OreDictionary.getOreName(i)))
                ctx.setReturnValue(true);
        }*/
        String identifier = GameData.getBlockRegistry().getNameForObject(block);
        if (cookables.containsKey(identifier)) ctx.setReturnValue(true);
        else ctx.setReturnValue(cookables.containsKey(identifier + "@" + meta));
    }

    @Inject(method = "getProduct", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getP(Block block, int meta, CallbackInfoReturnable<ItemStack> ctx) {
        /*todo for (int i = 0; i < OreDictionary.getOreIDs(new ItemStack(block)).length; i++) {
            ItemStack stack = cookables.get(OreDictionary.getOreName(i);
            if (stack != null) {
                ctx.setReturnValue(stack);
            }
        }*/
        String identifier = GameData.getBlockRegistry().getNameForObject(block);
        ItemStack stack = cookables.get(identifier);
        if (stack == null) stack = cookables.get(identifier + "@" + meta);
        ctx.setReturnValue(stack);
    }
}
