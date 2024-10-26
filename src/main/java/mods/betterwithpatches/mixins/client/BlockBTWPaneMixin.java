package mods.betterwithpatches.mixins.client;

import betterwithmods.blocks.BlockBTWPane;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockBTWPane.class)
public abstract class BlockBTWPaneMixin {
    @Inject(method = "addCollisionBoxesToList", at = @At("HEAD"), remap = false, cancellable = true)
    public void validate(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity, CallbackInfo ctx) {
        if (!(world.getBlock(x, y, z) instanceof BlockBTWPane)) ctx.cancel();
    }

    @Inject(method = "setBlockBoundsBasedOnState", at = @At("HEAD"), remap = false, cancellable = true)
    public void validate(IBlockAccess world, int x, int y, int z, CallbackInfo ctx) {
        if (!(world.getBlock(x, y, z) instanceof BlockBTWPane)) ctx.cancel();
    }
}
