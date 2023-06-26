package mods.betterwithpatches.compat.minetweaker;

import betterwithmods.craft.OreStack;
import cpw.mods.fml.common.registry.GameData;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import mods.betterwithpatches.compat.minetweaker.util.MTHelper;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
            MineTweakerAPI.apply(new TurntableTweaker.Add(MineTweakerMC.getItemStacks(output), new OreStack(((IOreDictEntry) input).getName())));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta > 0) s = BWPConstants.getId(block) + "@" + meta;
            else s = BWPConstants.getId(block);
            MineTweakerAPI.apply(new TurntableTweaker.Add(MineTweakerMC.getItemStacks(output), s));
        } else {
            BWPConstants.L.warn("Couldn't add Turntable recipe for {} -> {}.", input, output);
        }
    }

    @ZenMethod
    public static void remove(IIngredient input) {
        if (input instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new TurntableTweaker.Remove(new OreStack(((IOreDictEntry) input).getName())));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta > 0) s = BWPConstants.getId(block) + "@" + meta;
            else s = BWPConstants.getId(block);
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
            if (!(outputs[0].getItem() instanceof ItemBlock))
                L.info("Couldn't add Turntable recipe ({} -> {}) because the first output stack isn't a valid Block!", this.input, outputs);
            if (this.input instanceof OreStack) {
                for (ItemStack ore : ((OreStack) this.input).getOres()) {
                    Item item = ore.getItem();
                    int meta = ore.getItemDamage();
                    Block block;
                    if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
                    else block = Block.getBlockFromItem(item);
                    String name = GameData.getBlockRegistry().getNameForObject(block);
                    if (meta == OreDictionary.WILDCARD_VALUE) spinnables.put(name, outputs);
                    else spinnables.put(name + "@" + meta, outputs);
                }
            } else {
                spinnables.put((String) input, outputs);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (this.input instanceof OreStack) {
                for (ItemStack ore : ((OreStack) this.input).getOres()) {
                    Item item = ore.getItem();
                    int meta = ore.getItemDamage();
                    Block block;
                    if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
                    else block = Block.getBlockFromItem(item);
                    String name = GameData.getBlockRegistry().getNameForObject(block);
                    if (meta == OreDictionary.WILDCARD_VALUE) spinnables.remove(name, outputs);
                    else spinnables.remove(name + "@" + meta, outputs);
                }
            } else {
                spinnables.remove((String) input, outputs);
            }
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
            if (this.input instanceof OreStack) {
                for (ItemStack ore : ((OreStack) this.input).getOres()) {
                    Item item = ore.getItem();
                    int meta = ore.getItemDamage();
                    Block block;
                    if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
                    else block = Block.getBlockFromItem(item);
                    String name = GameData.getBlockRegistry().getNameForObject(block);
                    if (meta == OreDictionary.WILDCARD_VALUE) outputs = spinnables.remove(name);
                    else outputs = spinnables.remove(name + "@" + meta);
                }
            } else {
                outputs = spinnables.remove((String) input);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (outputs == null) return;
            if (this.input instanceof OreStack) {
                for (ItemStack ore : ((OreStack) this.input).getOres()) {
                    Item item = ore.getItem();
                    int meta = ore.getItemDamage();
                    Block block;
                    if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
                    else block = Block.getBlockFromItem(item);
                    String name = GameData.getBlockRegistry().getNameForObject(block);
                    if (meta == OreDictionary.WILDCARD_VALUE) spinnables.put(name, outputs);
                    else spinnables.put(name + "@" + meta, outputs);
                }
            } else {
                spinnables.put((String) input, outputs);
            }
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
