package mods.betterwithpatches.mixins.hcgunpowder;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityFurnace.class)
public abstract class TileEntityFurnaceMixin extends TileEntity {

    @Unique
    public boolean explode = false;

    @Inject(method = "setInventorySlotContents", at = @At(value = "TAIL"))
    public void test(int index, ItemStack stack, CallbackInfo ctx) {
        if (index == 0 && stack != null && stack.getItem() == Items.gunpowder) {
            this.explode = true;
        }
    }

    @Inject(method = "updateEntity", at = @At(value = "JUMP", opcode = Opcodes.IFLE, ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    public void nut(CallbackInfo ctx) {
        if (this.explode) {
            ctx.cancel();
            this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 3f, true);
        }
    }
}
