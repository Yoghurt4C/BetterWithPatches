package mods.betterwithpatches.mixins.fixes.cauldron;

import betterwithmods.blocks.tile.TileEntityCookingPot;
import betterwithmods.client.container.ContainerCookingPot;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(ContainerCookingPot.class)
public abstract class ContainerCookingPotMixin extends Container {
    @Shadow(remap = false)
    private TileEntityCookingPot tile;
    @Unique
    private int intensity = -1;

    @Inject(method = "onCraftGuiOpened", at = @At("RETURN"), remap = false)
    public void syncIntensity(ICrafting user, CallbackInfo ctx) {
        user.sendProgressBarUpdate(this, 1, this.tile.fireIntensity);
    }

    @Inject(method = "detectAndSendChanges", at = @At(value = "FIELD", target = "Lbetterwithmods/client/container/ContainerCookingPot;lastCookCounter:I", opcode = Opcodes.GETFIELD, remap = false, ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void syncIntensity(CallbackInfo ctx, Iterator<ICrafting> it, ICrafting user) {
        if (this.intensity != this.tile.fireIntensity) {
            user.sendProgressBarUpdate(this, 1, this.tile.fireIntensity);
        }
    }

    @Inject(method = "detectAndSendChanges", at = @At("RETURN"))
    public void setIntensity(CallbackInfo ctx) {
        this.intensity = this.tile.fireIntensity;
    }

    @Inject(method = "updateProgressBar", at = @At("RETURN"))
    public void clientIntensity(int index, int value, CallbackInfo ctx) {
        if (index == 1) {
            this.tile.fireIntensity = value;
        }
    }
}
