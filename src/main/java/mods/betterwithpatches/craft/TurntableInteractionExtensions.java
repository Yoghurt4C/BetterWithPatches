package mods.betterwithpatches.craft;

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

import static mods.betterwithpatches.util.BWPConstants.L;
import static mods.betterwithpatches.util.BWPConstants.getId;

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
            L.info("Couldn't add Turntable recipe ({} -> {}) because the first output stack isn't a valid Block!", getId(block), output);
    }

    static void addBlockRecipe(String oreDict, ItemStack... output) {
        if (!(output[0].getItem() instanceof ItemBlock))
            L.info("Couldn't add Turntable recipe ({} -> {}) because the first output stack isn't a valid Block!", oreDict, output);
        List<ItemStack> ores = OreDictionary.getOres(oreDict, true);
        for (ItemStack ore : ores) {
            Item item = ore.getItem();
            int meta = ore.getItemDamage();
            Block block;
            if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
            else block = Block.getBlockFromItem(item);
            String name = GameData.getBlockRegistry().getNameForObject(block);
            if (meta == OreDictionary.WILDCARD_VALUE) spinnables.put(name, output);
            else spinnables.put(name + "@" + meta, output);
        }
    }

    static boolean contains(Block block, int meta) {
        String identifier = getId(block);
        if (spinnables.containsKey(identifier)) return true;
        else return spinnables.containsKey(identifier + "@" + meta);
    }

    static ItemStack[] getProducts(Block block, int meta) {
        String identifier = getId(block);
        ItemStack[] stacks = spinnables.get(identifier);
        if (stacks == null) stacks = spinnables.get(identifier + "@" + meta);
        return stacks;
    }

    static void addTurntableRecipes() {
        addBlockRecipe(Blocks.clay, 0, new ItemStack(BWRegistry.unfiredPottery, 1, 0), new ItemStack(Items.clay_ball));
        addBlockRecipe(BWRegistry.unfiredPottery, 0, new ItemStack(BWRegistry.unfiredPottery, 1, 1), new ItemStack(Items.clay_ball));
        addBlockRecipe(BWRegistry.unfiredPottery, 1, new ItemStack(BWRegistry.unfiredPottery, 1, 2), new ItemStack(Items.clay_ball));
    }
}
