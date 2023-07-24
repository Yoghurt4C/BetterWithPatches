package mods.betterwithpatches.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.FuelBurnTimeEvent;

public class HCFurnaceBurnTimeEvent {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void onFuelBurnTime(FuelBurnTimeEvent evt) {
        int burn = getBurnTime(evt.fuel);
        if (burn > 0) {
            evt.burnTime = burn;
            evt.setResult(Event.Result.ALLOW);
        }
    }

    private int getBurnTime(ItemStack fuel) {
        Item item = fuel.getItem();
        if (item == Items.boat) {
            return 750;
        } else if (item == Item.getItemFromBlock(Blocks.log)) {
            switch (fuel.getItemDamage()) {
                case 1:
                case 3:
                    return 1200;
                case 2:
                    return 2000;
                case 0:
                default:
                    return 1600;
            }
        } else if (item == Item.getItemFromBlock(Blocks.log2)) {
            return 1600;
        } else if (item == Items.coal && fuel.getItemDamage() == 0) {
            return 1600;
        } else if (item == Item.getItemFromBlock(Blocks.planks)) {
            switch (fuel.getItemDamage()) {
                case 0:
                case 4:
                    return 400;
                case 1:
                case 3:
                case 5:
                    return 300;
                case 2:
                default:
                    return 500;
            }
        } else if (item == Item.getItemFromBlock(Blocks.sapling)) {
            return 25;
        }
        return 0;
    }
}
