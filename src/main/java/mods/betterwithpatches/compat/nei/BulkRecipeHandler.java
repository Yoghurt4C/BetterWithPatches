package mods.betterwithpatches.compat.nei;

import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.util.BWMRecipeAccessor;
import mods.betterwithpatches.util.BWPNEIHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.changeTexture;

public abstract class BulkRecipeHandler extends TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
    public class CachedBWMRecipe extends CachedRecipe {
        public CachedBWMRecipe(List<PositionedStack> inputs, List<PositionedStack> outputs) {
            this.inputs = inputs;
            this.output = outputs;
        }

        public List<PositionedStack> inputs;
        public List<PositionedStack> output;

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, inputs);
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

    abstract public void create(BulkRecipe recipe, ItemStack usageStack);

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getOverlayIdentifier())) {
            for (BulkRecipe recipe : this.getRecipes()) create(recipe, null);
        } else if (outputId.equals("item")) {
            for (Object result : results) {
                loadCraftingRecipes((ItemStack) result);
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (BulkRecipe recipe : this.getRecipes()) {
            for (ItemStack output : recipe.getOutput()) {
                if (NEIServerUtils.areStacksSameType(result, output)) create(recipe, null);
                break;
            }
        }
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals(this.getOverlayIdentifier())) {
            for (BulkRecipe recipe : this.getRecipes()) create(recipe, null);
        } else if (inputId.equals("item"))
            for (Object ingredient : ingredients) {
                loadUsageRecipes((ItemStack) ingredient);
            }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (BulkRecipe recipe : this.getRecipes()) {
            if (BWPNEIHelper.matchInput(recipe.getInput(), ingredient)) create(recipe, ingredient);
        }
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(this.getRect(), this.getOverlayIdentifier()));
    }

    public abstract Rectangle getRect();

    public abstract int getX();

    public int getX(int offset) {
        return getX() + offset;
    }

    public abstract int getY();

    public int getY(int offset) {
        return getY() + offset;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
    }
}
