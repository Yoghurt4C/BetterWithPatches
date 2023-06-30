package mods.betterwithpatches.mixins.hcbuckets;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mods.betterwithpatches.util.BWPConstants.isBlockReplaceable;

@Mixin(ItemBucket.class)
public abstract class ItemBucketMixin {

    @Shadow
    private Block isFull;

    @Inject(method = "tryPlaceContainedLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z"), cancellable = true)
    public void killWater(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> ctx) {
        world.setBlock(x, y, z, this.isFull, 1, 3);
        if (isBlockReplaceable(world, x + 1, y, z)) world.setBlock(x + 1, y, z, this.isFull, 2, 3);
        if (isBlockReplaceable(world, x, y, z + 1)) world.setBlock(x, y, z + 1, this.isFull, 2, 3);
        if (isBlockReplaceable(world, x - 1, y, z)) world.setBlock(x - 1, y, z, this.isFull, 2, 3);
        if (isBlockReplaceable(world, x, y, z - 1)) world.setBlock(x, y, z - 1, this.isFull, 2, 3);
        ctx.setReturnValue(true);
    }
}
