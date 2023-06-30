package mods.betterwithpatches.mixins.hcwood;

import betterwithmods.BWMod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import mods.betterwithpatches.event.LogHarvestEventReplacement;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BWMod.class)
public abstract class BWModMixin {
    /**
     * @reason This is going to postinit or even later
     */
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lbetterwithmods/BWRegistry;registerWood()V", remap = false), remap = false)
    public void registerBarkBetterdly() {
    }

    /**
     * @reason Replacing the vanilla BWM event with one making use of the relevant patches. Not a redirect because I was lazy.
     */
    @Inject(method = "postInit", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/common/FMLCommonHandler;instance()Lcpw/mods/fml/common/FMLCommonHandler;", remap = false), cancellable = true, remap = false)
    public void boop(FMLPostInitializationEvent evt, CallbackInfo ctx) {
        ctx.cancel();
        MinecraftForge.EVENT_BUS.register(new LogHarvestEventReplacement());
    }
}
