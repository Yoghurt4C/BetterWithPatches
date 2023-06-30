package mods.betterwithpatches.nei.machines;

import betterwithmods.BWRegistry;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.registry.GameData;
import mods.betterwithpatches.craft.TurntableInteractionExtensions;
import mods.betterwithpatches.nei.InteractionHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TurntableRecipeHandler extends InteractionHandler {
    private final ItemStack stack = new ItemStack(BWRegistry.singleMachines, 1, 5);

    @Override
    public Hashtable<String, ItemStack[]> getRecipes() {
        return TurntableInteractionExtensions.spinnables;
    }

    @Override
    public void create(String block, ItemStack[] output) {
        PositionedStack inputs;
        if (block.startsWith("ore:")) {
            String id = block.substring(4);
            inputs = new PositionedStack(OreDictionary.getOres(id), getX(21), getY(32), true);
        } else {
            String[] id = block.split("@");
            Block in = GameData.getBlockRegistry().getObject(id[0]);
            if (id.length > 1) {
                ItemStack input = new ItemStack(in, 1, Integer.parseInt(id[1]));
                inputs = new PositionedStack(input, getX(6), getY(10));
            } else {
                inputs = new PositionedStack(new ItemStack(in, 1, 32767), getX(6), getY(10), true);
            }
        }
        List<PositionedStack> outputs = new ArrayList<>();
        int x = getX(33), y = getY(10);
        for (int i = 0, outputLength = output.length; i < outputLength; i++) {
            ItemStack stack = output[i];
            outputs.add(new PositionedStack(stack, x, y));
            if (i > 1) x += 18;
            else y += 20;
        }
        arecipes.add(new CachedBWMRecipe(inputs, outputs));
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(getX(6), getY(30), 16, 16);
    }

    @Override
    public int getX() {
        return 45;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public String getGuiTexture() {
        return "betterwithpatches:textures/gui/nei/turntable.png";
    }

    @Override
    public String getRecipeName() {
        return I18n.format("tile.bwm:tileMachine.5.name");
    }

    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        GuiDraw.drawTexturedModalRect(getX(), getY(), 0, 0, 77, 51);
    }

    @Override
    public void drawExtras(int recipe) {
        RenderItem.getInstance().renderItemIntoGUI(GuiDraw.fontRenderer, GuiDraw.renderEngine, stack, getX(6), getY(30));
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.turntable";
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new TurntableRecipeHandler();
    }
}
