package mods.betterwithpatches.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import mods.betterwithpatches.compat.minetweaker.util.MTHelper;
import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static mods.betterwithpatches.craft.KilnInteractionExtensions.cookables;

@ZenClass("mods.betterwithmods.Kiln")
public class KilnTweaker {

    @ZenMethod
    public static void add(IItemStack[] output, IIngredient input) {
        if (input instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new Add(MineTweakerMC.getItemStacks(output), "ore:" + ((IOreDictEntry) input).getName()));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta == OreDictionary.WILDCARD_VALUE) {
                s = BWPUtils.getId(block);
            } else {
                s = BWPUtils.getId(block) + "@" + meta;
            }
            MineTweakerAPI.apply(new Add(MineTweakerMC.getItemStacks(output), s));
        } else {
            BWPConstants.L.warn("Couldn't add Kiln recipe for {} -> {}.", input, output);
        }
    }

    @ZenMethod
    public static void remove(IIngredient input) {
        if (input instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new Remove("ore:" + ((IOreDictEntry) input).getName()));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta == OreDictionary.WILDCARD_VALUE) {
                s = BWPUtils.getId(block);
            } else {
                s = BWPUtils.getId(block) + "@" + meta;
            }
            MineTweakerAPI.apply(new Remove(s));
        } else {
            BWPConstants.L.warn("Couldn't remove Kiln recipe for {}.", input);
        }
    }

    @ZenMethod
    public static void removeAll() {
        List<String> keys = new ArrayList<>(cookables.keySet());
        for (String s : keys) {
            MineTweakerAPI.apply(new Remove(s));
        }
    }

    private static class Add implements IUndoableAction {
        public ItemStack[] outputs;
        public Object input;

        public Add(ItemStack[] outputs, Object input) {
            this.outputs = outputs;
            this.input = input;
        }

        @Override
        public void apply() {
            cookables.put((String) input, outputs);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            cookables.remove((String) input, outputs);
        }

        @Override
        public String describe() {
            return MTHelper.addRecipeDescription("Kiln", this.input, this.outputs);
        }

        @Override
        public String describeUndo() {
            return MTHelper.removeRecipeDescription("Kiln", this.input, this.outputs);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    private static class Remove implements IUndoableAction {
        public Object input;
        public ItemStack[] outputs;

        public Remove(Object input) {
            this.input = input;
        }

        @Override
        public void apply() {
            this.outputs = cookables.remove((String) input);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (outputs == null) return;
            cookables.put((String) input, outputs);
        }

        @Override
        public String describe() {
            return String.format("[BWP] Removing %s recipe with input: [%s]", "Kiln", this.input);
        }

        @Override
        public String describeUndo() {
            return MTHelper.addRecipeDescription("Kiln", this.input, this.outputs);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
