package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.blocks.tile.TileEntityTurntable;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityTurntable.class)
public abstract class TileEntityTurntableMixin extends TileEntity {
    @Shadow(remap = false)
    public abstract void rotateTurntable();

    @Shadow(remap = false)
    private static int[] ticksToRotate;
    @Shadow(remap = false)
    public byte timerPos;
    @Unique
    private int timer = 0;

    /**
     * @reason Slightly streamlined checks, untangled ticks from world time.
     */
    @Inject(method = "updateEntity", at = @At("HEAD"), cancellable = true)
    public void patchTurntable(CallbackInfo ctx) {
        ctx.cancel();
        if (!this.worldObj.isRemote && this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) != null) {
            if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 6 || this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 14) {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) - 1, 3);
            }

            this.timer++;
            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) instanceof BlockMechMachines
                    && ((BlockMechMachines) this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord)).isMechanicalOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord)
                    && this.timer >= ticksToRotate[this.timerPos]) {
                this.worldObj.playSoundEffect((double) this.xCoord + 0.5, (double) this.yCoord + 0.5, (double) this.zCoord + 0.5, "random.click", 0.05F, 1.0F);
                this.rotateTurntable();
                this.timer = 0;
            }
        }
    }
}
