package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.blocks.tile.TileEntityCookingPot;
import betterwithmods.craft.heat.BWMHeatRegistry;
import mods.betterwithpatches.Config;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityCookingPot.class)
public abstract class TileEntityCookingPotMixin extends TileEntity {

    @Shadow(remap = false)
    public int fireIntensity;

    @Shadow(remap = false)
    protected abstract int getFireIntensity();

    @Shadow(remap = false)
    private boolean forceValidation;

    @Shadow(remap = false)
    public abstract void validateContents();

    @Shadow(remap = false)
    public int stokedCooldownCounter;

    @Shadow(remap = false)
    public int cookCounter;

    @Shadow(remap = false)
    protected abstract void performStokedFireUpdate(int fireIntensity);

    @Shadow(remap = false)
    public abstract int getCurrentFireIntensity();

    @Shadow(remap = false)
    protected abstract void performNormalFireUpdate(int fireIntensity);

    @Shadow(remap = false)
    public int forceFacing;

    @Shadow(remap = false)
    protected abstract void attemptToEjectStackFromInv(int facing);

    @Shadow(remap = false)
    public int scaledCookCounter;

    @Unique
    private short timer = 20;
    @Unique
    private int totalIntensity = 0;
    @Unique
    private boolean lazy = false;
    @Unique
    private final byte[] snakeX = new byte[]{1, 0, -1, -1, 0, 0, 1, 1};
    @Unique
    private final byte[] snakeZ = new byte[]{0, 1, 0, 0, -1, -1, 0, 0};

    @Inject(method = "updateEntity", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ctx) {
        ctx.cancel();
        if (!this.worldObj.isRemote) {
            BlockMechMachines block = (BlockMechMachines) this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
            if (this.timer >= (lazy ? Config.lazyCauldronDelay : 20)) {
                int intensity = this.getFireIntensity();
                if (this.fireIntensity == intensity) {
                    lazy = true;
                } else {
                    lazy = false;
                    this.fireIntensity = intensity;
                }
                this.totalIntensity = getCurrentFireIntensity();
                this.timer = 0;
            }

            if (!block.isMechanicalOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
                if (this.fireIntensity > 0) {
                    if (this.forceValidation) {
                        this.validateContents();
                        this.forceValidation = false;
                    }

                    if (this.fireIntensity > 4) {
                        if (this.stokedCooldownCounter < 1) {
                            this.cookCounter = 0;
                        }

                        this.stokedCooldownCounter = 20;
                        this.performStokedFireUpdate(this.totalIntensity);
                    } else if (this.stokedCooldownCounter > 0) {
                        --this.stokedCooldownCounter;
                        if (this.stokedCooldownCounter < 1) {
                            this.cookCounter = 0;
                        }
                    } else if (this.stokedCooldownCounter == 0 && this.fireIntensity > 0) {
                        this.performNormalFireUpdate(this.totalIntensity);
                    }
                } else {
                    this.cookCounter = 0;
                }
            } else {
                this.cookCounter = 0;
                int tilt = this.forceFacing;
                this.attemptToEjectStackFromInv(tilt);
            }

            this.scaledCookCounter = this.cookCounter * 1000 / 4350;
            this.timer++;
        }
    }

    @Inject(method = "getCurrentFireIntensity", at = @At("HEAD"), cancellable = true, remap = false)
    public void getTotalFireIntensity(CallbackInfoReturnable<Integer> ctx) {
        int fireFactor = this.fireIntensity;
        if (fireFactor > -1) {
            for (int i = 0; i < snakeX.length; i++) {
                int x = this.xCoord + snakeX[i];
                int y = this.yCoord - 1;
                int z = this.zCoord + snakeZ[i];
                Block block = this.worldObj.getBlock(x, y, z);
                int meta = this.worldObj.getBlockMetadata(x, y, z);
                if (BWMHeatRegistry.get(block, meta) != null) {
                    fireFactor += BWMHeatRegistry.get(block, meta).value;
                }
            }
            ctx.setReturnValue(fireFactor);
        } else ctx.setReturnValue(0);
    }

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    public void readTotal(NBTTagCompound tag, CallbackInfo ctx) {
        if (tag.hasKey("totalIntensity")) this.totalIntensity = tag.getInteger("totalIntensity");
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    public void appendTotal(NBTTagCompound tag, CallbackInfo ctx) {
        tag.setInteger("totalIntensity", this.totalIntensity);
    }
}
