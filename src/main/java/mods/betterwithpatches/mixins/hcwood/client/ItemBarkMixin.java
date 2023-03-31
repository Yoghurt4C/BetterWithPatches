package mods.betterwithpatches.mixins.hcwood.client;

import betterwithmods.items.ItemBark;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions.overrides;

@Mixin(ItemBark.class)
public abstract class ItemBarkMixin extends Item {
    /**
     * Redirecting to the block sprite atlas.
     */
    @Override
    public int getSpriteNumber() {
        return 0;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconFromTag(stack);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        return getIconFromTag(stack);
    }

    /**
     * Using Tag data to suss out the "source" Block for the Bark Item.
     */
    @Unique
    public IIcon getIconFromTag(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.stackTagCompound;
            String id = tag.getString("logId");
            int meta = tag.getInteger("logMeta");
            Block block = GameData.getBlockRegistry().getObject(id);
            return block.getIcon(2, meta);
        }
        return Blocks.log.getIcon(2, 0);
    }

    /**
     * @reason Filling the Creative Tab with every possible kind of Bark using NBT instead of hardcoded meta. This carries over to NEI if present.
     */
    @SuppressWarnings("all")
    @Inject(method = "getSubItems", at = @At("HEAD"), cancellable = true)
    public void nbtAware(Item item, CreativeTabs tab, List<ItemStack> list, CallbackInfo ctx) {
        ctx.cancel();
        int[] defaultMeta = new int[]{0, 1, 2, 3};
        for (ItemStack log : OreDictionary.getOres("logWood")) {
            String id = GameData.getBlockRegistry().getNameForObject(((ItemBlock) log.getItem()).field_150939_a);
            int[] iterable = overrides.getOrDefault(id, defaultMeta);
            for (int i : iterable) {
                ItemStack stack = new ItemStack(item, 1, 0);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("logId", id);
                tag.setInteger("logMeta", i);
                stack.setTagCompound(tag);
                list.add(stack);
            }
        }
    }

    /**
     * @reason A MISSINGNO is fine, too.
     */
    @Inject(method = "registerIcons", at = @At("HEAD"), cancellable = true)
    public void dontRegisterIcons(IIconRegister reg, CallbackInfo ctx) {
        ctx.cancel();
    }
}
