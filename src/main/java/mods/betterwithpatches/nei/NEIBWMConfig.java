package mods.betterwithpatches.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mods.betterwithpatches.nei.machines.*;
import net.minecraft.item.ItemStack;

public class NEIBWMConfig implements IConfigureNEI {
    public static boolean isAdded = true;

    @Override
    public void loadConfig() {
        isAdded = false;
        this.addHandler(new MillRecipeHandler()/*, new ItemStack(BWRegistry.singleMachines, 1, 0)*/);
        this.addHandler(new CauldronRecipeHandler());
        this.addHandler(new StokedCauldronRecipeHandler());
        this.addHandler(new CrucibleRecipeHandler());
        this.addHandler(new StokedCrucibleRecipeHandler());
        this.addHandler(new KilnRecipeHandler());
        isAdded = true;
    }

    private void addHandler(TemplateRecipeHandler handler, ItemStack... catalysts) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
        //ModContainer nei = Loader.instance().getIndexedModList().get("NotEnoughItems");
        //if (nei.getVersion().endsWith("GTNH")) NEIGTNHCompat.addCompat(handler, catalysts);
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
