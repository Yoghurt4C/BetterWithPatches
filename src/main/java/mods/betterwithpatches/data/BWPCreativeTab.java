package mods.betterwithpatches.data;

import betterwithmods.BWRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BWPCreativeTab extends CreativeTabs {
    public final ItemStack gear = new ItemStack(BWRegistry.material, 1, 0);

    public BWPCreativeTab() {
        super("betterwithmods");
    }

    @Override
    public ItemStack getIconItemStack() {
        return this.gear;
    }

    @Override
    public Item getTabIconItem() {
        return BWRegistry.donut;
    }
}
