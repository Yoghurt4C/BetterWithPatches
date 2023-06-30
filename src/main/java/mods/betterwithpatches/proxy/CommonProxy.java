package mods.betterwithpatches.proxy;

import betterwithmods.event.TConHelper;
import cpw.mods.fml.common.Loader;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.compat.minetweaker.util.MTHelper;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.craft.SawInteractionExtensions;
import mods.betterwithpatches.nei.NEIBWMConfig;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommonProxy implements Proxy {
    @Override
    public void preInit() {
        Config.tryInit();
        if (Loader.isModLoaded("NotEnoughItems")) {
            new NEIBWMConfig();
        }
    }

    @Override
    public void init() {
        if (Loader.isModLoaded("MineTweaker3")) {
            MTHelper.addMineTweakerCompat();
        }
    }

    @Override
    public void postInit() {
        if (Config.patchHCWood) {
            HardcoreWoodInteractionExtensions.addVanillaTanninOverrides();

            List<ItemStack> logs = new ArrayList<>();
            List<ItemStack> temp = new ArrayList<>();
            int ore = OreDictionary.getOreID("logWood");
            for (Object o : Item.itemRegistry) {
                Item thing = (Item) o;
                if (thing instanceof ItemBlock) {
                    Block block = BWPConstants.getBlock(thing);
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
                String id = BWPConstants.getId(BWPConstants.getBlock(log.getItem()));
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
                    HardcoreWoodInteractionExtensions.registerTanninRecipe(entry.getKey(), point);
                }

                HardcoreWoodInteractionExtensions.overrideLogMeta(entry.getKey(), meta);
            }
            map.clear();

            if (Config.patchSaw) {
                SawInteractionExtensions.setAdvancedEntityDrop(EntitySkeleton.class, SawInteractionExtensions::getSkeletonHead);
                SawInteractionExtensions.setEntityDrop(EntityZombie.class, true, Config.choppingBlockHeadDropChance, new ItemStack(Items.skull, 1, 2));
                SawInteractionExtensions.setEntityDrop(EntityCreeper.class, true, Config.choppingBlockHeadDropChance, new ItemStack(Items.skull, 1, 4));
                if (Config.forceChopPlayerHeads || (Loader.isModLoaded("TConstruct") && TConHelper.dropPlayerHeads)) {
                    SawInteractionExtensions.setAdvancedEntityDrop(EntityPlayer.class, SawInteractionExtensions::getPlayerHead);
                }
            }
        }
    }

    @Override
    public void registerRenderInformation() {

    }
}
