package mods.betterwithpatches.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.registry.GameData;
import mods.betterwithpatches.util.BWPNEIHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static codechicken.lib.gui.GuiDraw.changeTexture;

public abstract class InteractionHandler extends TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
    public class CachedBWMRecipe extends CachedRecipe {
        public CachedBWMRecipe(PositionedStack inputs, List<PositionedStack> outputs) {
            this.inputs = inputs;
            this.output = outputs;
        }

        public PositionedStack inputs;
        public List<PositionedStack> output;

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Collections.singletonList(inputs));
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

    public abstract Hashtable<String, ItemStack[]> getRecipes();

    abstract public void create(String block, ItemStack[] output);

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getOverlayIdentifier())) {
            for (Map.Entry<String, ItemStack[]> entry : this.getRecipes().entrySet()) {
                create(entry.getKey(), entry.getValue());
            }
        } else if (outputId.equals("item")) loadCraftingRecipes((ItemStack) results[0]);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Map.Entry<String, ItemStack[]> entry : this.getRecipes().entrySet()) {
            for (ItemStack itemStack : entry.getValue()) {
                if (NEIServerUtils.areStacksSameType(result, itemStack)) create(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Item item = ingredient.getItem();
        Block block;
        if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
        else block = Block.getBlockFromItem(item);
        String id = GameData.getBlockRegistry().getNameForObject(block);
        for (Map.Entry<String, ItemStack[]> entry : this.getRecipes().entrySet()) {
            if (BWPNEIHelper.matchInput(entry.getKey(), id)) create(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int recipiesPerPage() {
        return 2;
    }

    abstract public Rectangle getRect();

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(this.getRect(), this.getOverlayIdentifier()));
    }

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
