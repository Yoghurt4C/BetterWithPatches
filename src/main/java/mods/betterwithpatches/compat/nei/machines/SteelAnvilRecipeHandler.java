package mods.betterwithpatches.compat.nei.machines;

import betterwithmods.craft.OreStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.craft.anvil.ShapedSteelRecipe;
import mods.betterwithpatches.craft.anvil.ShapelessSteelRecipe;
import mods.betterwithpatches.util.BWPNEIHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;
import static mods.betterwithpatches.craft.SteelCraftingManager.RECIPES;

public class SteelAnvilRecipeHandler extends TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
    public class CachedBWMRecipe extends CachedRecipe {
        public final List<PositionedStack> inputs;
        public final PositionedStack output;

        public CachedBWMRecipe(List<PositionedStack> inputs, PositionedStack output) {
            this.inputs = inputs;
            this.output = output;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, this.inputs);
        }

        @Override
        public PositionedStack getResult() {
            return this.output;
        }
    }

    public void create(IRecipe recipe) {
        Object[] ingredients;
        int x = 0, y = 0, width = 4;
        List<PositionedStack> stacks = new ArrayList<>();
        if (recipe instanceof ShapedSteelRecipe) {
            ingredients = ((ShapedSteelRecipe) recipe).ingredients;
            width = ((ShapedSteelRecipe) recipe).width;
        } else if (recipe instanceof ShapelessSteelRecipe) {
            ingredients = ((ShapelessSteelRecipe) recipe).ingredients;
        } else ingredients = new Object[0];
        for (Object ingr : ingredients) {
            if (ingr instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) ingr;
                stacks.add(new PositionedStack(itemStack, 18 * x + getX(1), 18 * y + getY(1), false));
            } else if (ingr instanceof OreStack) {
                OreStack oreStack = (OreStack) ingr;
                stacks.add(new PositionedStack(oreStack.getOres(), 18 * x + getX(1), 18 * y + getY(1)));
            }
            x++;
            if (x == width) {
                x = 0;
                y++;
            }
        }
        PositionedStack output = new PositionedStack(recipe.getRecipeOutput(), getX(113), getY(28), false);
        this.arecipes.add(new CachedBWMRecipe(stacks, output));
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(getX(79), getY(28), 22, 15), this.getOverlayIdentifier()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getOverlayIdentifier())) {
            for (IRecipe recipe : RECIPES) this.create(recipe);
        } else if (outputId.equals("item")) {
            this.loadCraftingRecipes((ItemStack) results[0]);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe recipe : RECIPES) {
            if (NEIServerUtils.areStacksSameType(result, recipe.getRecipeOutput())) create(recipe);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe recipe : RECIPES) {
            if (recipe instanceof ShapedSteelRecipe && BWPNEIHelper.matchInput(Arrays.asList(((ShapedSteelRecipe) recipe).ingredients), ingredient))
                this.create(recipe);
        }
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.anvil";
    }

    @Override
    public String getGuiTexture() {
        return "betterwithpatches:textures/gui/nei/steel_anvil.png";
    }

    @Override
    public String getRecipeName() {
        return I18n.format("tile.bwm:steelAnvil.name");
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(16, 0, 0, 0, 134, 72);
    }

    public int getX() {
        return 16;
    }

    public int getY() {
        return 0;
    }

    public int getX(int offset) {
        return getX() + offset;
    }

    public int getY(int offset) {
        return getY() + offset;
    }
}
