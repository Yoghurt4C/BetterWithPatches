package mods.betterwithpatches.item.tool;

import cpw.mods.fml.common.FMLCommonHandler;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.client.DredgeHeavyArmorRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemDredgeHeavyArmor extends ItemArmor {
    public final ModelBiped model;
    public ItemDredgeHeavyArmor(int armorSlot) {
        super(BWPRegistry.SOULFORGED_ARMOR, 2, armorSlot);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.model = new DredgeHeavyArmorRenderer(armorSlot);
        } else {
            this.model = null;
        }
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return this.model;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return DredgeHeavyArmorRenderer.texPath.toString();
    }
    /*

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        return false;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("color")) {
            NBTTagCompound color = stack.getTagCompound().getCompoundTag("color");
            return pass == 1 ? color.getInteger("leather") : color.getInteger("steel");
        }
        return 16777215;
    }

    @Override
    public int getColor(ItemStack p_82814_1_) {
        return 16777215;
    }
     */
}
