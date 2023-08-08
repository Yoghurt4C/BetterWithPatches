package mods.betterwithpatches.craft;

import betterwithmods.BWCrafting;
import betterwithmods.BWRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.betterwithpatches.util.BWMaterials;
import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.BWPUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

import static mods.betterwithpatches.util.BWPUtils.getId;

public interface HardcoreWoodInteractionExtensions {
    @SideOnly(Side.CLIENT)
    Map<String, int[]> displayMap = new LinkedHashMap<>();
    Hashtable<String, ItemStack[]> barkOverrides = new Hashtable<>();
    Hashtable<String, Integer> tannin = new Hashtable<>();

    static void addBlock(Block block, ItemStack... barkOverride) {
        barkOverrides.put(getId(block), barkOverride);

    }

    static void addBlock(Block block, int meta, ItemStack... barkOverride) {
        barkOverrides.put(getId(block) + "@" + meta, barkOverride);
    }

    static boolean contains(Block block, int meta) {
        String identifier = getId(block);
        if (barkOverrides.containsKey(identifier)) return true;
        else return barkOverrides.containsKey(identifier + "@" + meta);
    }

    /**
     * @return Specific override for bark in case you don't want some log to drop any bark (or define a custom stacksize for it).
     * The overrides have to be "registered" in {@link HardcoreWoodInteractionExtensions#barkOverrides}.
     * Don't forget to use {@link HardcoreWoodInteractionExtensions#getBarkTagForLog(Block, int)} if you're going to use Bark in overrides!
     */
    static ItemStack[] getBarkOverrides(Block block, int meta) {
        String identifier = getId(block);
        ItemStack[] stacks = barkOverrides.get(identifier);
        if (stacks == null) stacks = barkOverrides.get(identifier + "@" + meta);
        return stacks;
    }

    /**
     * @return Bark data for a specific Log. Mainly used internally.
     */
    static NBTTagCompound getBarkTagForLog(Block block, int meta) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("logId", getId(block));
        tag.setInteger("logMeta", meta);
        return tag;
    }

    @SideOnly(Side.CLIENT)
    /**
     * @param logId Identifier of the log, e.g. "minecraft:log2"
     * @param meta  Ints specifying the meta values for the override.
     *              Upright vanilla logs use [0, 1, 2, 3], which is the default.
     *              However, minecraft:log2 only uses [0, 1], so it needs an override.
     *              This exists to *specifically* define edge cases like that.
     */
    static void displayLogMeta(String logId, int... meta) {
        if (logId.contains(":")) displayMap.put(logId, meta);
        else
            BWPConstants.L.warn("Tried to add a Hardcore Wood Bark Override for {}, which isn't a valid identifier and will be skipped!", logId);
    }

    static void displayLogMeta(String modId, String logId, int... meta) {
        displayMap.put(modId + ":" + logId, meta);
    }

    /**
     * @param logId  Identifier of the log, e.g. "minecraft:log2"
     * @param meta   Meta for the log, usually ranges from 0 to 3.
     * @param amount Amount of bark required for Tanning recipes in the Cauldron. Defaults to 8 if not overridden.
     */
    static void overrideTanninAmount(String logId, int meta, int amount) {
        if (logId.contains(":")) tannin.put(logId + "@" + meta, amount);
        else
            BWPConstants.L.warn("Tried to add a Hardcore Wood Tannin Override for {}, which isn't a valid identifier and will be skipped!", logId);
    }

    static void overrideTanninAmount(String modid, String logId, int meta, int amount) {
        tannin.put(modid + ":" + logId + "@" + meta, amount);
    }

    static int getTanninAmount(String logId, int meta) {
        String joint = logId + "@" + meta;
        return tannin.containsKey(joint) ? tannin.get(logId + "@" + meta) : getDefaultTanninAmount(logId, meta);
    }

    static int getTanninAmount(ItemStack stack) {
        NBTTagCompound tag = stack.stackTagCompound;
        String id = tag.getString("logId");
        int meta = tag.getInteger("logMeta");
        return getTanninAmount(id, meta);
    }

    static int getDefaultTanninAmount(String id, int meta) {
        String[] splitId = id.split(":");
        int amt = MathHelper.clamp_int(splitId[0].length() & 7 - (splitId[1].length() & 5) + (meta & 3), 2, 10);
        overrideTanninAmount(id, meta, amt);
        return amt;
    }

    static void registerTannin() {
        overrideTanninAmount("minecraft:log", 0, 5);
        overrideTanninAmount("minecraft:log", 1, 3);
        overrideTanninAmount("minecraft:log", 2, 8);
        overrideTanninAmount("minecraft:log", 3, 2);
        overrideTanninAmount("minecraft:log2", 0, 4);
        overrideTanninAmount("minecraft:log2", 1, 2);

        ItemStack stack = new ItemStack(BWRegistry.bark, 1, 32767);
        BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 1, 6), new Object[]{BWMaterials.getMaterial(BWMaterials.SCOURED_LEATHER), stack});
        BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 2, 33), new Object[]{BWMaterials.getMaterial(BWMaterials.SCOURED_LEATHER_CUT, 2), stack});
    }

    @SideOnly(Side.CLIENT)
    static void fillDisplayMap() {
        List<ItemStack> logs = new ArrayList<>();
        List<ItemStack> temp = new ArrayList<>();
        int ore = OreDictionary.getOreID("logWood");
        for (Object o : Item.itemRegistry) {
            Item thing = (Item) o;
            if (thing instanceof ItemBlock) {
                Block block = BWPUtils.getBlock(thing);
                for (CreativeTabs creativeTab : thing.getCreativeTabs()) {
                    block.getSubBlocks(thing, creativeTab, temp);
                }
                temp.removeIf(stack -> !ArrayUtils.contains(OreDictionary.getOreIDs(stack), ore));
                logs.addAll(temp);
                temp.clear();
            }
        }

        Map<String, List<Integer>> map = new LinkedHashMap<>();
        for (ItemStack log : logs) {
            String id = BWPUtils.getId(BWPUtils.getBlock(log.getItem()));
            if (map.containsKey(id)) {
                map.get(id).add(log.getItemDamage());
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(log.getItemDamage());
                map.put(id, list);
            }
        }
        logs.clear();

        for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
            int[] meta = new int[entry.getValue().size()];
            for (int i = 0; i < entry.getValue().size(); i++) {
                int point = entry.getValue().get(i);
                meta[i] = point;
            }

            HardcoreWoodInteractionExtensions.displayLogMeta(entry.getKey(), meta);
        }
        map.clear();
    }
}
