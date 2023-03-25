package mods.betterwithpatches.util;

import betterwithmods.craft.OreStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

import static codechicken.nei.NEIServerUtils.areStacksSameTypeCrafting;

public interface BulkUtils {

    static boolean matchInput(List<Object> inputs, ItemStack ingredient) {
        for (int i = 0; i < inputs.size(); ++i) {
            Object obj = inputs.get(i);
            if (obj instanceof ItemStack) return areStacksSameTypeCrafting(ingredient, (ItemStack) obj);
            else if (obj instanceof Item)
                return areStacksSameTypeCrafting(ingredient, new ItemStack((Item) obj, 1, 32767));
            else if (obj instanceof Block)
                return areStacksSameTypeCrafting(ingredient, new ItemStack((Block) obj, 1, 32767));
            else if (obj instanceof String) {
                List<ItemStack> stacks;
                if (i + 1 < inputs.size()) {
                    if (inputs.get(i + 1) instanceof Integer) {
                        stacks = new OreStack((String) inputs.get(i), (Integer) inputs.get(i + 1)).getOres();
                        ++i;
                    } else {
                        stacks = (new OreStack((String) inputs.get(i)).getOres());
                    }
                } else {
                    stacks = new OreStack((String) inputs.get(i)).getOres();
                }
                for (ItemStack stack : stacks) return areStacksSameTypeCrafting(ingredient, stack);
            } else if (obj instanceof OreStack && ((OreStack) obj).getOres() != null) {
                for (ItemStack stack : ((OreStack) obj).getOres()) {
                    return areStacksSameTypeCrafting(ingredient, stack);
                }
            }
        }
        return false;
    }
}
