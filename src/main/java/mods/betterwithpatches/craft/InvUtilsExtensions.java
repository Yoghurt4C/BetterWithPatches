package mods.betterwithpatches.craft;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import static betterwithmods.util.InvUtils.decrStackSize;

public interface InvUtilsExtensions {

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

    static boolean consumeItemsWithTagsInInventory(IInventory inv, ItemStack stack, int meta, int stackSize) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack invStack = inv.getStackInSlot(i);
            if (invStack != null && invStack.getItem() == stack.getItem() && (meta == 32767 || invStack.getItemDamage() == meta)) {
                if (invStack.stackSize >= stack.stackSize && compareTags(stack, invStack)) {
                    decrStackSize(inv, i, stack.stackSize);
                    return false;
                }

                stackSize -= stack.stackSize;
                inv.setInventorySlotContents(i, null);
            }
        }

        return false;
    }


    static boolean compareTags(ItemStack ingredient, ItemStack invStack) {
        boolean ing = ingredient.hasTagCompound();
        boolean inv = invStack.hasTagCompound();
        if (inv && ing) {
            for (String s : ingredient.stackTagCompound.func_150296_c()) {
                if (ingredient.stackTagCompound.getTag(s).hashCode() != invStack.stackTagCompound.getTag(s).hashCode())
                    return false;
            }
            return true;
        } else return !ing;
    }
}
