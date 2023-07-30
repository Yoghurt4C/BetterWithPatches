package mods.betterwithpatches.craft;

import betterwithmods.BWRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Hashtable;
import java.util.Map;

import static mods.betterwithpatches.util.BWPConstants.L;
import static mods.betterwithpatches.util.BWPConstants.getId;

/**
 * Functionally a replacement for {@link betterwithmods.craft.KilnInteraction} - necessary to make use of multiple outputs in recipes.
 * You can still call the methods in that class directly if you don't need multiple outputs, as they are redirected here.
 */
public interface KilnInteractionExtensions {
    Hashtable<String, ItemStack[]> cookables = new Hashtable<>();

    static void addStokedRecipe(Block block, ItemStack... output) {
        cookables.put(getId(block), output);
    }

    static void addStokedRecipe(Block block, int meta, ItemStack... output) {
        cookables.put(getId(block) + "@" + meta, output);
    }

    static void addStokedRecipe(String oreDict, ItemStack... output) {
        if (OreDictionary.doesOreNameExist(oreDict)) {
            cookables.put("ore:" + oreDict, output);
        } else {
            L.warn("[Kiln] Couldn't add recipe ({} -> {}) because {} is empty.", oreDict, output, oreDict);
        }
    }

    static boolean contains(Block block, int meta) {
        String id = getId(block);
        String withMeta = id + "@" + meta;
        ItemStack stack = new ItemStack(block, 1, meta);
        for (String s : cookables.keySet()) {
            if (s.startsWith("ore:") && BWPConstants.presentInOD(stack, s.substring(4))) {
                return true;
            } else if (s.equals(id) || s.equals(withMeta)) {
                return true;
            }
        }
        return false;
    }

    static ItemStack[] getProducts(Block block, int meta) {
        String id = getId(block);
        String withMeta = id + "@" + meta;
        ItemStack stack = new ItemStack(block, 1, meta);
        for (Map.Entry<String, ItemStack[]> pair : cookables.entrySet()) {
            String s = pair.getKey();
            if (s.startsWith("ore:") && BWPConstants.presentInOD(stack, s.substring(4))) {
                return pair.getValue();
            } else if (s.equals(id) || s.equals(withMeta)) {
                return pair.getValue();
            }
        }
        return null;
    }

    static void addKilnRecipes() {
        KilnInteractionExtensions.addStokedRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay));
        KilnInteractionExtensions.addStokedRecipe(BWRegistry.unfiredPottery, 0, new ItemStack(BWRegistry.singleMachines, 1, 2));
        KilnInteractionExtensions.addStokedRecipe(BWRegistry.unfiredPottery, 1, new ItemStack(BWRegistry.planter, 1, 0));
        KilnInteractionExtensions.addStokedRecipe(BWRegistry.unfiredPottery, 2, new ItemStack(BWRegistry.planter, 1, 15));
        KilnInteractionExtensions.addStokedRecipe("logWood", new ItemStack(Items.coal, 2, 1));
    }

    static void addKilnOreRecipes() {
        for (String oreName : OreDictionary.getOreNames()) {
            if (oreName.startsWith("ore")) {
                for (ItemStack ore : OreDictionary.getOres(oreName, false)) {
                    if (ore.getItem() instanceof ItemBlock) {
                        Block block = ((ItemBlock) ore.getItem()).field_150939_a;
                        int meta = ore.getItemDamage();
                        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(ore);
                        if (result != null) {
                            if (Config.HCOres) result.stackSize = 2;
                            if (meta == OreDictionary.WILDCARD_VALUE) {
                                KilnInteractionExtensions.addStokedRecipe(block, result);
                            } else {
                                KilnInteractionExtensions.addStokedRecipe(block, meta, result);
                            }
                        }
                    }
                }
            }
        }
    }
}
