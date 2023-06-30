package mods.betterwithpatches.mixins.hcbuckets;

import betterwithmods.BWMod;
import cpw.mods.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BWMod.class)
public abstract class BWModMixin {
    /**
     * @reason Don't need the event as we're mangling the bucket item itself.
     */
    @Redirect(method = "postInit", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/eventhandler/EventBus;register(Ljava/lang/Object;)V", ordinal = 1, remap = false), remap = false)
    private void noEvent(EventBus instance, Object eventType) {
    }
}
