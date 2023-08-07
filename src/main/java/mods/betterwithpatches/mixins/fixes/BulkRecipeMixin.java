package mods.betterwithpatches.mixins.fixes;

import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.OreStack;
import betterwithmods.items.ItemBark;
import betterwithmods.util.InvUtils;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.util.InvUtilsExtensions;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;

@Mixin(BulkRecipe.class)
public abstract class BulkRecipeMixin {
    @Shadow(remap = false)
    @Final
    private ArrayList<Object> recipeInput;

    /**
     * @reason This DOES NOT want to work as an inject. This could've been a single redirect, but the LVT won't allow it.
     * Basically, all these changes do is add CompoundTag checks to ItemStacks. If there's a way to make it work
     * without the machines infinitely procuring outputs from the first indexed recipe, I'd really like to see it.
     * <p>
     * It also breaks if you don't use fori. I don't even want to look at this class anymore.
     * @author Yoghurt4C
     */
    @Overwrite(remap = false)
    public boolean doesInvContainIngredients(IInventory inv) {
        if (this.recipeInput != null && this.recipeInput.size() > 0) {
            for (Object o : this.recipeInput) {
                if (o instanceof ItemStack) {
                    ItemStack stack = (ItemStack) o;
                    int stackSize = stack.stackSize;
                    int meta = stack.getItemDamage();
                    if (InvUtilsExtensions.countItemsWithTagsInInventory(inv, stack, meta) < stackSize) {
                        return false;
                    }
                } else if (o instanceof OreStack) {
                    OreStack stack = (OreStack) o;
                    if (InvUtils.countOresInInv(inv, stack.getOres()) < stack.getStackSize()) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @reason See above for decraption.
     * @author smile
     */
    @Overwrite(remap = false)
    public boolean consumeInvIngredients(IInventory inv) {
        boolean success = true;
        if (this.recipeInput.size() > 0) {
            for (Object o : this.recipeInput) {
                if (o instanceof ItemStack) {
                    ItemStack stack = (ItemStack) o;
                    int stackSize = stack.stackSize;
                    success = InvUtilsExtensions.consumeItemsWithTagsInInventory(inv, stack, stack.getItemDamage(), stackSize);
                } else if (o instanceof OreStack) {
                    OreStack stack = (OreStack) o;
                    success = InvUtils.consumeOresInInventory(inv, stack.getOres(), stack.getStackSize());
                }
            }
        }

        return success;
    }
}
