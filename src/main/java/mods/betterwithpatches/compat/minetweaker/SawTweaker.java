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
import mods.betterwithpatches.craft.SawInteractionExtensions;
import mods.betterwithpatches.data.BWPMobDrops;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static mods.betterwithpatches.craft.SawInteractionExtensions.blockOverrides;

@ZenClass("mods.betterwithmods.Saw")
public class SawTweaker {
    @ZenMethod
    public static void add(IItemStack[] output, IIngredient input) {
        if (input instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new Add(MineTweakerMC.getItemStacks(output), new OreStack(((IOreDictEntry) input).getName())));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta > 0) s = BWPConstants.getId(block) + "@" + meta;
            else s = BWPConstants.getId(block);
            MineTweakerAPI.apply(new Add(MineTweakerMC.getItemStacks(output), s));
        } else {
            BWPConstants.L.warn("Couldn't add Saw recipe for {} -> {}.", input, output);
        }
    }

    @ZenMethod
    public static void remove(IIngredient input) {
        if (input instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new Remove(new OreStack(((IOreDictEntry) input).getName())));
        } else if (input instanceof IItemStack) {
            Block block = MineTweakerMC.getBlock((IItemStack) input);
            int meta = ((IItemStack) input).getDamage();
            String s;
            if (meta > 0) s = BWPConstants.getId(block) + "@" + meta;
            else s = BWPConstants.getId(block);
            MineTweakerAPI.apply(new Remove(s));
        } else {
            BWPConstants.L.warn("Couldn't remove Saw recipe for {}.", input);
        }
    }

    @ZenMethod
    public static void removeAll() {
        List<String> keys = new ArrayList<>(blockOverrides.keySet());
        for (String s : keys) {
            MineTweakerAPI.apply(new Remove(s));
        }
    }

    @ZenMethod
    public static void addEntity(String entity, boolean choppingBlock, IItemStack[] drops, int dropChance) {
        Class<? extends Entity> cls = EntityList.stringToClassMapping.get(entity);
        if (cls == null) return;
        MineTweakerAPI.apply(new AddEntity(choppingBlock, MineTweakerMC.getItemStacks(drops), dropChance, cls.getName()));
    }

    @ZenMethod
    public static void addEntity(String entity, IItemStack[] drops, int dropChance, IItemStack[] chops, int chopChance) {
        Class<? extends Entity> cls = EntityList.stringToClassMapping.get(entity);
        if (cls == null) return;
        MineTweakerAPI.apply(new AddEntity(MineTweakerMC.getItemStacks(drops), dropChance, MineTweakerMC.getItemStacks(chops), chopChance, cls.getName()));
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
            if (this.input instanceof OreStack) {
                for (ItemStack ore : ((OreStack) this.input).getOres()) {
                    Item item = ore.getItem();
                    int meta = ore.getItemDamage();
                    Block block;
                    if (item instanceof ItemBlock) block = ((ItemBlock) item).field_150939_a;
                    else block = Block.getBlockFromItem(item);
                    String name = GameData.getBlockRegistry().getNameForObject(block);
                    if (meta == OreDictionary.WILDCARD_VALUE) blockOverrides.put(name, outputs);
                    else blockOverrides.put(name + "@" + meta, outputs);
                }
            } else {
                blockOverrides.put((String) input, outputs);
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
                    if (meta == OreDictionary.WILDCARD_VALUE) blockOverrides.remove(name, outputs);
                    else blockOverrides.remove(name + "@" + meta, outputs);
                }
            } else {
                blockOverrides.remove((String) input, outputs);
            }
        }

        @Override
        public String describe() {
            return MTHelper.addRecipeDescription("Saw", this.input, this.outputs);
        }

        @Override
        public String describeUndo() {
            return MTHelper.removeRecipeDescription("Saw", this.input, this.outputs);
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
                    if (meta == OreDictionary.WILDCARD_VALUE) outputs = blockOverrides.remove(name);
                    else outputs = blockOverrides.remove(name + "@" + meta);
                }
            } else {
                outputs = blockOverrides.remove((String) input);
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
                    if (meta == OreDictionary.WILDCARD_VALUE) blockOverrides.put(name, outputs);
                    else blockOverrides.put(name + "@" + meta, outputs);
                }
            } else {
                blockOverrides.put((String) input, outputs);
            }
        }

        @Override
        public String describe() {
            return String.format("[BWP] Removing %s recipe: %s", "Saw", input);
        }

        @Override
        public String describeUndo() {
            return MTHelper.addRecipeDescription("Saw", input, outputs);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    private static class AddEntity extends Add {
        public int dropChance, chopChance;
        public ItemStack[] chops;
        public boolean choppingBlock;

        public AddEntity(boolean choppingBlock, ItemStack[] drops, int dropChance, Object input) {
            super(drops, input);
            this.choppingBlock = choppingBlock;
            this.dropChance = dropChance;
        }

        public AddEntity(ItemStack[] drops, int dropChance, ItemStack[] chops, int chopChance, Object input) {
            super(drops, input);
            this.dropChance = dropChance;
            this.chops = chops;
            this.chopChance = chopChance;
        }

        @Override
        public void apply() {
            if (this.dropChance > 0 && this.chopChance > 0) {
                SawInteractionExtensions.setEntityDrop((String) input, dropChance, outputs, chopChance, chops);
            } else {
                SawInteractionExtensions.setEntityDrop((String) input, choppingBlock, dropChance, outputs);
            }
        }

        @Override
        public void undo() {
            SawInteractionExtensions.entityDrops.remove((String) input);
        }
    }

    private static class RemoveEntity extends Remove {
        public BWPMobDrops drops;

        public RemoveEntity(Object input) {
            super(input);
        }

        @Override
        public void apply() {
            this.drops = SawInteractionExtensions.entityDrops.remove((String) input);
        }

        @Override
        public void undo() {
            SawInteractionExtensions.entityDrops.put((String) input, drops);
        }

        @Override
        public String describeUndo() {
            return MTHelper.addRecipeDescription("Saw", input, drops.stacks);
        }
    }
}
