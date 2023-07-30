package mods.betterwithpatches.features;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.FuelBurnTimeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public interface HCFurnace {
    Hashtable<ItemStack, Integer> COOKING_TIMINGS = new Hashtable<>();

    static int getCookingTime(ItemStack input) {
        for (Map.Entry<ItemStack, Integer> e : COOKING_TIMINGS.entrySet()) {
            if (BWPConstants.stacksMatch(e.getKey(), input)) return e.getValue();
        }
        return Config.hcFurnaceDefaultCookTime;
    }

    static void registerHCFurnaceEvents() {
        if (Config.hcFurnaceCustomFuel) {
            MinecraftForge.EVENT_BUS.register(new HCFurnaceBurnTimeEvent());
        }
        if (Config.hcFurnaceTooltip) {
            MinecraftForge.EVENT_BUS.register(new HCFurnaceTooltipEvent());
        }
    }

    static void overrideCookingTime(ItemStack stack, int ticks) {
        COOKING_TIMINGS.put(stack, ticks);
    }

    static void overrideCookingTime(String od, int ticks) {
        List<ItemStack> ores = OreDictionary.getOres(od, false);
        for (ItemStack ore : ores) {
            overrideCookingTime(ore, ticks);
        }
    }

    static void removeOverride(String od) {
        List<ItemStack> ores = OreDictionary.getOres(od, false);
        for (ItemStack ore : ores) {
            removeOverride(ore);
        }
    }

    static void removeOverride(ItemStack stack) {
        COOKING_TIMINGS.remove(stack);
    }

    class HCFurnaceTooltipEvent {
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

    class HCFurnaceBurnTimeEvent {

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
}
