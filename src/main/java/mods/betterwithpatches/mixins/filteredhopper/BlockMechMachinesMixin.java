package mods.betterwithpatches.mixins.filteredhopper;

import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import mods.betterwithpatches.util.IFilteredHopper;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockMechMachines.class)
public abstract class BlockMechMachinesMixin {
    /**
     * @author Yoghurt4C
     * @reason AAAA
     */
    @Overwrite
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta == 4 || meta == 12) {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileEntityFilteredHopper) {
                    ((IFilteredHopper) tile).onEntityCollidedWithHopper(world, x, y, z, entity);
                }
            }
        }
    }
}
