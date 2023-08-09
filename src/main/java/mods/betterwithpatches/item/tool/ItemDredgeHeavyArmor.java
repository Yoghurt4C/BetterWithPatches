package mods.betterwithpatches.item.tool;

import cpw.mods.fml.common.FMLCommonHandler;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.client.ModelDredgeHeavyArmor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemDredgeHeavyArmor extends ItemArmor implements MultiRenderPassArmor {
    public IIcon overlay;
    public static final int steelColor = 0x757575, leatherColor = 0x562A1A;

    public ItemDredgeHeavyArmor(int armorSlot) {
        super(BWPRegistry.DREDGE_HEAVY_ARMOR, 2, armorSlot);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            ModelDredgeHeavyArmor.models[armorSlot] = new ModelDredgeHeavyArmor(armorSlot);
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof BlockCauldron) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta > 0) {
                this.removeColor(stack);
                ((BlockCauldron) block).func_150024_a(world, x, y, z, meta - 1);
            }
        }
        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return ModelDredgeHeavyArmor.models[armorSlot];
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return type == null ? ModelDredgeHeavyArmor.texPath.toString() : ModelDredgeHeavyArmor.overPath.toString();
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
        return this.getColorForRenderPass(stack, pass);
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
        return this.getColorForRenderPass(stack, 0);
    }

    @Override
    public int getColorForRenderPass(ItemStack stack, int pass) {
        return this.hasColor(stack) ? this.getColorFromTag(stack, pass) : this.getDefaultColorForRenderPass(pass);
    }

    public int getColorFromTag(ItemStack stack, int pass) {
        NBTTagCompound tag = stack.getTagCompound().getCompoundTag("color");
        String key = String.valueOf(pass);
        return tag.hasKey(key) ? tag.getInteger(key) : this.getDefaultColorForRenderPass(pass);
    }

    @Override
    public void setColor(ItemStack stack, int pass, int color) {
        NBTTagCompound colors;
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("color")) {
            colors = new NBTTagCompound();
            stack.stackTagCompound.setTag("color", colors);
        } else {
            colors = stack.stackTagCompound.getCompoundTag("color");
        }

        colors.setInteger(String.valueOf(pass), color);
    }

    @Override
    public void removeColor(ItemStack stack) {
        if (hasColor(stack)) {
            stack.stackTagCompound.removeTag("color");
            if (stack.stackTagCompound.hasNoTags()) stack.stackTagCompound = null;
        }
    }

    @Override
    public void removeColor(ItemStack stack, int pass) {
        this.removeColor(stack);
    }
}
