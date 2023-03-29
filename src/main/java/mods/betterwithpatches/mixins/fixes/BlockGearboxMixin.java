package mods.betterwithpatches.mixins.fixes;

import betterwithmods.blocks.BTWBlock;
import betterwithmods.blocks.BlockGearbox;
import betterwithmods.util.DirUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockGearbox.class)
public abstract class BlockGearboxMixin extends BTWBlock {
    @Shadow(remap = false)
    public abstract void setFacing(World world, int x, int y, int z, int facing);

    public BlockGearboxMixin(Material material, String name) {
        super(material, name);
    }

    /**
     * @reason Inverse target facing when the player is sneaking. Purely QOL.
     * Also fixes faulty sound path.
     */
    @Inject(method = "onBlockActivated", at = @At(value = "INVOKE", target = "Lbetterwithmods/blocks/BlockGearbox;toggleFacing(Lnet/minecraft/world/World;IIIZ)Z", remap = false), cancellable = true)
    private void rotationBackwards(World world, int x, int y, int z, EntityPlayer player, int side, float flX, float flY, float flZ, CallbackInfoReturnable<Boolean> ctx) {
        this.toggleFacing(world, x, y, z, player.isSneaking());
        if (!world.isRemote) {
            world.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, this.stepSound.getStepResourcePath(), this.stepSound.getVolume(), this.stepSound.getPitch() * 0.8F);
        }

        ctx.setReturnValue(true);
    }

    /**
     * @reason Invocation's result is being flushed down the toilet, which is rather wasteful.
     */
    @Redirect(method = "toggleFacing", at = @At(value = "INVOKE", target = "Lbetterwithmods/util/DirUtils;cycleFacing(IZ)I", remap = false), remap = false)
    public int toggleFacingFix(int facing, boolean reverse, World world, int x, int y, int z) {
        this.setFacing(world, x, y, z, DirUtils.cycleFacing(facing, reverse));
        return 0;
    }
}
