package mods.betterwithpatches.mixins;

import betterwithmods.BWRegistry;
import mods.betterwithpatches.BWPRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BWRegistry.class)
public abstract class BWRegistryMixin {
    /**
     * Circumventing Forge's idiotic registry limitations by registering items through coremodding - because that's much saner than being able to pass your own modid.
     */
    @Inject(method = "init", at = @At("TAIL"), remap = false)
    private static void moreThings(CallbackInfo ctx) {
        BWPRegistry.init();
    }
}
