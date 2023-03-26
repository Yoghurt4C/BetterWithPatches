package mods.betterwithpatches.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.ItemStack;

import java.util.Hashtable;
import java.util.Map;

public abstract class InteractionHandler extends TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
    public class CachedBWMRecipe extends CachedRecipe {
        public CachedBWMRecipe(PositionedStack inputs, PositionedStack outputs) {
            this.inputs = inputs;
            this.output = outputs;
        }

        public PositionedStack inputs;
        public PositionedStack output;

        @Override
        public PositionedStack getIngredient() {
            return this.inputs;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }
    }

    public abstract Hashtable<String, ItemStack> getRecipes();

    abstract public void create(String block, ItemStack output);

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getOverlayIdentifier())) {
            for (Map.Entry<String, ItemStack> entry : this.getRecipes().entrySet()) {
                create(entry.getKey(), entry.getValue());
            }
        } else if (outputId.equals("item")) loadCraftingRecipes((ItemStack) results[0]);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Map.Entry<String, ItemStack> entry : this.getRecipes().entrySet()) {
            if (NEIServerUtils.areStacksSameType(result, entry.getValue())) create(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Map.Entry<String, ItemStack> entry : this.getRecipes().entrySet()) {

            //if
            //if (NEIServerUtils.areStacksSameType(ingredient, )) create(entry.getKey(), entry.getValue());
        }
    }

    public abstract int getX();

    public int getX(int offset) {
        return getX() + offset;
    }

    public abstract int getY();

    public int getY(int offset) {
        return getY() + offset;
    }

}
