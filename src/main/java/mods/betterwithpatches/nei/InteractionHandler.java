package mods.betterwithpatches.nei;

import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.util.BWMRecipeAccessor;
import mods.betterwithpatches.util.BulkUtils;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public abstract class InteractionHandler extends TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
    public class CachedBWMRecipe extends CachedRecipe {
        public CachedBWMRecipe(List<PositionedStack> inputs, List<PositionedStack> outputs) {
            this.inputs = inputs;
            this.output = outputs;
        }
        public List<PositionedStack> inputs;
        public List<PositionedStack> output;

        @Override
        public List<PositionedStack> getIngredients() {
            return this.inputs;
        }

        @Override
        public PositionedStack getResult() {
            return output.get(0);
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            if (output.size() > 1) return output.subList(1, output.size());
            else return Collections.emptyList();
        }
    }

    public List<BulkRecipe> getRecipes() {
        return ((BWMRecipeAccessor) getManager()).getRecipes();
    }

    abstract public CraftingManagerBulk getManager();

    abstract public void create(BulkRecipe recipe);

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getOverlayIdentifier())) {
            for (BulkRecipe recipe : this.getRecipes()) create(recipe);
        } else if (outputId.equals("item")) loadCraftingRecipes((ItemStack) results[0]);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (BulkRecipe recipe : this.getRecipes()) {
            for (ItemStack output : recipe.getOutput()) {
                if (NEIServerUtils.areStacksSameType(result, output)) create(recipe);
                break;
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (BulkRecipe recipe : this.getRecipes()) {
            if (BulkUtils.matchInput(recipe.getInput(), ingredient)) create(recipe);
        }
    }

    public abstract int getX();
    public int getX(int offset) {
        return getX() + offset;
    }
    public abstract int getY();
    public int getY(int offset) {return getY() + offset;}

}
