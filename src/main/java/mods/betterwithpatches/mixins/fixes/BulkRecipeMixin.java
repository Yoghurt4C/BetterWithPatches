package mods.betterwithpatches.mixins.fixes;

import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.OreStack;
import betterwithmods.util.InvUtils;
import mods.betterwithpatches.craft.InvUtilsExtensions;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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
            for (int i = 0; i < this.recipeInput.size(); ++i) {
                if (this.recipeInput.get(i) instanceof ItemStack) {
                    ItemStack stack = (ItemStack) this.recipeInput.get(i);
                    if (stack != null && InvUtilsExtensions.countItemsWithTagsInInventory(inv, stack, stack.getItemDamage()) < stack.stackSize) {
                        return false;
                    }
                } else if (this.recipeInput.get(i) instanceof OreStack) {
                    OreStack stack = (OreStack) this.recipeInput.get(i);
                    if (stack != null && InvUtils.countOresInInv(inv, stack.getOres()) < stack.getStackSize()) {
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
        if (this.recipeInput != null && this.recipeInput.size() > 0) {
            for (int i = 0; i < this.recipeInput.size(); ++i) {
                if (this.recipeInput.get(i) instanceof ItemStack) {
                    ItemStack stack = (ItemStack) this.recipeInput.get(i);
                    if (stack != null)
                        success = InvUtilsExtensions.consumeItemsWithTagsInInventory(inv, stack, stack.getItemDamage(), stack.stackSize);
                } else if (this.recipeInput.get(i) instanceof OreStack) {
                    OreStack stack = (OreStack) this.recipeInput.get(i);
                    if (stack != null)
                        success = InvUtils.consumeOresInInventory(inv, stack.getOres(), stack.getStackSize());
                }
            }
        }

        return success;
    }
}
