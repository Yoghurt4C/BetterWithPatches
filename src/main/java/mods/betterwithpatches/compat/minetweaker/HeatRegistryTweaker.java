package mods.betterwithpatches.compat.minetweaker;

import betterwithmods.craft.heat.BWMHeatRegistry;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.betterwithmods.HeatRegistry")
public class HeatRegistryTweaker {

    @ZenMethod
    public static void addHeatSource(IItemStack stack, int heat) {
        Block block = MineTweakerMC.getBlock(stack);
        if (block == null) {
            BWPConstants.L.warn("Couldn't add HeatRegistry data [{}] -> [{}] because the input isn't a valid block.", stack, heat);
        } else {
            MineTweakerAPI.apply(new Add(block, stack.getDamage(), heat));
        }
    }

    private static class Add implements IUndoableAction {
        public Block block;
        public int meta;
        public int heat;

        public Add(Block block, int meta, int heat) {
            this.block = block;
            this.meta = meta;
            this.heat = heat;
        }

        @Override
        public void apply() {
            if (this.meta == OreDictionary.WILDCARD_VALUE) {
                BWMHeatRegistry.setBlockHeatRegistry(this.block, this.heat);
            } else {
                BWMHeatRegistry.setBlockHeatRegistry(this.block, this.meta, this.heat);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (this.meta == OreDictionary.WILDCARD_VALUE) {
                for (int i = 0; i < 16; i++) {
                    if (BWMHeatRegistry.getHeatRegistry().remove(this.block + ":" + i) == null) break;
                }
            } else {
                BWMHeatRegistry.getHeatRegistry().remove(this.block + ":" + this.meta);
            }
        }

        @Override
        public String describe() {
            return String.format("[BWP] Adding block to the Heat Registry: [%s] -> [%s] heat.", this.meta == OreDictionary.WILDCARD_VALUE ? this.block : this.block + "@" + this.meta, this.heat);
        }

        @Override
        public String describeUndo() {
            return String.format("[BWP] Removing block from the Heat Registry: [%s] -> [%s] heat.", this.meta == OreDictionary.WILDCARD_VALUE ? this.block : this.block + "@" + this.meta, this.heat);
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
