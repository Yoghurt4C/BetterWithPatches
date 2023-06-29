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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Collections;

import static mods.betterwithpatches.util.BWPConstants.presentInOD;

public class LogHarvestEventReplacement extends LogHarvestEvent {

    /**
     * This is a very fat event. Caution is advised.
     */
    @Override
    @SubscribeEvent
    public void harvestLog(BlockEvent.HarvestDropsEvent evt) {
        Block block = evt.block;
        int meta = evt.blockMetadata;
        ItemStack log = new ItemStack(block, 1, meta);
        if (presentInOD(log, "logWood")) {
            int harvestMeta = block.damageDropped(meta);
            if (evt.harvester != null) {
                boolean harvest = true, fort = false, saw = false;
                if (evt.harvester.getCurrentEquippedItem() != null) {
                    ItemStack item = evt.harvester.getCurrentEquippedItem();
                    if (item.getItem() instanceof ItemTool) {
                        ItemTool tool = (ItemTool) item.getItem();
                        if (tool.getHarvestLevel(item, "axe") >= 0) {
                            saw = (item.hasTagCompound() && item.stackTagCompound.hasKey("BWMHarvest"));
                            if (tool instanceof ItemKnife) {
                                fort = true;
                            } else if (!saw) {
                                harvest = false;
                            }
                        }
                    } else if (Loader.isModLoaded("TConstruct") && TConHelper.isEquippedItemCorrectTool(evt.harvester, "axe", true) && TConHelper.isEquippedItemCorrectLevel(evt.harvester, "axe", block.getHarvestLevel(meta))) {
                        harvest = false;
                    }
                }

                if (harvest && !evt.isSilkTouching) {
                    int fortune = evt.fortuneLevel;
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
                                        planks.stackSize = Math.max(0, fort ? planks.stackSize - Config.hcWoodPlankLoss + evt.world.rand.nextInt(fortune) : planks.stackSize - Config.hcWoodPlankLoss);
                                        if (saw) planks.stackSize = planks.stackSize << 1;
                                    } else if (fort) {
                                        planks.stackSize += evt.world.rand.nextInt(fortune);
                                    }
                                    if (HardcoreWoodInteractionExtensions.contains(drop, harvestMeta)) {
                                        ItemStack[] overrides = HardcoreWoodInteractionExtensions.getBarkOverrides(drop, harvestMeta);
                                        Collections.addAll(evt.drops, overrides);
                                    } else {
                                        int barkStack = 1, sawdustStack = 1;
                                        if (fort || saw) {
                                            barkStack = 1 + evt.world.rand.nextInt(fortune);
                                            sawdustStack = 1 + evt.world.rand.nextInt(fortune);
                                        }
                                        ItemStack bark = new ItemStack(BWRegistry.bark, barkStack);
                                        bark.setTagCompound(HardcoreWoodInteractionExtensions.getBarkTagForLog(drop, harvestMeta));
                                        evt.drops.add(bark);
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
