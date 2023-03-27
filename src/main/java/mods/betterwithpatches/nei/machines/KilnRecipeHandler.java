package mods.betterwithpatches.nei.machines;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.registry.GameData;
import mods.betterwithpatches.kiln.KilnInteractionExtensions;
import mods.betterwithpatches.nei.InteractionHandler;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class KilnRecipeHandler extends InteractionHandler {
    @Override
    public Hashtable<String, ItemStack[]> getRecipes() {
        return KilnInteractionExtensions.cookables;
    }

    @Override
    public void create(String block, ItemStack[] output) {
        String[] id = block.split("@");
        PositionedStack inputs;
        Block in = GameData.getBlockRegistry().getObject(id[0]);
        if (id.length > 1) {
            ItemStack input = new ItemStack(in, 1, Integer.parseInt(id[1]));
            inputs = new PositionedStack(input, getX(21), getY(32));
        } else {
            inputs = new PositionedStack(new ItemStack(in, 1, 32767), getX(21), getY(32), true);
        }
        List<PositionedStack> outputs = new ArrayList<>();
        int x = getX(87);
        for (ItemStack stack : output) {
            outputs.add(new PositionedStack(stack, x, getY(32)));
            x += 18;
        }
        arecipes.add(new CachedBWMRecipe(inputs, outputs));
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getX(59), getY(32), 22, 15);
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.kiln";
    }

    @Override
    public int getX() {
        return 11;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        GuiDraw.drawTexturedModalRect(getX(), getY(), 0, 0, 145, 80);
    }

    @Override
    public String getGuiTexture() {
        return "betterwithpatches:textures/gui/nei/kiln.png";
    }

    @Override
    public String getRecipeName() {
        return I18n.format("tile.bwm:kiln.name");
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new KilnRecipeHandler();
    }
}
