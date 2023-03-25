package mods.betterwithpatches.nei.machines;

import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.CraftingManagerCrucibleStoked;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.resources.I18n;

public class StokedCrucibleRecipeHandler extends StokedCauldronRecipeHandler {

    @Override
    public TemplateRecipeHandler newInstance() {
        return new StokedCrucibleRecipeHandler();
    }

    @Override
    public String getRecipeName() {
        return I18n.format("text.bwp:cauldronStoked") + " " + I18n.format("tile.bwm:tileMachine.2.name");
    }

    @Override
    public CraftingManagerBulk getManager() {
        return CraftingManagerCrucibleStoked.getInstance();
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.crucibleStoked";
    }
}
