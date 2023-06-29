package mods.betterwithpatches.compat.minetweaker;

import betterwithmods.craft.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import mods.betterwithpatches.compat.minetweaker.util.AddBulkAction;
import mods.betterwithpatches.compat.minetweaker.util.MTHelper;
import mods.betterwithpatches.compat.minetweaker.util.RemoveBulkAction;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

//basically complete copy of cauldrontweaker because I have no faith in zenscripts ability to cope with inheritance
@ZenClass("mods.betterwithmods.Crucible")
public class CrucibleTweaker {

    @ZenMethod
    public static void addUnstoked(IItemStack[] outputs, IIngredient[] inputs) {
        MineTweakerAPI.apply(new Add(outputs, inputs, false));
    }

    @ZenMethod
    public static void addStoked(IItemStack[] outputs, IIngredient[] inputs) {
        MineTweakerAPI.apply(new Add(outputs, inputs, true));
    }

    @ZenMethod
    public static void remove(IItemStack output, boolean stoked) {
        ItemStack out = MineTweakerMC.getItemStack(output);
        List<BulkRecipe> recipes = MTHelper.copyBulkRecipeList(stoked ? CraftingManagerCrucibleStoked.getInstance() : CraftingManagerCrucible.getInstance());
        for (BulkRecipe recipe : recipes) {
            for (ItemStack stack : recipe.getOutput()) {
                if (MTHelper.stacksMatch(stack, out)) {
                    MineTweakerAPI.apply(new Remove(recipe.getOutput().toArray(new ItemStack[0]), recipe.getInput().toArray(), stoked));
                    break;
                }
            }
        }
    }

    @ZenMethod
    public static void removeUnstoked(IItemStack[] outputs, IIngredient[] inputs) {
        MineTweakerAPI.apply(new Remove(outputs, inputs, false));
    }

    @ZenMethod
    public static void removeStoked(IItemStack[] outputs, IIngredient[] inputs) {
        MineTweakerAPI.apply(new Remove(outputs, inputs, true));
    }

    @ZenMethod
    public static void removeAll(boolean stoked) {
        List<BulkRecipe> recipes = MTHelper.copyBulkRecipeList(stoked ? CraftingManagerCrucibleStoked.getInstance() : CraftingManagerCrucible.getInstance());
        for (BulkRecipe recipe : recipes) {
            MineTweakerAPI.apply(new Remove(recipe.getOutput().toArray(new ItemStack[0]), recipe.getInput().toArray(), stoked));
        }
    }

    private static class Add extends AddBulkAction {
        public final boolean stoked;

        public Add(IItemStack[] outputs, IIngredient[] inputs, boolean stoked) {
            super(MineTweakerMC.getItemStacks(outputs), MTHelper.toBWMStacks(inputs));
            this.stoked = stoked;
        }

        @Override
        public CraftingManagerBulk getManager() {
            return this.stoked ? CraftingManagerCrucibleStoked.getInstance() : CraftingManagerCrucible.getInstance();
        }

        @Override
        public String getHandlerName() {
            return this.stoked ? "Stoked Crucible" : "Crucible";
        }
    }

    private static class Remove extends RemoveBulkAction {
        public final boolean stoked;

        public Remove(IItemStack[] outputs, IIngredient[] inputs, boolean stoked) {
            super(MineTweakerMC.getItemStacks(outputs), MTHelper.toBWMStacks(inputs));
            this.stoked = stoked;
        }

        public Remove(ItemStack[] outputs, Object[] inputs, boolean stoked) {
            super(outputs, inputs);
            this.stoked = stoked;
        }

        @Override
        public CraftingManagerBulk getManager() {
            return this.stoked ? CraftingManagerCauldronStoked.getInstance() : CraftingManagerCauldron.getInstance();
        }

        @Override
        public String getHandlerName() {
            return this.stoked ? "Stoked Crucible" : "Crucible";
        }
    }
}
