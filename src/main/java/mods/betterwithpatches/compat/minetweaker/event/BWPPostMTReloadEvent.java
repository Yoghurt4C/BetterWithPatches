package mods.betterwithpatches.compat.minetweaker.event;

import minetweaker.MineTweakerImplementationAPI;
import minetweaker.util.IEventHandler;
import mods.betterwithpatches.craft.BWPRecipe;
import mods.betterwithpatches.craft.filteredhopper.HopperRecipe;
import mods.betterwithpatches.data.recipe.ItemStackMap;
import mods.betterwithpatches.data.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

import static mods.betterwithpatches.craft.FilteredHopperInteractions.RECIPE_MAP;

public class BWPPostMTReloadEvent implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent> {
    public static final ItemStackMap<Set<HopperRecipe>> toAdd = new ItemStackMap<>(new HashSet<>());

    //todo collapse
    @Override
    public void handle(MineTweakerImplementationAPI.ReloadEvent reloadEvent) {
        for (Map.Entry<ItemStack, ItemStackMap<HopperRecipe>> entry : RECIPE_MAP.entrySet()) {
            if (entry.getValue() == null) continue;
            HashMap<Integer, RecipeOutput[]> cache = new HashMap<>();
            Iterator<HopperRecipe> iter = entry.getValue().values().iterator();
            while (iter.hasNext()) {
                BWPRecipe recipe = iter.next();
                if (recipe.getOreId() >= 0) {
                    cache.putIfAbsent(recipe.getOreId(), recipe.getRawOutputs());
                    iter.remove();
                }
            }
            if (cache.isEmpty()) continue;

            for (Map.Entry<Integer, RecipeOutput[]> recipe : cache.entrySet()) {
                List<ItemStack> ores = OreDictionary.getOres(OreDictionary.getOreName(recipe.getKey()));
                for (ItemStack ore : ores) {
                    entry.getValue().put(ore, new HopperRecipe(recipe.getValue(), recipe.getKey()));
                }
            }
        }

        for (Map.Entry<ItemStack, Set<HopperRecipe>> addPairs : toAdd.entrySet()) {
            ItemStackMap<HopperRecipe> recipes = !RECIPE_MAP.containsKey(addPairs.getKey()) ? RECIPE_MAP.put(addPairs.getKey(), new ItemStackMap<>(null)) : RECIPE_MAP.get(addPairs.getKey());
            if (recipes == null) continue;
            for (HopperRecipe recipe : addPairs.getValue()) {
                for (ItemStack ore : OreDictionary.getOres(OreDictionary.getOreName(recipe.getOreId()), false)) {
                    recipes.put(ore, recipe);
                }
            }
        }
    }

    public static void enqueueODRecipe(ItemStack filter, String od, Object... outputs) {
        enqueueODRecipe(filter, new HopperRecipe(od, outputs));
    }

    public static void enqueueODRecipe(ItemStack filter, HopperRecipe recipe) {
        if (!toAdd.containsKey(filter)) {
            toAdd.put(filter, new HashSet<>());
        }
        toAdd.get(filter).add(recipe);
    }
}
