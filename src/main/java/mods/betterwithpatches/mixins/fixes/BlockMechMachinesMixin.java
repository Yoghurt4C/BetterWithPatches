package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockMechMachines.class)
public abstract class BlockMechMachinesMixin extends BlockContainer {

    protected BlockMechMachinesMixin(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    /**
     * @reason Original edge-casing misses half of the tiles that can crash the game when passed to the inventory handler.
     */
    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    public void fixInventoryCrash(World world, int x, int y, int z, Block block, int meta, CallbackInfo ctx) {
        ctx.cancel();
        switch (meta) {
            case 0:
            case 8:
            case 2:
            case 10:
            case 3:
            case 11:
            case 4:
            case 12:
                InvUtils.ejectInventoryContents(world, x, y, z, (IInventory) world.getTileEntity(x, y, z));
                break;
            default:
                break;
        }
        super.breakBlock(world, x, y, z, block, meta);
    }
}
