package mods.betterwithpatches.nei;

/*
import codechicken.nei.recipe.CatalystInfo;
import codechicken.nei.recipe.GuiRecipeTab;
import codechicken.nei.recipe.HandlerInfo;
import codechicken.nei.recipe.RecipeCatalysts;

public interface NEIGTNHCompat {

    static void addCompat(BWMRecipeHandler handler, ItemStack... catalysts) {
        //for (int i = 0, catalystsLength = catalysts.length; i < catalystsLength; i++) {
            // DOESN'T WORK
            // RecipeCatalysts.addRecipeCatalyst(handler.getHandlerId(), new CatalystInfo(catalysts[i], i));
        //}
        HandlerInfo info = new HandlerInfo(handler.getHandlerId(), BetterWithPatches.MODNAME, BetterWithPatches.MODID, true, null);
        info.setHandlerDimensions(HandlerInfo.DEFAULT_HEIGHT, HandlerInfo.DEFAULT_WIDTH, handler.recipiesPerPage());
        info.setItem("betterwithmods:singleMachine:0", null);
        GuiRecipeTab.handlerMap.put(handler.getHandlerId(), info);
    }
}


 */