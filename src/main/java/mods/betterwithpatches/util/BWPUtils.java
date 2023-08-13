package mods.betterwithpatches.util;

import betterwithmods.util.InvUtils;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public interface BWPUtils {
    static String getId(Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }

    static Block getBlock(Item item) {
        return item instanceof ItemBlock ? ((ItemBlock) item).field_150939_a : Block.getBlockFromItem(item);
    }

    static boolean isBlockReplaceable(World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z) || world.getBlock(x, y, z).isReplaceable(world, x, y, z);
    }

    static boolean presentInOD(ItemStack stack, String od) {
        return ArrayUtils.contains(OreDictionary.getOreIDs(stack), OreDictionary.getOreID(od));
    }

    static boolean presentInOD(ItemStack stack, String... od) {
        for (String s : od) {
            if (presentInOD(stack, s)) return true;
        }
        return false;
    }

    static boolean stackHasODPrefix(ItemStack stack, String prefix) {
        for (int oreID : OreDictionary.getOreIDs(stack)) {
            String name = OreDictionary.getOreName(oreID);
            if (name.startsWith(prefix)) return true;
        }
        return false;
    }

    static void offerOre(String name, Object... ores) {
        processOres(name, false, ores);
    }

    static void registerOre(String name, Object... ores) {
        processOres(name, true, ores);
    }

    static void processOres(String name, boolean force, Object... ores) {
        if (force || !OreDictionary.doesOreNameExist(name))
            for (Object ore : ores) {
                if (ore instanceof ItemStack) {
                    OreDictionary.registerOre(name, (ItemStack) ore);
                } else if (ore instanceof Item) {
                    OreDictionary.registerOre(name, new ItemStack((Item) ore));
                } else if (ore instanceof Block) {
                    OreDictionary.registerOre(name, new ItemStack((Block) ore));
                }
            }
    }

    static void copyInto(List<ItemStack> list, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            list.add(stack.copy());
        }
    }

    static void scatter(World world, int x, int y, int z, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            InvUtils.ejectStackWithOffset(world, x, y, z, stack.copy());
        }
    }

    static boolean stacksMatch(ItemStack stack1, ItemStack stack2) {
        return stack1 != null && stack2 != null &&
                stack1.getItem() == stack2.getItem() &&
                (stack1.getItemDamage() == stack2.getItemDamage() || stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack1.getItem().isDamageable());
    }

    static int offsetYDown(EntityLivingBase entity, float offset) {
        return (int) (entity.posY - (entity instanceof EntityClientPlayerMP ? (offset + entity.yOffset) : offset));
    }
}
