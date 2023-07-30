package mods.betterwithpatches.menu;

import mods.betterwithpatches.block.tile.TileEntitySteelAnvil;
import mods.betterwithpatches.craft.anvil.SteelCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class MenuSteelAnvil extends Container {

    public final InventoryCrafting craftMatrix;
    public final IInventory craftResult;
    private final TileEntitySteelAnvil te;

    public MenuSteelAnvil(InventoryPlayer player, TileEntitySteelAnvil te) {
        this.te = te;
        this.craftMatrix = new InventorySteelCrafting(this, te);
        this.craftResult = new InventoryCraftResult();
        this.addSlotToContainer(new SlotCrafting(player.player, this.craftMatrix, this.craftResult, 0, 124, 44));

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                this.addSlotToContainer(new Slot(craftMatrix, j + i * 4, 12 + j * 18, 17 + i * 18));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 160));
        }

        this.onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory player) {
        this.craftResult.setInventorySlotContents(0, SteelCraftingManager.findMatchingRecipe(this.craftMatrix, this.te.getWorldObj()));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);
        final int invFirst = 17, invLast = 44, hotLast = 53;

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 16) {
                if (!this.mergeItemStack(itemstack1, invFirst, hotLast, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= invFirst && index < invLast) {
                if (!this.mergeItemStack(itemstack1, invLast, hotLast, false)) {
                    return null;
                }
            } else if (index >= invLast && index < hotLast) {
                if (!this.mergeItemStack(itemstack1, invFirst, invLast, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, invFirst, hotLast, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

}
