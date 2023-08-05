package mods.betterwithpatches.item.tool;

import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemSoulforgedArmor extends ItemArmor {
    public final String texPath;

    public ItemSoulforgedArmor(int armorSlot) {
        super(BWPRegistry.SOULFORGED_ARMOR, 2, armorSlot);
        this.texPath = String.format("%s:textures/models/armor/steel_layer_%s.png", BWPConstants.MODID, this.armorType == 2 ? 2 : 1);
    }

    @Override
    public boolean getIsRepairable(ItemStack tool, ItemStack material) {
        return BWPUtils.presentInOD(material, "ingotSoulforgedSteel");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return this.texPath;
    }
}
