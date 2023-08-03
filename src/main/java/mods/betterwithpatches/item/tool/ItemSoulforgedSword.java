package mods.betterwithpatches.item.tool;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemSoulforgedSword extends ItemSword {
    public ItemSoulforgedSword() {
        super(BWPRegistry.SOULFORGED_TOOL);
    }

    @Override
    public boolean getIsRepairable(ItemStack tool, ItemStack material) {
        return BWPUtils.presentInOD(material, "ingotSoulforgedSteel");
    }
}
