package mods.betterwithpatches.mixins.saw;

import betterwithmods.blocks.BlockSaw;
import betterwithmods.util.BlockPosition;
import betterwithmods.util.InvUtils;
import mods.betterwithpatches.craft.SawInteractionExtensions;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BlockSaw.class)
public abstract class BlockSawMixin {

    @Shadow(remap = false)
    private static ItemStack axe;

    @Shadow(remap = false)
    public abstract int getFacing(IBlockAccess world, int x, int y, int z);

    @Shadow public abstract int tickRate(World par1);

    /**
     * @reason Making the saw invoke the Hardcore Wood event {@link mods.betterwithpatches.event.LogHarvestEventReplacement#harvestLog(BlockEvent.HarvestDropsEvent)} as fallback for the manual overrides.
     */
    @Inject(method = "sawBlockInFront", at = @At("HEAD"), cancellable = true, remap = false)
    public void applySaw(World world, int x, int y, int z, Random rand, CallbackInfo ctx) {
        ctx.cancel();
        if (!world.isRemote && world instanceof WorldServer) {
            BlockPosition pos = new BlockPosition(x, y, z, ForgeDirection.getOrientation(this.getFacing(world, x, y, z)), true);
            Block block = world.getBlock(pos.x, pos.y, pos.z);
            int harvestMeta = block.damageDropped(world.getBlockMetadata(pos.x, pos.y, pos.z));
            FakePlayer fake = FakePlayerFactory.getMinecraft((WorldServer) world);
            ItemStack stack = axe.copy();
            fake.setCurrentItemOrArmor(0, stack);

            if (SawInteractionExtensions.containsBlock(block, harvestMeta)) {
                ItemStack[] stacks = SawInteractionExtensions.getBlockOverrides(block, harvestMeta);
                for (ItemStack drop : stacks) {
                    InvUtils.ejectStackWithOffset(world, pos.x, pos.y, pos.z, drop);
                }
                block.removedByPlayer(world, fake, pos.x, pos.y, pos.z, false);
            } else if (BWPConstants.presentInOD(new ItemStack(block, 1, harvestMeta), "logWood") && fake.canHarvestBlock(block)) {
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x, y, z, world, block, harvestMeta, fake);
                MinecraftForge.EVENT_BUS.post(event);
                if (block.removedByPlayer(world, fake, pos.x, pos.y, pos.z, true))
                    block.harvestBlock(world, fake, pos.x, pos.y, pos.z, harvestMeta);

                world.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, "minecart.base", 1.5F + rand.nextFloat() * 0.1F, 2.0F + rand.nextFloat() * 0.1F);
            } else {
                world.scheduleBlockUpdate(x, y, z, (BlockSaw)(Object)this, tickRate(world) + rand.nextInt(6));
            }
            fake.setCurrentItemOrArmor(0, null);
        }

    }
}
