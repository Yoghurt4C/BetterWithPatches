package mods.betterwithpatches.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import mods.betterwithpatches.craft.HCFurnaceExtensions;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.HCFurnace")
public class HCFurnaceTweaker {

    @ZenMethod
    public static void add(IIngredient input, int ticks) {
        MineTweakerAPI.apply(new Add(input, ticks));
    }

    @ZenMethod
    public static void remove(IIngredient input) {
        MineTweakerAPI.apply(new Remove(input));
    }

    private static class Add implements IUndoableAction {
        public final IIngredient ingr;
        public final int ticks;

        public Add(IIngredient input, int ticks) {
            this.ingr = input;
            this.ticks = ticks;
        }

        @Override
        public void apply() {
            if (ingr instanceof IOreDictEntry) {
                HCFurnaceExtensions.overrideCookingTime(((IOreDictEntry) ingr).getName(), ticks);
            } else {
                HCFurnaceExtensions.overrideCookingTime(MineTweakerMC.getItemStack(ingr), ticks);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (ingr instanceof IOreDictEntry) {
                HCFurnaceExtensions.removeOverride(((IOreDictEntry) ingr).getName());
            } else {
                HCFurnaceExtensions.removeOverride(MineTweakerMC.getItemStack(ingr));
            }
        }

        @Override
        public String describe() {
            return String.format("[BWP] Adding HCFurnace override: %s -> %s", ingr, ticks);
        }

        @Override
        public String describeUndo() {
            return String.format("[BWP] Removing HCFurnace override: %s -> %s", ingr, ticks);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    private static class Remove implements IUndoableAction {
        public final IIngredient ingr;

        public int ticks;

        public Remove(IIngredient input) {
            this.ingr = input;
        }

        @Override
        public void apply() {
            if (ingr instanceof IOreDictEntry) {
                HCFurnaceExtensions.removeOverride(((IOreDictEntry) ingr).getName());
            } else {
                HCFurnaceExtensions.removeOverride(MineTweakerMC.getItemStack(ingr));
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (ingr instanceof IOreDictEntry) {
                HCFurnaceExtensions.overrideCookingTime(((IOreDictEntry) ingr).getName(), ticks);
            } else {
                HCFurnaceExtensions.overrideCookingTime(MineTweakerMC.getItemStack(ingr), ticks);
            }
        }

        @Override
        public String describeUndo() {
            return String.format("[BWP] Adding HCFurnace override: %s", ingr);
        }

        @Override
        public String describe() {
            return String.format("[BWP] Removing HCFurnace override: %s", ingr);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
