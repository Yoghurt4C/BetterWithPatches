package mods.betterwithpatches.mixins.compat.signpic;

import com.kamesuta.mc.signpic.CoreHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CoreHandler.class)
public abstract class CoreHandlerMixin {
    @Inject(method = "debugKey", at = @At("HEAD"), remap = false, cancellable = true)
    public void stop(CallbackInfo ctx) {
        ctx.cancel();
    }
}
