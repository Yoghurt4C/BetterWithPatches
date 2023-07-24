package mods.betterwithpatches.mixins.hcfurnace.client;

import mods.betterwithpatches.util.IHCFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityFurnace.class)
public abstract class TileEntityFurnaceMixin {
    @Shadow
    public int furnaceCookTime;

    @Inject(method = "getCookProgressScaled", at = @At("HEAD"), cancellable = true)
    public void bleh(int current, CallbackInfoReturnable<Integer> ctx) {
        ctx.setReturnValue(this.furnaceCookTime * current / ((IHCFurnace)this).getCookTime());
    }
}
