package mods.betterwithpatches.event;

import betterwithmods.BWRegistry;
import betterwithmods.event.LogHarvestEvent;
import betterwithmods.event.TConHelper;
import betterwithmods.items.ItemKnife;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;

public class LogHarvestEventReplacement extends LogHarvestEvent {

    @Override
    @SubscribeEvent
    public void harvestLog(BlockEvent.HarvestDropsEvent evt) {
        Block block = evt.block;
        int meta = evt.blockMetadata;
        ItemStack log = new ItemStack(block, 1, meta);
        if (presentInOD(log, "logWood")) {
            int harvestMeta = block.damageDropped(meta);
            if (!evt.isSilkTouching || evt.harvester != null) {
                boolean harvest = false;
                if (evt.harvester != null && evt.harvester.getCurrentEquippedItem() != null) {
                    Item item = evt.harvester.getCurrentEquippedItem().getItem();
                    if (item instanceof ItemTool) {
                        ItemTool tool = (ItemTool) item;
                        if (tool.getHarvestLevel(evt.harvester.getCurrentEquippedItem(), "axe") >= 0 && !(tool instanceof ItemKnife)) {
                            harvest = true;
                        }
                    } else if (Loader.isModLoaded("TConstruct") && TConHelper.isEquippedItemCorrectTool(evt.harvester, "axe", true) && TConHelper.isEquippedItemCorrectLevel(evt.harvester, "axe", block.getHarvestLevel(meta))) {
                        harvest = true;
                    }
                }

                if (!harvest) {
                    int fortune = evt.harvester != null && evt.harvester.getCurrentEquippedItem() != null && evt.harvester.getCurrentEquippedItem().getItem() instanceof ItemKnife ? evt.fortuneLevel : 0;
                    boolean fort = fortune > 0;
                    if (!evt.isSilkTouching) {
                        for (ItemStack logStack : evt.drops) {
                            if (presentInOD(logStack, "logWood")) {
                                craft.setInventorySlotContents(0, new ItemStack(block, 1, harvestMeta));
                                IRecipe recipe = findMatchingRecipe(craft, evt.world);
                                ItemStack planks;
                                if (recipe != null && recipe.getCraftingResult(craft) != null) {
                                    planks = recipe.getCraftingResult(craft);
                                    if (presentInOD(planks, "plankWood")) {
                                        Block drop = ((ItemBlock) logStack.getItem()).field_150939_a;
                                        if (Config.hcWoodPlankLoss > 0) {
                                            planks.stackSize = Math.max(0, fort ? planks.stackSize - Config.hcWoodPlankLoss + evt.world.rand.nextInt(2) : planks.stackSize - Config.hcWoodPlankLoss);
                                        } else if (fort) {
                                            planks.stackSize += evt.world.rand.nextInt(2);
                                        }
                                        if (HardcoreWoodInteractionExtensions.contains(drop, harvestMeta)) {
                                            ItemStack[] overrides = HardcoreWoodInteractionExtensions.getBarkOverrides(drop, harvestMeta);
                                            Collections.addAll(evt.drops, overrides);
                                        } else {
                                            int barkStack = fort ? 1 + evt.world.rand.nextInt(fortune) : 1;
                                            ItemStack bark = new ItemStack(BWRegistry.bark, barkStack);
                                            bark.setTagCompound(HardcoreWoodInteractionExtensions.getBarkTagForLog(drop, harvestMeta));
                                            evt.drops.add(bark);
                                            int sawdustStack = fort ? 1 + evt.world.rand.nextInt(fortune) : 1;
                                            ItemStack sawdust = new ItemStack(BWRegistry.material, sawdustStack, 22);
                                            evt.drops.add(sawdust);
                                        }

                                        evt.drops.add(planks);
                                        evt.drops.remove(logStack);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean presentInOD(ItemStack stack, String od) {
        return ArrayUtils.contains(OreDictionary.getOreIDs(stack), OreDictionary.getOreID(od));
    }
}
