package mods.betterwithpatches.item.tool;

import cpw.mods.fml.common.FMLCommonHandler;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.client.DredgeHeavyArmorRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemDredgeHeavyArmor extends ItemArmor implements MultiRenderPassArmor {
    public IIcon overlay;
    public final ModelBiped model;
    public static final int steelColor = 0x757575, leatherColor = 0x562A1A;

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
        return type == null ? DredgeHeavyArmorRenderer.texPath.toString() : DredgeHeavyArmorRenderer.overPath.toString();
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(this.iconString);
        this.overlay = register.registerIcon(this.iconString + "Overlay");
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
        return pass == 1 ? this.overlay : this.itemIcon;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return getColorForRenderPass(stack, pass);
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("color");
    }

    @Override
    public int getDefaultColorForRenderPass(int pass) {
        return pass == 1 ? leatherColor : steelColor;
    }

    @Override
    public int getColor(ItemStack stack) {
        return getColorForRenderPass(stack, 0);
    }

    @Override
    public int getColorForRenderPass(ItemStack stack, int pass) {
        if (hasColor(stack)) {
            return pass == 1 ? getColorFromTag(stack, "leather") : getColorFromTag(stack, "steel");
        } else return getDefaultColorForRenderPass(pass);
    }

    @Override
    public int getColorFromTag(ItemStack stack, String color) {
        return stack.getTagCompound().getCompoundTag("color").getInteger(color);
    }
}
