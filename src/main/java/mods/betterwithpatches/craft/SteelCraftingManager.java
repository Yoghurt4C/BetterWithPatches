package mods.betterwithpatches.craft;

import betterwithmods.BWRegistry;
import betterwithmods.craft.OreStack;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.craft.anvil.ShapedSteelRecipe;
import mods.betterwithpatches.craft.anvil.ShapelessSteelRecipe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static mods.betterwithpatches.util.BWMaterials.*;

public interface SteelCraftingManager {
    List<IRecipe> RECIPES = new ArrayList<>();

    static void addShapedRecipe(ItemStack output, Object... inputs) {
        StringBuilder sb = new StringBuilder();
        int i = 0, w = 0, h = 0;
        if (inputs[i] instanceof String[]) {
            String[] in = (String[]) inputs[i++];
            for (String s : in) {
                h++;
                if (s.length() > w) w = s.length();
                sb.append(s);
            }
        } else while (inputs[i] instanceof String) {
            String s = (String) inputs[i++];
            h++;
            if (s.length() > w) w = s.length();
            sb.append(s);
        }

        Map<Character, Object> keys = new Hashtable<>();
        while (i < inputs.length) {
            char c = (char) inputs[i];
            Object ingr = null;
            Object oStack = inputs[i + 1];
            if (oStack instanceof Item) {
                ingr = new ItemStack((Item) oStack);
            } else if (oStack instanceof Block) {
                ingr = new ItemStack((Block) oStack);
            } else if (oStack instanceof String) {
                ingr = new OreStack((String) oStack);
            } else if (oStack instanceof ItemStack || oStack instanceof OreStack) {
                ingr = oStack;
            }
            keys.put(c, ingr);
            i += 2;
        }

        Object[] ingredients = new Object[w * h];

        for (int j = 0; j < w * h; j++) {
            char c = sb.charAt(j);
            ingredients[j] = keys.get(c);
        }

        IRecipe recipe = new ShapedSteelRecipe(w, h, ingredients, output);
        RECIPES.add(recipe);
    }

    /**
     * Just hope everything passsed through is valid and let the recipe resolve itself
     */
    static void addShapelessRecipe(ItemStack output, Object... inputs) {
        IRecipe recipe = new ShapelessSteelRecipe(output, inputs);
        RECIPES.add(recipe);
    }

    static ItemStack findMatchingRecipe(InventoryCrafting matrix, World world) {
        for (IRecipe recipe : RECIPES) {
            if (recipe.matches(matrix, world)) return recipe.getCraftingResult(matrix);
        }
        if (Config.vanillaRecipesInAnvil) {
            for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
                if (recipe.matches(matrix, world)) return recipe.getCraftingResult(matrix);
            }
        }
        return null;
    }

    static void addSteelAnvilRecipes() {
        addShapedRecipe(new ItemStack(BWRegistry.detector), "CCCC", "LTTL", "SRRS", "SRRS", 'C', "cobblestone", 'L', getMaterial(POLISHED_LAPIS), 'T', Blocks.redstone_torch, 'S', "stone", 'R', "dustRedstone");
        addShapedRecipe(getMaterial(POLISHED_LAPIS, 2), "LLL", "LLL", "GGG", " R ", 'L', "gemLapis", 'R', "dustRedstone", 'G', "nuggetGold");
        addShapedRecipe(getMaterial(CHAINMAIL, 2), "N N ", " N N", "N N ", " N N", 'N', "nuggetIron");

        ItemStack haft = getMaterial(HAFT);
        addShapedRecipe(new ItemStack(BWPRegistry.steelAxe), "XX", "XH", " H", " H", 'X', "ingotSoulforgedSteel", 'H', haft);
        addShapedRecipe(new ItemStack(BWPRegistry.steelHoe), "XX", " H", " H", " H", 'X', "ingotSoulforgedSteel", 'H', haft);
        addShapedRecipe(new ItemStack(BWPRegistry.steelPickaxe), "XXX", " H ", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', haft);
        addShapedRecipe(new ItemStack(BWPRegistry.steelShovel), "X", "H", "H", "H", 'X', "ingotSoulforgedSteel", 'H', haft);
        addShapedRecipe(new ItemStack(BWPRegistry.steelSword), "X", "X", "X", "H", 'X', "ingotSoulforgedSteel", 'H', haft);

        ItemStack plate = getMaterial(ARMOR_PLATE);
        addShapedRecipe(plate, "BSPB", 'B', getMaterial(LEATHER_STRAP), 'S', "ingotSoulforgedSteel", 'P', getMaterial(PADDING));
        addShapedRecipe(new ItemStack(BWPRegistry.steelHelmet), "SSSS", "S  S", "S  S", " PP ", 'P', plate, 'S', "ingotSoulforgedSteel");
        addShapedRecipe(new ItemStack(BWPRegistry.steelChestplate), "P  P", "SSSS", "SSSS", "SSSS", 'P', plate, 'S', "ingotSoulforgedSteel");
        addShapedRecipe(new ItemStack(BWPRegistry.steelLeggings), "SSSS", "PSSP", "P  P", "P  P", 'P', plate, 'S', "ingotSoulforgedSteel");
        addShapedRecipe(new ItemStack(BWPRegistry.steelBoots), " SS ", " SS ", "SPPS", 'P', plate, 'S', "ingotSoulforgedSteel");
        ItemStack leather = getMaterial(TANNED_LEATHER_CUT);
        addShapedRecipe(new ItemStack(BWPRegistry.dredgeHeavyHelmet), "SSSS", "LSSL", "L  L", " PP ", 'S', "ingotSoulforgedSteel", 'L', leather, 'P', plate);
        addShapedRecipe(new ItemStack(BWPRegistry.dredgeHeavyChestplate), "SPPS", "LSSL", "LPPL", "SSSS", 'S', "ingotSoulforgedSteel", 'L', leather, 'P', plate);
        addShapedRecipe(new ItemStack(BWPRegistry.dredgeHeavyLeggings), "SPPS", "SLLS", "L  L", "L  L", 'S', "ingotSoulforgedSteel", 'L', leather, 'P', plate);
        addShapedRecipe(new ItemStack(BWPRegistry.dredgeHeavyBoots), " SS ", "PSSP", "SLLS", 'S', "ingotSoulforgedSteel", 'L', leather, 'P', plate);
    }
}
