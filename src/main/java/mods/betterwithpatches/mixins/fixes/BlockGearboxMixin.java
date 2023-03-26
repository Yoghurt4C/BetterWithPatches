package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.BTWBlock;
import betterwithmods.blocks.BlockGearbox;
import betterwithmods.util.DirUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockGearbox.class)
public abstract class BlockGearboxMixin extends BTWBlock {
    public BlockGearboxMixin(Material material, String name) {
        super(material, name);
    }

    /**
     * @reason Inverse target facing when the player is sneaking. Purely QOL.
     */
    @Inject(method = "onBlockActivated", at = @At(value = "INVOKE", target = "Lbetterwithmods/blocks/BlockGearbox;toggleFacing(Lnet/minecraft/world/World;IIIZ)Z", remap = false), cancellable = true)
    private void fix(World world, int x, int y, int z, EntityPlayer player, int side, float flX, float flY, float flZ, CallbackInfoReturnable<Boolean> ctx) {
        this.toggleFacing(world, x, y, z, player.isSneaking());
        if (!world.isRemote) {
            world.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, this.stepSound.soundName, (this.stepSound.getVolume() + 1.0F) / 2.0F, this.stepSound.getPitch() * 0.8F);
        }

        ctx.setReturnValue(true);
    }

    /**
     * @reason Original method doesn't actually do anything and just queues pointless block updates.
     */
    @Inject(method = "toggleFacing", at = @At(value = "INVOKE_ASSIGN", target = "Lbetterwithmods/blocks/BlockGearbox;getFacing(Lnet/minecraft/world/IBlockAccess;III)I", remap = false), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, remap = false)
    public void fixToggleFacing(World world, int x, int y, int z, boolean reverse, CallbackInfoReturnable<Boolean> ctx, int facing) {
        this.setFacing(world, x, y, z, DirUtils.cycleFacing(facing, reverse));
        world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        world.scheduleBlockUpdate(x, y, z, (BlockGearbox) (Object) this, 10);
        world.notifyBlockChange(x, y, z, (BlockGearbox) (Object) this);
        ctx.setReturnValue(true);
    }
}
