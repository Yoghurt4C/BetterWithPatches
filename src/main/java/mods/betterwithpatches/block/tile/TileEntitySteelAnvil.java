package mods.betterwithpatches.block.tile;

import betterwithmods.util.InvUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySteelAnvil extends TileEntity implements IInventory {
    public ItemStack[] internal = new ItemStack[16];

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound inv = new NBTTagCompound();
        for (int i = 0; i < internal.length; i++) {
            ItemStack stack = internal[i];
            if (stack != null) inv.setTag(String.valueOf(i), stack.writeToNBT(new NBTTagCompound()));
        }
        compound.setTag("inventory", inv);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound inv = compound.getCompoundTag("inventory");
        for (int i = 0; i < internal.length; i++) {
            String s = String.valueOf(i);
            if (inv.hasKey(s)) {
                internal[i] = ItemStack.loadItemStackFromNBT(inv.getCompoundTag(s));
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return this.internal.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.internal[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        return InvUtils.decrStackSize(this, slot, count);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.internal[slot] != null) {
            ItemStack stack = this.internal[slot];
            this.internal[slot] = null;
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.internal[slot] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "tile.bwm:steelAnvil.name";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }
}
