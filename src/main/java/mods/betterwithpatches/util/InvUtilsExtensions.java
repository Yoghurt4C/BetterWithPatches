package mods.betterwithpatches.util;

import betterwithmods.items.ItemBark;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import static betterwithmods.util.InvUtils.decrStackSize;

public interface InvUtilsExtensions {

    @SuppressWarnings("unchecked")
    static List<ItemStack> getMatchingSuffix(String ore, String prefix, String target) {
        String replaced = ore.replace(prefix, target);
        if (OreDictionary.doesOreNameExist(replaced)) return OreDictionary.getOres(replaced, false);
        return Collections.EMPTY_LIST;
    }


    static int countItemsWithTagsInInventory(IInventory inv, ItemStack stack, int meta) {
        int itemCount = 0;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (invStack != null && invStack.getItem() == stack.getItem() && (meta == 32767 || invStack.getItemDamage() == meta)) {
                if (compareTags(stack, invStack)) {
                    itemCount += inv.getStackInSlot(i).stackSize;
                }
            }
        }
        return itemCount;
    }

    static void consumeItemsWithTagsInInventory(IInventory inv, ItemStack stack, int meta, int stackSize) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (invStack != null && invStack.getItem() == stack.getItem() && (meta == 32767 || invStack.getItemDamage() == meta)) {
                if (invStack.stackSize >= stackSize && compareTags(stack, invStack)) {
                    decrStackSize(inv, i, stackSize);
                    return;
                }

                stackSize -= stack.stackSize;
                inv.setInventorySlotContents(i, null);
            }
        }
    }


    /**
     * @return false if there's enough bark in the inventory for a recipe, true if the recipe matching fails.
     */
    static boolean countBarkInInventory(IInventory inv) {
        Hashtable<String, Integer> cache = new Hashtable<>();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (invStack != null && invStack.getItem() instanceof ItemBark && invStack.hasTagCompound()) {
                NBTTagCompound tag = invStack.stackTagCompound;
                String id = tag.getString("logId");
                int bMeta = tag.getInteger("logMeta");
                String joint = id + bMeta;
                int has = cache.containsKey(joint) ? cache.get(joint) + invStack.stackSize : invStack.stackSize;
                int expected = HardcoreWoodInteractionExtensions.getTanninAmount(id, bMeta);
                if (has < expected) {
                    cache.put(joint, has);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    static void consumeBarkInInventory(IInventory inv) {
        Hashtable<String, Integer> cache = new Hashtable<>();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (invStack != null && invStack.getItem() instanceof ItemBark && invStack.hasTagCompound()) {
                NBTTagCompound tag = invStack.stackTagCompound;
                String id = tag.getString("logId");
                int bMeta = tag.getInteger("logMeta");
                String joint = id + bMeta;
                int stackSize = cache.getOrDefault(joint, HardcoreWoodInteractionExtensions.getTanninAmount(id, bMeta));
                if (invStack.stackSize < stackSize) {
                    stackSize -= invStack.stackSize;
                    inv.setInventorySlotContents(i, null);
                    cache.put(id + bMeta, stackSize);
                } else {
                    decrStackSize(inv, i, stackSize);
                    return;
                }
            }
        }
    }


    static boolean compareTags(ItemStack ingredient, ItemStack invStack) {
        boolean ing = ingredient.hasTagCompound();
        boolean inv = invStack.hasTagCompound();
        if (inv && ing) {
            for (String s : ingredient.stackTagCompound.func_150296_c()) {
                if (!invStack.stackTagCompound.hasKey(s) || ingredient.stackTagCompound.getTag(s).hashCode() != invStack.stackTagCompound.getTag(s).hashCode())
                    return false;
            }
            return true;
        } else return !ing;
    }
}
