package mods.betterwithpatches.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import mods.betterwithpatches.compat.minetweaker.util.MTHelper;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static mods.betterwithpatches.craft.TurntableInteractionExtensions.spinnables;
import static mods.betterwithpatches.util.BWPConstants.L;

@ZenClass("mods.betterwithmods.Turntable")
public class TurntableTweaker {
    @ZenMethod
    public static void add(IItemStack[] output, IIngredient input) {
        if (input instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new TurntableTweaker.Add(MineTweakerMC.getItemStacks(output), "ore:" + ((IOreDictEntry) input).getName()));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta == OreDictionary.WILDCARD_VALUE) {
                s = BWPConstants.getId(block);
            } else {
                s = BWPConstants.getId(block) + "@" + meta;
            }
            MineTweakerAPI.apply(new TurntableTweaker.Add(MineTweakerMC.getItemStacks(output), s));
        } else {
            BWPConstants.L.warn("Couldn't add Turntable recipe for {} -> {}.", input, output);
        }
    }

    @ZenMethod
    public static void remove(IIngredient input) {
        if (input instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new TurntableTweaker.Remove("ore:" + ((IOreDictEntry) input).getName()));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta == OreDictionary.WILDCARD_VALUE) {
                s = BWPConstants.getId(block);
            } else {
                s = BWPConstants.getId(block) + "@" + meta;
            }
            MineTweakerAPI.apply(new TurntableTweaker.Remove(s));
        } else {
            BWPConstants.L.warn("Couldn't remove Turntable recipe for {}.", input);
        }
    }

    @ZenMethod
    public static void removeAll() {
        List<String> keys = new ArrayList<>(spinnables.keySet());
        for (String s : keys) {
            MineTweakerAPI.apply(new TurntableTweaker.Remove(s));
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
            if (outputs[0].getItem() instanceof ItemBlock) {
                spinnables.put((String) input, outputs);
            } else {
                L.info("Couldn't add Turntable recipe ({} -> {}) because the first output stack isn't a valid Block!", this.input, this.outputs);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            spinnables.remove((String) input, outputs);
        }

        @Override
        public String describe() {
            return MTHelper.addRecipeDescription("Turntable", this.input, this.outputs);
        }

        @Override
        public String describeUndo() {
            return MTHelper.removeRecipeDescription("Turntable", this.input, this.outputs);
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
            outputs = spinnables.remove((String) input);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (outputs == null) return;
            spinnables.put((String) input, outputs);
        }

        @Override
        public String describe() {
            return String.format("[BWP] Removing %s recipe: %s", "Turntable", input);
        }

        @Override
        public String describeUndo() {
            return MTHelper.addRecipeDescription("Turntable", input, outputs);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
