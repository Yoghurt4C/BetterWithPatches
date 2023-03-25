package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.BlockMechMachines;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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
        switch (meta) {
            case 1:
            case 9:
            case 5:
            case 6:
            case 7:
            case 13:
            case 14:
                ctx.cancel();
                super.breakBlock(world, x, y, z, block, meta);
                break;
            default:
                break;
        }
    }
}
