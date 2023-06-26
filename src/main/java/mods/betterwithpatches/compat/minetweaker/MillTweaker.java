package mods.betterwithpatches.compat.minetweaker;

import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.CraftingManagerMill;
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

@ZenClass("mods.betterwithmods.Mill")
public class MillTweaker {
    @ZenMethod
    public static void add(IItemStack[] outputs, IIngredient[] inputs) {
        MineTweakerAPI.apply(new Add(MineTweakerMC.getItemStacks(outputs), MTHelper.toBWMStacks(inputs)));
    }

    @ZenMethod
    public static void remove(IItemStack[] outputs, IIngredient[] inputs) {
        MineTweakerAPI.apply(new Remove(MineTweakerMC.getItemStacks(outputs), MTHelper.toBWMStacks(inputs)));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        ItemStack out = MineTweakerMC.getItemStack(output);
        List<BulkRecipe> recipes = MTHelper.copyBulkRecipeList(CraftingManagerMill.getInstance());
        for (BulkRecipe recipe : recipes) {
            if (recipe.getOutput().contains(out))
                MineTweakerAPI.apply(new Remove(recipe.getOutput().toArray(new ItemStack[0]), recipe.getInput().toArray()));
        }
    }

    @ZenMethod
    public static void removeAll() {
        List<BulkRecipe> recipes = MTHelper.copyBulkRecipeList(CraftingManagerMill.getInstance());
        for (BulkRecipe recipe : recipes) {
            MineTweakerAPI.apply(new Remove(recipe.getOutput().toArray(new ItemStack[0]), recipe.getInput().toArray()));
        }
    }

    private static class Add extends AddBulkAction {

        public Add(ItemStack[] outputs, Object[] inputs) {
            super(outputs, inputs);
        }

        @Override
        public CraftingManagerBulk getManager() {
            return CraftingManagerMill.getInstance();
        }

        @Override
        public String getHandlerName() {
            return "Mill";
        }
    }

    private static class Remove extends RemoveBulkAction {

        public Remove(ItemStack[] outputs, Object[] inputs) {
            super(outputs, inputs);
        }

        @Override
        public CraftingManagerBulk getManager() {
            return CraftingManagerMill.getInstance();
        }

        @Override
        public String getHandlerName() {
            return "Mill";
        }
    }
}
