package mods.betterwithpatches.nei.machines;

import betterwithmods.client.container.GuiCookingPot;
import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.CraftingManagerCauldron;
import betterwithmods.craft.OreStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.nei.BulkRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

public class CauldronRecipeHandler extends BulkRecipeHandler {
    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void create(BulkRecipe recipe) {
        List<PositionedStack> in = new ArrayList<>();
        int x = 8, y = 3;
        List<Object> recipeInput = recipe.getInput();
        for (int i = 0, recipeInputSize = recipeInput.size(); i < recipeInputSize; i++) {
            Object input = recipeInput.get(i);
            if (input instanceof OreStack) {
                List<ItemStack> ores = ((OreStack) input).getOres();
                if (ores != null) in.add(new PositionedStack(ores, x, y, true));
            } else {
                in.add(new PositionedStack(input, x, y));
            }
            if ((i + 1) % 3 == 0) {
                x = 8;
                y += 18;
            } else {
                x += 18;
            }
        }
        if (in.isEmpty()) return;

        x = getX(106);
        y = 3;
        List<PositionedStack> out = new ArrayList<>();
        List<ItemStack> output = recipe.getOutput();
        for (int i = 0, outputSize = output.size(); i < outputSize; i++) {
            ItemStack stack = output.get(i);
            out.add(new PositionedStack(stack, x, y));
            if ((i + 1) % 3 == 0) {
                x = 8;
                y += 18;
            } else {
                x += 18;
            }

        }

        arecipes.add(new CachedBWMRecipe(in, out));
    }

    @Override
    public String getGuiTexture() {
        return "betterwithpatches:textures/gui/nei/cooking.png";
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(0, 0, 0, 0, 166, 58);
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(getX(76), getY(21), 166, 0, 14, 14, 200, 3);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new CauldronRecipeHandler();
    }

    @Override
    public String getRecipeName() {
        return I18n.format("tile.bwm:tileMachine.3.name");
    }

    @Override
    public int recipiesPerPage() {
        return 2;
    }

    @Override
    public CraftingManagerBulk getManager() {
        return CraftingManagerCauldron.getInstance();
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(getX(76), getY(21), 14, 14), this.getOverlayIdentifier()));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCookingPot.class;
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.cauldron";
    }
}
