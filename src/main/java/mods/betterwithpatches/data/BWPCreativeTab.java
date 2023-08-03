package mods.betterwithpatches.data;

import betterwithmods.BWRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BWPCreativeTab extends CreativeTabs {
    public ItemStack windmill;

    public BWPCreativeTab() {
        super("betterwithmods");
    }

    @Override
    public ItemStack getIconItemStack() {
        if (this.windmill == null) this.windmill = new ItemStack(BWRegistry.windmill, 1, 0);
        return this.windmill;
    }

    @Override
    public Item getTabIconItem() {
        return BWRegistry.windmill;
    }
}
