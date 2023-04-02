package mods.betterwithpatches.mixins.saw;

import betterwithmods.BWMod;
import cpw.mods.fml.common.eventhandler.EventBus;
import mods.betterwithpatches.event.MobDropEventReplacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BWMod.class)
public abstract class BWModMixin {
    /**
     * @reason Replacing the vanilla BWM event with one making use of the relevant patches.
     */
    @Redirect(method = "postInit", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/eventhandler/EventBus;register(Ljava/lang/Object;)V", ordinal = 0, remap = false), remap = false)
    private void otherDrops(EventBus instance, Object eventType) {
        instance.register(new MobDropEventReplacement());
    }
}
