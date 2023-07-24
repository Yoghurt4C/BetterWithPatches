package mods.betterwithpatches.mixins.hcfurnace;

import mods.betterwithpatches.Config;
import mods.betterwithpatches.util.IHCFurnace;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ICrafting;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ContainerFurnace.class)
public abstract class ContainerFurnaceMixin extends Container {

    @Shadow private TileEntityFurnace tileFurnace;
    @Unique public int cachedCookTime = Config.hcFurnaceDefaultCookTime;

    @Inject(method = "addCraftingToCrafters", at = @At("TAIL"))
    public void syncCookTime(ICrafting crafter, CallbackInfo ctx) {
        crafter.sendProgressBarUpdate(this, 76, ((IHCFurnace)this.tileFurnace).getCookTime());
    }

    @Inject(method = "detectAndSendChanges", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/inventory/ContainerFurnace;lastCookTime:I"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void syncCookTime(CallbackInfo ci, int i, ICrafting crafter) {
        int cookTime = ((IHCFurnace)this.tileFurnace).getCookTime();
        if (this.cachedCookTime != cookTime) {
            crafter.sendProgressBarUpdate(this, 76, cookTime);
        }
    }

    @Inject(method = "detectAndSendChanges", at = @At("RETURN"))
    public void setCookTime(CallbackInfo ctx) {
        this.cachedCookTime = ((IHCFurnace)this.tileFurnace).getCookTime();
    }

    @Inject(method = "updateProgressBar", at = @At("RETURN"))
    public void clientCookTime(int index, int value, CallbackInfo ctx) {
        if (index == 76) {
            ((IHCFurnace)this.tileFurnace).setCookTime(value);
        }
    }
}
