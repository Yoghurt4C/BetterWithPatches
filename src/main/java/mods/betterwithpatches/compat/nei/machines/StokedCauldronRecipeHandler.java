package mods.betterwithpatches.compat.nei.machines;

import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.CraftingManagerCauldronStoked;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.resources.I18n;

public class StokedCauldronRecipeHandler extends CauldronRecipeHandler {
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(getX(76), getY(21), 166, 14, 14, 14, 200, 3);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new StokedCauldronRecipeHandler();
    }

    @Override
    public String getRecipeName() {
        return I18n.format("text.bwp:cauldronStoked") + " " + super.getRecipeName();
    }

    @Override
    public CraftingManagerBulk getManager() {
        return CraftingManagerCauldronStoked.getInstance();
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.cauldronStoked";
    }
}
