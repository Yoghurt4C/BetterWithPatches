package mods.betterwithpatches.proxy;

import betterwithmods.BWCrafting;
import betterwithmods.BWRegistry;
import betterwithmods.event.TConHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.compat.BWPModCompat;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.craft.SawInteractionExtensions;
import mods.betterwithpatches.nei.NEIBWMConfig;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import static mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions.metaOverrides;

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
            BWPModCompat.addMineTweakerCompat();
        }
    }

    @Override
    public void postInit() {
        if (Config.patchHCWood) {
            BWPModCompat.addNatureBarkOverrides();
            BWPModCompat.addEBXLBarkOverrides();
            BWPModCompat.addThaumcraftBarkOverrides();

            int[] defaultMeta = new int[]{0, 1, 2, 3};
            for (ItemStack log : OreDictionary.getOres("logWood")) {
                String id = GameData.getBlockRegistry().getNameForObject(((ItemBlock) log.getItem()).field_150939_a);
                int[] iterable = metaOverrides.getOrDefault(id, defaultMeta);
                for (int i : iterable) {
                    int tannin = HardcoreWoodInteractionExtensions.getTanninAmount(id, i);
                    ItemStack stack = new ItemStack(BWRegistry.bark, tannin, 0);
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("logId", id);
                    tag.setInteger("logMeta", i);
                    stack.setTagCompound(tag);

                    BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 1, 6), new Object[]{new ItemStack(BWRegistry.material, 1, 7), stack});
                    BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 2, 33), new Object[]{new ItemStack(BWRegistry.material, 2, 34), stack});
                }
            }

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
