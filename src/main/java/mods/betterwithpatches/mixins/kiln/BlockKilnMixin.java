package mods.betterwithpatches.mixins.kiln;

import betterwithmods.blocks.BTWBlock;
import betterwithmods.blocks.BlockKiln;
import betterwithmods.util.InvUtils;
import mods.betterwithpatches.craft.KilnInteractionExtensions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(BlockKiln.class)
public abstract class BlockKilnMixin extends BTWBlock {
    @Shadow(remap = false)
    public abstract void setCookCounter(World world, int x, int y, int z, int cookCounter);

    @Shadow(remap = false)
    protected abstract int calculateTickRate(IBlockAccess world, int x, int y, int z);

    @Shadow(remap = false)
    protected abstract void cookBlock(World world, int x, int y, int z);

    @Shadow(remap = false)
    protected abstract boolean checkKilnIntegrity(IBlockAccess world, int x, int y, int z);

    public BlockKilnMixin(Material material, String name) {
        super(material, name);
    }

    /**
     * Original method has a lengthy 'if' check for the sake of hardcoded behaviours. It is now simplified to a single hashtable query.
     */
    @Inject(method = "updateTick", at = @At(value = "INVOKE", target = "Lbetterwithmods/craft/KilnInteraction;contains(Lnet/minecraft/block/Block;I)Z", remap = false), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void unHardcodeTick(World world, int x, int y, int z, Random rand, CallbackInfo ctx, int oldCookTime, int currentTickRate, boolean canCook, Block above, int aboveMeta) {
        ctx.cancel();
        if (KilnInteractionExtensions.contains(above, aboveMeta) && this.checkKilnIntegrity(world, x, y, z)) {
            int newCookTime = oldCookTime + 1;
            if (newCookTime > 7) {
                newCookTime = 0;
                this.cookBlock(world, x, y + 1, z);
            } else {
                if (newCookTime > 0) {
                    world.destroyBlockInWorldPartially(0, x, y + 1, z, newCookTime + 2);
                }

                currentTickRate = this.calculateTickRate(world, x, y, z);
            }

            this.setCookCounter(world, x, y, z, newCookTime);
            if (newCookTime == 0) {
                world.destroyBlockInWorldPartially(0, x, y + 1, z, -1);
                world.markBlockForUpdate(x, y, z);
            }
        } else if (oldCookTime != 0) {
            world.destroyBlockInWorldPartially(0, x, y + 1, z, -1);
            this.setCookCounter(world, x, y, z, 0);
            world.markBlockForUpdate(x, y, z);
        }

        world.scheduleBlockUpdate(x, y, z, this, currentTickRate);
    }

    /**
     * @reason If/else has fallen. Billions must be funneled through the hashtable.
     */
    @Inject(method = "cookBlock", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getBlockMetadata(III)I"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void cook(World world, int x, int y, int z, CallbackInfo ctx, Block block, int meta) {
        ctx.cancel();
        if (block != null) {
            ItemStack[] result = KilnInteractionExtensions.getProducts(block, meta);
            for (ItemStack stack : result) {
                InvUtils.ejectStackWithOffset(world, x, y, z, stack);
            }
            world.setBlockToAir(x, y, z);
        }
    }
}
