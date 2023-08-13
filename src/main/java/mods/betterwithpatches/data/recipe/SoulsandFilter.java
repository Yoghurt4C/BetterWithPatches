package mods.betterwithpatches.data.recipe;

import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import mods.betterwithpatches.util.IFilteredHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;

public abstract class SoulsandFilter implements HopperFilter {
    @Override
    public boolean shouldHopperProcessItems(World world, int x, int y, int z, TileEntityFilteredHopper hopper, Entity entity) {
        IFilteredHopper acc = (IFilteredHopper) hopper;
        if (entity instanceof EntityXPOrb && !acc.isXPFull()) {
            EntityXPOrb orb = (EntityXPOrb) entity;
            int remaining = acc.getMaxExperienceCount() - acc.getExperienceCount();
            int value = orb.getXpValue();
            if (remaining > 0) {
                if (value <= remaining) {
                    acc.setExperienceCount(acc.getExperienceCount()+value);
                    orb.setDead();
                    return false;
                }
                orb.xpValue -= remaining;
                acc.setExperienceCount(acc.getMaxExperienceCount());
                return false;
            }
        }
        return true;
    }
}
