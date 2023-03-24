package mods.betterwithpatches.proxy;

import betterwithmods.craft.CraftingManagerMill;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.nei.NEIBWMConfig;
import mods.betterwithpatches.nei.machines.MillRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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
        //todo delete
        CraftingManagerMill.getInstance().addRecipe(new ItemStack[]{new ItemStack(Items.diamond), new ItemStack(Items.record_11), new ItemStack(Items.baked_potato)},
                new ItemStack[]{new ItemStack(Blocks.dirt), new ItemStack(Items.carrot_on_a_stick)});
    }

    @Override
    public void registerRenderInformation() {

    }
}
