package mods.betterwithpatches.craft;

import betterwithmods.BWRegistry;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Hashtable;
import java.util.Map;

import static mods.betterwithpatches.util.BWPConstants.L;
import static mods.betterwithpatches.util.BWPUtils.getId;

/**
 * Functionally a replacement for {@link betterwithmods.craft.TurntableInteraction} - necessary to make use of multiple outputs in recipes.
 * You can still call the methods in that class directly if you don't need multiple outputs, as they are redirected here.
 */
public interface TurntableInteractionExtensions {
    Hashtable<String, ItemStack[]> spinnables = new Hashtable<>();

    static void addBlockRecipe(Block block, ItemStack... output) {
        if (output[0].getItem() instanceof ItemBlock) spinnables.put(getId(block), output);
        else
            L.info("Couldn't add Turntable recipe ({} -> {}) because the first output stack isn't a valid Block!", getId(block), output);
    }

    static void addBlockRecipe(Block block, int meta, ItemStack... output) {
        if (output[0].getItem() instanceof ItemBlock) spinnables.put(getId(block) + "@" + meta, output);
        else
            L.info("[Turntable] Couldn't add recipe ({} -> {}) because the first output stack isn't a valid Block!", getId(block), output);
    }

    static void addBlockRecipe(String oreDict, ItemStack... output) {
        if (OreDictionary.doesOreNameExist(oreDict)) {
            spinnables.put("ore:" + oreDict, output);
        } else {
            L.warn("[Turntable] Couldn't add recipe ({} -> {}) because {} is empty.", oreDict, output, oreDict);
        }
    }

    static boolean contains(Block block, int meta) {
        String id = getId(block);
        String withMeta = id + "@" + meta;
        ItemStack stack = new ItemStack(block, 1, meta);
        for (String s : spinnables.keySet()) {
            if (s.startsWith("ore:") && BWPUtils.presentInOD(stack, s.substring(4))) {
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
        for (Map.Entry<String, ItemStack[]> pair : spinnables.entrySet()) {
            String s = pair.getKey();
            if (s.startsWith("ore:") && BWPUtils.presentInOD(stack, s.substring(4))) {
                return pair.getValue();
            } else if (s.equals(id) || s.equals(withMeta)) {
                return pair.getValue();
            }
        }
        return null;
    }

    static void addTurntableRecipes() {
        addBlockRecipe(Blocks.clay, 0, new ItemStack(BWRegistry.unfiredPottery, 1, 0), new ItemStack(Items.clay_ball));
        addBlockRecipe(BWRegistry.unfiredPottery, 0, new ItemStack(BWRegistry.unfiredPottery, 1, 1), new ItemStack(Items.clay_ball));
        addBlockRecipe(BWRegistry.unfiredPottery, 1, new ItemStack(BWRegistry.unfiredPottery, 1, 2), new ItemStack(Items.clay_ball));
    }
}
