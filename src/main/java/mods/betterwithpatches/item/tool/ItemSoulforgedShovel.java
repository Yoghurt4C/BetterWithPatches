package mods.betterwithpatches.item.tool;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class ItemSoulforgedShovel extends ItemSpade {
    public ItemSoulforgedShovel() {
        super(BWPRegistry.SOULFORGED_TOOL);
    }

    @Override
    public boolean getIsRepairable(ItemStack tool, ItemStack material) {
        return BWPUtils.presentInOD(material, "ingotSoulforgedSteel");
    }
}
