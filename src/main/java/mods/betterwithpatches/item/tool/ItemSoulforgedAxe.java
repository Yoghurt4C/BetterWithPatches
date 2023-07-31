package mods.betterwithpatches.item.tool;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemSoulforgedAxe extends ItemAxe {
    public ItemSoulforgedAxe() {
        super(BWPRegistry.SOULFORGED);
    }

    @Override
    public boolean getIsRepairable(ItemStack tool, ItemStack material) {
        return BWPUtils.presentInOD(material, "ingotSoulforgedSteel");
    }
    //todo hcenchanting
}
