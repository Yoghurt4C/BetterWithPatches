package mods.betterwithpatches.compat.nei.machines;

import betterwithmods.client.container.GuiMill;
import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.CraftingManagerMill;
import betterwithmods.craft.OreStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.compat.nei.BulkRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

public class MillRecipeHandler extends BulkRecipeHandler {

    @Override
    public int getX() {
        return 6;
    }

    @Override
    public int getY() {
        return 11;
    }

    @Override
    public void create(BulkRecipe recipe) {
        List<PositionedStack> in = new ArrayList<>();
        int x = getX(8), y = getY();
        for (Object input : recipe.getInput()) {
            if (input instanceof OreStack) {
                List<ItemStack> ores = ((OreStack) input).getOres();
                if (ores != null) in.add(new PositionedStack(ores, x, y, true));
            } else {
                in.add(new PositionedStack(input, x, y));
            }
            x += 18;
        }
        if (in.isEmpty()) return;

        x = getX(90);
        List<PositionedStack> out = new ArrayList<>();
        for (ItemStack stack : recipe.getOutput()) {
            out.add(new PositionedStack(stack, x, y));
            x += 18;
        }

        arecipes.add(new CachedBWMRecipe(in, out));
    }

    @Override
    public String getGuiTexture() {
        return "betterwithpatches:textures/gui/nei/mill.png";
    }

    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        drawTexturedModalRect(6, 3, 0, 0, 150, 33);
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(getX(68), getY(), 150, 0, 14, 14, 200, 1);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new MillRecipeHandler();
    }

    @Override
    public String getRecipeName() {
        return I18n.format("tile.bwm:tileMachine.0.name");
    }

    @Override
    public int recipiesPerPage() {
        return 2;
    }

    @Override
    public CraftingManagerBulk getManager() {
        return CraftingManagerMill.getInstance();
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getX(68), getY(), 14, 14);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiMill.class;
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.mill";
    }
}
