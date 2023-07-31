package mods.betterwithpatches.compat.nei.machines;

import betterwithmods.craft.CraftingManagerBulk;
import betterwithmods.craft.CraftingManagerCrucible;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.resources.I18n;

public class CrucibleRecipeHandler extends CauldronRecipeHandler {

    @Override
    public TemplateRecipeHandler newInstance() {
        return new CrucibleRecipeHandler();
    }

    @Override
    public String getRecipeName() {
        return I18n.format("tile.bwm:tileMachine.2.name");
    }

    @Override
    public CraftingManagerBulk getManager() {
        return CraftingManagerCrucible.getInstance();
    }

    @Override
    public String getOverlayIdentifier() {
        return "bwm.crucible";
    }
}
