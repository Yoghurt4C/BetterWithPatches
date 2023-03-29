package mods.betterwithpatches.mixins.fixes.dirty;

import betterwithmods.blocks.BlockFireStoked;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(BlockFireStoked.class)
public abstract class BlockFireStokedMixin extends BlockFire {

    @Shadow
    public abstract int tickRate(World par1);

    /**
     * @reason Something's wrong with the updates. CBA to fix the root of the issue at the moment.
     * todo fix the shit
     */
    @Inject(method = "updateTick", at = @At(value = "JUMP", opcode = Opcodes.IF_ICMPLT, shift = At.Shift.BEFORE, ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void meta(World world, int x, int y, int z, Random rand, CallbackInfo ctx, Block below, int meta) {
        ctx.cancel();
        if (meta >= 5) {
            world.setBlock(x, y, z, Blocks.fire, 15, 3);
        } else {
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world) + world.rand.nextInt(10));
        }
    }
}
