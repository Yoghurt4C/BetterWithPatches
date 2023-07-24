package mods.betterwithpatches.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import mods.betterwithpatches.data.Ingredient;
import mods.betterwithpatches.features.HCArmor;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenClass("mods.betterwithmods.HCArmor")
public class HCArmorTweaker {

    @ZenMethod
    public static void add(IItemStack armor, int weight) {
        MineTweakerAPI.apply(new Add(MineTweakerMC.getItemStack(armor).getItem(), weight));
    }

    private static class Add implements IUndoableAction {
        public final Item item;
        public final int weight;

        private Add(Item item, int weight) {
            this.item = item;
            this.weight = weight;
        }

        @Override
        public void apply() {
            HCArmor.weights.put(item, OreDictionary.WILDCARD_VALUE, weight);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            HCArmor.weights.remove(new Ingredient(item, OreDictionary.WILDCARD_VALUE));
        }

        @Override
        public String describe() {
            return String.format("Added HCArmor weight: %s -> %s", item, weight);
        }

        @Override
        public String describeUndo() {
            return String.format("Removed HCArmor weight: %s -> %s", item, weight);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
