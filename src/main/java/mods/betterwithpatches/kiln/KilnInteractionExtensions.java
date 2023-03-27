package mods.betterwithpatches.kiln;

import betterwithmods.BWRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Hashtable;
import java.util.List;

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
        List<ItemStack> ores = OreDictionary.getOres(oreDict, true);
        for (ItemStack ore : ores) {
            Item item = ore.getItem();
            int meta = ore.getItemDamage();
            Block block;
            if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
            else block = Block.getBlockFromItem(item);
            String name = GameData.getBlockRegistry().getNameForObject(block);
            if (meta == OreDictionary.WILDCARD_VALUE) cookables.put(name, output);
            else cookables.put(name + "@" + meta, output);
        }
    }

    static boolean contains(Block block, int meta) {
        String identifier = getId(block);
        if (cookables.containsKey(identifier)) return true;
        else return cookables.containsKey(identifier + "@" + meta);
    }

    static ItemStack[] getProducts(Block block, int meta) {
        String identifier = getId(block);
        ItemStack[] stacks = cookables.get(identifier);
        if (stacks == null) stacks = cookables.get(identifier + "@" + meta);
        return stacks;
    }

    static String getId(Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }

    static void addKilnRecipes() {
        KilnInteractionExtensions.addStokedRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay));
        KilnInteractionExtensions.addStokedRecipe(BWRegistry.unfiredPottery, 0, new ItemStack(BWRegistry.singleMachines, 1, 2));
        KilnInteractionExtensions.addStokedRecipe(BWRegistry.unfiredPottery, 1, new ItemStack(BWRegistry.planter, 1, 0));
        KilnInteractionExtensions.addStokedRecipe(BWRegistry.unfiredPottery, 2, new ItemStack(BWRegistry.planter, 1, 15));
        KilnInteractionExtensions.addStokedRecipe("logWood", new ItemStack(Items.coal, 2, 1));
    }
}
