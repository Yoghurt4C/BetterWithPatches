package mods.betterwithpatches.item.tool;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class ItemSoulforgedHoe extends ItemHoe {
    public ItemSoulforgedHoe() {
        super(BWPRegistry.SOULFORGED);
    }

    @Override
    public boolean getIsRepairable(ItemStack tool, ItemStack material) {
        return BWPUtils.presentInOD(material, "ingotSoulforgedSteel");
    }
}
