package mods.betterwithpatches.item.tool;

import com.google.common.collect.ImmutableSet;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class ItemSoulforgedPickaxe extends ItemPickaxe {
    public ItemSoulforgedPickaxe() {
        super(BWPRegistry.SOULFORGED);
    }

    @Override
    public boolean getIsRepairable(ItemStack tool, ItemStack material) {
        return BWPUtils.presentInOD(material, "ingotSoulforgedSteel");
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe");
    }
}
