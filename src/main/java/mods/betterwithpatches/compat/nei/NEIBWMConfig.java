package mods.betterwithpatches.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import mods.betterwithpatches.compat.nei.machines.*;
import net.minecraft.nbt.NBTTagCompound;

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
        this.addHandler(new SteelAnvilRecipeHandler());
    }

    private void addHandler(TemplateRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }

    public void sendIMC(String handler, String id, String itemName, int recipies) {
        handler = "mods.betterwithpatches.compat.nei.machines." + handler;
        NBTTagCompound imc = new NBTTagCompound();
        imc.setString("handler", handler);
        imc.setString("handlerID", handler);
        imc.setString("modName", "Better With Mods");
        imc.setString("modId", "betterwithmods");
        imc.setBoolean("modRequired", true);
        imc.setString("itemName", itemName);
        imc.setInteger("yShift", 0);
        imc.setInteger("handlerHeight", 65);
        imc.setInteger("handlerWidth", 166);
        imc.setInteger("maxRecipesPerPage", recipies);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", imc);
        imc.setString("handlerID", id);
        imc.setString("catalystHandlerID", id);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", imc);
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
