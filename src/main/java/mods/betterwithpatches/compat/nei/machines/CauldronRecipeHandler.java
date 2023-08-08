package mods.betterwithpatches.compat.nei.machines;

import betterwithmods.BWRegistry;
import betterwithmods.craft.BulkRecipe;
import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.CraftingManagerCauldron;
import betterwithmods.craft.OreStack;
import betterwithmods.items.ItemBark;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.compat.nei.BulkRecipeHandler;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void create(BulkRecipe recipe, ItemStack usageStack) {
        List<PositionedStack> in = new ArrayList<>();
        int x = 8, y = 3;
        List<Object> recipeInput = recipe.getInput();
        for (int i = 0, recipeInputSize = recipeInput.size(); i < recipeInputSize; i++) {
            Object input = recipeInput.get(i);
            if (input instanceof OreStack) {
                List<ItemStack> ores = ((OreStack) input).getOres();
                if (ores != null) {
                    ores = new ArrayList<>(ores);
                    for (ItemStack ore : ores) {
                        ore.stackSize = ((OreStack) input).getStackSize();
                    }
                    in.add(new PositionedStack(ores, x, y, true));
                }
            } else if (input instanceof ItemStack && ((ItemStack) input).getItem() instanceof ItemBark) {
                List<ItemStack> bark = new ArrayList<>();
                if (usageStack != null && usageStack.getItem() instanceof ItemBark && usageStack.hasTagCompound()) {
                    ItemStack barkStack = usageStack.copy();
                    barkStack.stackSize = HardcoreWoodInteractionExtensions.getTanninAmount(usageStack);
                    bark.add(barkStack);
                } else {
                    for (Map.Entry<String, int[]> pair : HardcoreWoodInteractionExtensions.displayMap.entrySet()) {
                        for (int meta : pair.getValue()) {
                            ItemStack barkStack = new ItemStack(BWRegistry.bark, HardcoreWoodInteractionExtensions.getTanninAmount(pair.getKey(), meta));
                            NBTTagCompound tag = new NBTTagCompound();
                            tag.setString("logId", pair.getKey());
                            tag.setInteger("logMeta", meta);
                            barkStack.setTagCompound(tag);
                            bark.add(barkStack);
                        }
                    }
                }
                in.add(new PositionedStack(bark, x, y));
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
    public Rectangle getRect() {
        return new Rectangle(getX(76), getY(21), 14, 14);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        /*return GuiCookingPot.class;*/
        return null;
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.cauldron";
    }
}
