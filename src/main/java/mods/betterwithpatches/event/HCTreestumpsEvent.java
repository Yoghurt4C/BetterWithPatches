package mods.betterwithpatches.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class HCTreestumpsEvent {

    @SubscribeEvent
    public void breakSneed(PlayerEvent.BreakSpeed evt) {
        if (this.isDirt(evt.entityPlayer.worldObj.getBlock(evt.x, evt.y - 1, evt.z)) && this.isLog(evt.block, evt.metadata)) {
            evt.newSpeed = evt.newSpeed * 0.05f;
        } else if (this.isDirt(evt.block) && this.isLog(evt.entityPlayer.worldObj, evt.x, evt.y + 1, evt.z)) {
            evt.newSpeed = evt.newSpeed * 0.01f;
        }
    }

    private boolean isDirt(Block block) {
        return block.getMaterial() == Material.ground || block.getMaterial() == Material.grass;
    }

    private boolean isLog(Block block, int meta) {
        if ((meta & 3) == meta) {
            if (block instanceof BlockLog) return true;
            else return BWPConstants.presentInOD(new ItemStack(block, 1, meta), "logWood");
        }
        return false;
    }

    private boolean isLog(World world, int x, int y, int z) {
        return this.isLog(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }
}
