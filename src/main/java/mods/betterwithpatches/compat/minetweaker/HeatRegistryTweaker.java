package mods.betterwithpatches.compat.minetweaker;

import betterwithmods.craft.heat.BWMHeatRegistry;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.HeatRegistry")
public class HeatRegistryTweaker {

    @ZenMethod
    public static void addHeatSource(IItemStack stack, int heat) {
        Block block = MineTweakerMC.getBlock(stack);
        if (block == null) {
            BWPConstants.L.warn("Couldn't add HeatRegistry data [{}] -> [{}] because the input isn't a valid block.", stack, heat);
            return;
        }
        if (stack.getDamage() > 0) {
            MineTweakerAPI.apply(new Add(block, stack.getDamage(), heat));
        } else {
            MineTweakerAPI.apply(new Add(block, heat));
        }
    }

    @ZenMethod
    public static void addHeatSource(IItemStack stack, int meta, int heat) {
        Block block = MineTweakerMC.getBlock(stack);
        if (block == null) {
            BWPConstants.L.warn("Couldn't add HeatRegistry data [{}] -> [{}] because the input isn't a valid block.", stack, heat);
            return;
        }
        MineTweakerAPI.apply(new Add(block, meta, heat));
    }

    private static class Add implements IUndoableAction {
        public Block block;
        public int meta;
        public int heat;

        public Add(Block block, int heat) {
            this.block = block;
            this.meta = -1;
            this.heat = heat;
        }

        public Add(Block block, int meta, int heat) {
            this.block = block;
            this.meta = meta;
            this.heat = heat;
        }

        @Override
        public void apply() {
            if (meta > -1) BWMHeatRegistry.setBlockHeatRegistry(block, meta, heat);
            else BWMHeatRegistry.setBlockHeatRegistry(block, heat);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (meta > -1) BWMHeatRegistry.getHeatRegistry().remove(block + ":" + meta);
            else for (int i = 0; i < 16; i++) {
                if (BWMHeatRegistry.getHeatRegistry().remove(block + ":" + i) == null) break;
            }
        }

        @Override
        public String describe() {
            return String.format("[BWP] Adding block to the Heat Registry: [%s] -> [%s] heat.", meta > -1 ? block + "@" + meta : block, heat);
        }

        @Override
        public String describeUndo() {
            return String.format("[BWP] Removing block from the Heat Registry: [%s] -> [%s] heat.", meta > -1 ? block + "@" + meta : block, heat);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
