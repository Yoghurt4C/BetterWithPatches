package mods.betterwithpatches.mixins.hcwood.compat;

import betterwithmods.integration.Natura;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Natura.class)
public abstract class NaturaCompatMixin {

    //todo more grace less funny face
    @Inject(method = "addLogsToHardcoreWood", at = @At("HEAD"), cancellable = true, remap = false)
    private static void giveUp(CallbackInfo ctx) {
        ctx.cancel();
    }
}
