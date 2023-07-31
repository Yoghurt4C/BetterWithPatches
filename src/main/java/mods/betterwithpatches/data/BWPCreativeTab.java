package mods.betterwithpatches.data;

import betterwithmods.BWRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BWPCreativeTab extends CreativeTabs {
    public ItemStack gear;

    public BWPCreativeTab() {
        super("betterwithmods");
    }

    @Override
    public ItemStack getIconItemStack() {
        if (this.gear == null) this.gear = new ItemStack(BWRegistry.material, 1, 0);
        return this.gear;
    }

    @Override
    public Item getTabIconItem() {
        return BWRegistry.donut;
    }
}
