package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.BlockPlanter;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockPlanter.class)
public abstract class BlockPlanterMixin {
    @Inject(method = "isValidBlockStack", at = @At("HEAD"), cancellable = true, remap = false)
    public void nullCheck(ItemStack stack, CallbackInfoReturnable<Boolean> ctx) {
        if (stack == null) ctx.setReturnValue(false);
    }
}
