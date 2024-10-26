package mods.betterwithpatches.craft.filteredhopper;

import betterwithmods.BWRegistry;
import betterwithmods.api.block.ISoulSensitive;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.util.InvUtils;
import mods.betterwithpatches.util.IFilteredHopper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SoulUrnRecipe extends HopperRecipe {

    public SoulUrnRecipe(Object... outputs) {
        super(-1, outputs);
    }

    public SoulUrnRecipe(int oreId, Object... outputs) {
        super(oreId, outputs);
    }

    public SoulUrnRecipe(String oreName, Object... outputs) {
        super(oreName, outputs);
    }

    @Override
    public void onCraft(World world, int x, int y, int z, TileEntityFilteredHopper tile) {
        IFilteredHopper acc = (IFilteredHopper) tile;
        if (acc.getSoulsRetained() < acc.getMaxSoulsRetained()) tile.increaseSoulCount(1);
        world.playSoundEffect(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, "mob.ghast.scream", 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
        boolean isOn = ((BlockMechMachines) BWRegistry.singleMachines).isMechanicalOn(world, x, y, z);
        int bY = y - 1;
        Block blockBelow = world.getBlock(x, bY, z);
        if (acc.getSoulsRetained() > 0 && isOn && blockBelow instanceof ISoulSensitive && ((ISoulSensitive) blockBelow).isSoulSensitive(world, x, bY, z)) {
            int soulsConsumed = ((ISoulSensitive) blockBelow).processSouls(world, x, bY, z, acc.getSoulsRetained());
            int belowMeta = world.getBlockMetadata(x, bY, z);
            if (blockBelow == BWRegistry.planter && belowMeta > 9) {
                int meta = belowMeta - soulsConsumed;
                if (meta == 9) {
                    InvUtils.ejectStackWithOffset(world, x, bY, z, new ItemStack(BWRegistry.planter, 1, 9));
                    world.setBlockToAir(x, bY, z);
                } else {
                    world.setBlockMetadataWithNotify(x, bY, z, meta, 3);
                }
            }

            acc.setSoulsRetained(acc.getSoulsRetained() - soulsConsumed);
        } else if (acc.getSoulsRetained() > 7) {
            if (acc.trySpawnGhast()) {
                world.playSoundEffect(x + 0.5f, y + 0.5f, z + 0.5f, "mob.ghast.scream", 1.0F, world.rand.nextFloat() * 0.1F + 0.8F);
            }

            if (world.getBlock(x, y, z) == BWRegistry.singleMachines) {
                ((BlockMechMachines) world.getBlock(x, y, z)).breakHopper(world, x, y, z);
            }
        }
    }
}
