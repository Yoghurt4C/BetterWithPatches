package mods.betterwithpatches.menu;

import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventorySteelCrafting extends InventoryCrafting {
    public final TileEntitySteelAnvil tile;
    public final Container container;

    public InventorySteelCrafting(Container container, TileEntitySteelAnvil te) {
        super(container, 4, 4);
        this.tile = te;
        this.container = container;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= this.getSizeInventory() ? null : tile.getStackInSlot(slot);
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < 4) {
            int x = row + column * 4;
            return this.getStackInSlot(x);
        } else {
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        ItemStack stack = tile.getStackInSlot(slot);
        this.container.onCraftMatrixChanged(this);
        if (stack != null) {
            ItemStack itemstack;
            if (stack.stackSize <= decrement) {
                itemstack = stack.copy();
                stack = null;
                tile.setInventorySlotContents(slot, null);
            } else {
                itemstack = stack.splitStack(decrement);
                if (stack.stackSize == 0) {
                    stack = null;
                    tile.setInventorySlotContents(slot, null);
                }
            }
            this.container.onCraftMatrixChanged(this);
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        tile.setInventorySlotContents(slot, itemstack);
        this.container.onCraftMatrixChanged(this);
    }

}
