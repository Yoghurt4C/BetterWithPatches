package mods.betterwithpatches.mixins.turntable;

import betterwithmods.api.block.IBTWBlock;
import betterwithmods.blocks.tile.TileEntityTurntable;
import mods.betterwithpatches.craft.TurntableInteractionExtensions;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TileEntityTurntable.class)
public abstract class TileEntityTurntableMixin extends TileEntity {

    @Shadow(remap = false)
    private boolean potteryRotated;

    @Shadow(remap = false)
    private int potteryRotation;

    @Shadow(remap = false)
    protected abstract void rotateRail(BlockRail rail, int x, int y, int z, boolean reverse);

    @Shadow(remap = false)
    protected abstract void rotateRepeater(BlockRedstoneDiode repeater, int x, int y, int z, boolean reverse);

    @Shadow(remap = false)
    protected abstract void rotatePiston(BlockPistonBase piston, int x, int y, int z, boolean reverse);

    @Shadow(remap = false)
    protected abstract void rotateLog(int x, int y, int z, boolean reverse);

    @Shadow(remap = false)
    protected abstract void rotateStairs(int x, int y, int z, boolean reverse);

    /**
     * @reason This is probably faster than redirection in every aspect, sans the LOC count.
     */
    @Inject(method = "rotateBlock", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getBlockMetadata(III)I"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, remap = false)
    public void basicallyReplaceEntireMethod(int x, int y, int z, boolean reverse, CallbackInfo ctx, Block target, int meta) {
        ctx.cancel();
        IBTWBlock block;
        ItemStack[] products = TurntableInteractionExtensions.getProducts(target, meta);
        if (products == null) {
            if (target instanceof BlockRail) {
                BlockRail rail = (BlockRail) target;
                this.rotateRail(rail, x, y, z, reverse);
            }

            if (target instanceof IBTWBlock) {
                block = (IBTWBlock) target;
                if (block.canRotateOnTurntable(this.worldObj, x, y, z)) {
                    block.rotateAroundYAxis(this.worldObj, x, y, z, reverse);
                }
            } else if (target instanceof BlockRedstoneDiode) {
                BlockRedstoneDiode repeater = (BlockRedstoneDiode) target;
                this.rotateRepeater(repeater, x, y, z, reverse);
            } else if (target instanceof BlockPistonBase) {
                BlockPistonBase piston = (BlockPistonBase) target;
                this.rotatePiston(piston, x, y, z, reverse);
            } else if (target instanceof BlockRotatedPillar) {
                this.rotateLog(x, y, z, reverse);
            } else if (target instanceof BlockStairs) {
                this.rotateStairs(x, y, z, reverse);
            }

        } else {
            if (target instanceof IBTWBlock) {
                block = (IBTWBlock) target;
                if (block.canRotateOnTurntable(this.worldObj, x, y, z)) {
                    block.rotateAroundYAxis(this.worldObj, x, y, z, reverse);
                }
            }

            this.rotateCraftableFromTable(products, x, y, z, target.stepSound.getBreakSound());
            this.potteryRotated = true;
        }
    }

    /**
     * Replacement for TileEntityTurntable#rotateCraftable with streamlined checks and less bold assumptions. Got rid of hardcoded behaviour.
     * No point in injecting into the existing method because its LVT is useless with the patched recipe table.
     */
    @Unique
    private void rotateCraftableFromTable(ItemStack[] products, int x, int y, int z, String soundType) {
        if (products == null) return;
        ItemStack craftingStack = products[0];
        ++this.potteryRotation;
        if (this.potteryRotation > 7) {
            if (craftingStack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) craftingStack.getItem()).field_150939_a;
                int meta = craftingStack.getItemDamage();
                this.worldObj.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, soundType, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.8F);
                for (int i = 1; i < products.length; i++) {
                    BWPUtils.scatter(this.worldObj, x, y + 1, z, products[i]);
                }

                this.worldObj.setBlock(x, y, z, block, meta, 3);
            } else {
                BWPUtils.scatter(this.worldObj, x, y + 1, z, craftingStack);
            }

            this.potteryRotation = 0;
        }
    }
}
