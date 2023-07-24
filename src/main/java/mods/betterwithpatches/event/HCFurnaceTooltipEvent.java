package mods.betterwithpatches.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.features.HCFurnace;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class HCFurnaceTooltipEvent {
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent evt) {
        if (FurnaceRecipes.smelting().getSmeltingResult(evt.itemStack) != null) {
            int ticks = HCFurnace.getCookingTime(evt.itemStack);
            float seconds = ticks * 0.05f;
            float items = ticks * 0.005f;
            evt.toolTip.add(I18n.format("text.bwp.hcFurnace", String.format("%.2f", seconds), String.format("%.2f", items)));
        }
    }
}
