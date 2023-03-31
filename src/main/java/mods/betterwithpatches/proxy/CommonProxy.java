package mods.betterwithpatches.proxy;

import betterwithmods.BWCrafting;
import betterwithmods.BWRegistry;
import cpw.mods.fml.common.registry.GameData;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.nei.NEIBWMConfig;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import static mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions.overrides;

public class CommonProxy implements Proxy {
    @Override
    public void preInit() {
        Config.tryInit();
        new NEIBWMConfig();
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
        int[] defaultMeta = new int[]{0, 1, 2, 3};
        for (ItemStack log : OreDictionary.getOres("logWood")) {
            String id = GameData.getBlockRegistry().getNameForObject(((ItemBlock) log.getItem()).field_150939_a);
            int[] iterable = overrides.getOrDefault(id, defaultMeta);
            for (int i : iterable) {
                int tannin = HardcoreWoodInteractionExtensions.tannin.getOrDefault(id + "@" + i, 8);
                ItemStack stack = new ItemStack(BWRegistry.bark, tannin, 0);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("logId", id);
                tag.setInteger("logMeta", i);
                stack.setTagCompound(tag);

                BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 1, 6), new Object[]{new ItemStack(BWRegistry.material, 1, 7), stack});
                BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 2, 33), new Object[]{new ItemStack(BWRegistry.material, 2, 34), stack});
            }
        }
    }

    @Override
    public void registerRenderInformation() {

    }
}
