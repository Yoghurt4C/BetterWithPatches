package mods.betterwithpatches.craft;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import mods.betterwithpatches.compat.minetweaker.event.BWPPostMTReloadEvent;
import mods.betterwithpatches.craft.filteredhopper.HopperRecipe;
import mods.betterwithpatches.craft.filteredhopper.SoulUrnRecipe;
import mods.betterwithpatches.data.recipe.*;
import mods.betterwithpatches.proxy.CommonProxy;
import mods.betterwithpatches.util.BWMaterials;
import mods.betterwithpatches.util.BWPUtils;
import mods.betterwithpatches.util.IFilteredHopper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static mods.betterwithpatches.util.BWPUtils.presentInOD;
import static mods.betterwithpatches.util.BWPUtils.stackHasODPrefix;

public interface FilteredHopperInteractions {
    HopperFilter EMPTY = stack -> true;
    ItemStackMap<HopperFilter> HOPPER_FILTERS = new ItemStackMap<>(EMPTY);
    ItemStackMap<ItemStackMap<HopperRecipe>> RECIPE_MAP = new ItemStackMap<>(new ItemStackMap<>(null));

    static void addFilter(Block block, HopperFilter inputPredicate) {
        addFilter(new ItemStack(block), inputPredicate);
    }

    static void addFilter(ItemStack filter, HopperFilter inputPredicate) {
        HOPPER_FILTERS.put(filter, inputPredicate);
    }

    static void addRecipe(ItemStack filter, Block input, Object... outputs) {
        addRecipe(filter, new ItemStack(input), outputs);
    }

    static void addRecipe(ItemStack filter, Item input, Object... outputs) {
        addRecipe(filter, new ItemStack(input), outputs);
    }

    static void addRecipe(ItemStack filter, String oreName, Object... outputs) {
        if (RECIPE_MAP.get(filter) == null) RECIPE_MAP.put(filter, new ItemStackMap<>(null));
        if (CommonProxy.isMTPresent) {
            BWPPostMTReloadEvent.enqueueODRecipe(filter, oreName, outputs);
        } else {
            List<ItemStack> ores = OreDictionary.getOres(oreName, false);
            if (ores.isEmpty()) return;
            HopperRecipe recipe = new HopperRecipe(oreName, outputs);
            for (ItemStack ore : ores) {
                RECIPE_MAP.get(filter).put(ore, recipe);
            }
        }
    }

    static void addRecipe(ItemStack filter, ItemStack input, Object... outputs) {
        if (input == null) return;
        else RECIPE_MAP.computeIfAbsent(filter, k -> new ItemStackMap<>(null));
        RECIPE_MAP.get(filter).put(input, new HopperRecipe(-1, outputs));
    }

    /**
     * @param filter Filter stack
     * @param input  Input stack, if you need OD use the method below
     * @param recipe new HopperRecipe(String oreName, ItemStack[] outputs)
     */
    static void addCustomRecipe(@Nullable ItemStack filter, @Nonnull ItemStack input, HopperRecipe recipe) {
        RECIPE_MAP.computeIfAbsent(filter, k -> new ItemStackMap<>(null));
        RECIPE_MAP.get(filter).put(input, recipe);
    }

    static void addCustomRecipe(@Nullable ItemStack filter, HopperRecipe recipe) {
        if (CommonProxy.isMTPresent) {
            BWPPostMTReloadEvent.enqueueODRecipe(filter, recipe);
        } else {
            String oreName = OreDictionary.getOreName(recipe.getOreId());
            if (oreName.equals("Unknown")) return;
            for (ItemStack ore : OreDictionary.getOres(oreName)) {
                RECIPE_MAP.get(filter).put(ore, recipe);
            }
        }
    }

    static HopperRecipe findFilteredRecipe(ItemStack filter, ItemStack input) {
        ItemStackMap<HopperRecipe> map = RECIPE_MAP.get(filter);
        if (map == null) {
            return null;
        } else {
            return map.get(input);
        }
    }

    static void registerFiltersAndRecipes() {
        addFilter(Blocks.ladder, stack -> !(stack.getItem() instanceof ItemBlock) || presentInOD(stack, "treeSapling"));
        ItemStack soulSand = new ItemStack(Blocks.soul_sand);
        addFilter(soulSand, new SoulsandFilter() {
            @Override
            public boolean test(ItemStack stack) {
                return BWPUtils.getBlock(stack.getItem()) == Blocks.soul_sand;
            }
        });
        ItemStack wicker = new ItemStack(BWRegistry.pane, 1, 2);
        addFilter(wicker, stack -> presentInOD(stack, "sand", "listAllSeeds", "foodFlour", "pile") || stackHasODPrefix(stack, "dust"));
        addFilter(Blocks.trapdoor, stack -> stack.getItem() instanceof ItemBlock);
        addFilter(BWRegistry.pane, stack -> stack.getMaxStackSize() == 1);
        addFilter(Blocks.iron_bars, stack -> stack.getMaxStackSize() > 1);
        addFilter(new ItemStack(BWRegistry.pane, 1, 1), stack -> presentInOD(stack, "paper", "scroll", "string", "fiberHemp", "hideTanned", "hideBelt", "hideScoured", "hideStrap", "leather", "wool"));


        addCustomRecipe(soulSand, new SoulUrnRecipe("dustNetherrack", BWMaterials.getMaterial(BWMaterials.HELLFIRE_DUST)));
        addCustomRecipe(soulSand, new SoulUrnRecipe("dustSoul", BWMaterials.getMaterial(BWMaterials.SAWDUST)));
        /*todo config?*/
        addCustomRecipe(soulSand, new SoulUrnRecipe("dustGlowstone", BWMaterials.getMaterial(BWMaterials.BRIMSTONE)));
        addRecipe(wicker, Blocks.gravel, new WeightedStack(new ChanceStack(Blocks.sand, 0.5f), new ChanceStack(new ItemStack(Blocks.sand, 1, 1), 0.5f)), Items.flint);
        addCustomRecipe(soulSand, new HopperRecipe("sand", Blocks.soul_sand) {
            @Override
            public boolean canCraft(World world, int x, int y, int z, TileEntityFilteredHopper tile) {
                return ((IFilteredHopper) tile).getSoulsRetained() > 0;
            }

            @Override
            public void onCraft(World world, int x, int y, int z, TileEntityFilteredHopper tile) {
                tile.increaseSoulCount(-1);
            }
        });
    }
}
