package mods.betterwithpatches.util;

import mods.betterwithpatches.data.recipe.HopperFilter;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public interface IFilteredHopper {
    int getMaxExperienceCount();

    int getMaxEjectedXP();

    int getDelayBetweenXPOrbs();

    int getExperienceCount();

    int getXPDropDelay();

    int getSoulsRetained();

    int getMaxSoulsRetained();

    HopperFilter getFilter();

    void setExperienceCount(int xp);

    void setSoulsRetained(int souls);

    AxisAlignedBB getCollectionZone();

    void setCollectionZone(AxisAlignedBB box);

    default boolean isXPFull() {
        return this.getExperienceCount() >= this.getMaxExperienceCount();
    }

    boolean trySpawnGhast();

    void onEntityCollidedWithHopper(World world, int x, int y, int z, Entity entity);
}
