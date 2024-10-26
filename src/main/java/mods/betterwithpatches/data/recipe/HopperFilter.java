package mods.betterwithpatches.data.recipe;

import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Predicate;

public interface HopperFilter extends Predicate<ItemStack> {
    default boolean shouldHopperProcessItems(World world, int x, int y, int z, TileEntityFilteredHopper tile, Entity entity) {
        return true;
    }
}
