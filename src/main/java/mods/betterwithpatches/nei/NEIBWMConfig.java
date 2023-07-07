package mods.betterwithpatches.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.nei.machines.*;

public class NEIBWMConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        this.addHandler(new MillRecipeHandler());
        this.addHandler(new CauldronRecipeHandler());
        this.addHandler(new StokedCauldronRecipeHandler());
        this.addHandler(new CrucibleRecipeHandler());
        this.addHandler(new StokedCrucibleRecipeHandler());
        this.addHandler(new KilnRecipeHandler());
        this.addHandler(new TurntableRecipeHandler());
    }

    private void addHandler(TemplateRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }

    @Override
    public String getName() {
        return "BetterWithPatches-NEI";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
