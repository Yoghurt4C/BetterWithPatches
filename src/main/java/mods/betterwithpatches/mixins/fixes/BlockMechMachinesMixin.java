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

    @Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lbetterwithmods/util/InvUtils;ejectInventoryContents(Lnet/minecraft/world/World;IIILnet/minecraft/inventory/IInventory;)V"), cancellable = true, remap = true)
    public void fixPulleyCrash(World world, int x, int y, int z, Block block, int meta, CallbackInfo ctx) {
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
            default: break;
        }
    }
}
