package mods.betterwithpatches.proxy;

import betterwithmods.event.TConHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.betterwithpatches.BWPRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.compat.minetweaker.util.MTHelper;
import mods.betterwithpatches.compat.nei.NEIBWMConfig;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.craft.KilnInteractionExtensions;
import mods.betterwithpatches.craft.SawInteractionExtensions;
import mods.betterwithpatches.craft.SteelCraftingManager;
import mods.betterwithpatches.event.PunitiveEvents;
import mods.betterwithpatches.features.*;
import mods.betterwithpatches.menu.BWPMenuHandler;
import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CommonProxy implements Proxy {

    @Override
    public void preInit() {
        Config.tryInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(BWPConstants.MODID, new BWPMenuHandler());

        if (Config.enableNEICompat && Loader.isModLoaded("NotEnoughItems")) {
            NEIBWMConfig nei = new NEIBWMConfig();
            ModContainer neiMC = Loader.instance().getIndexedModList().get("NotEnoughItems");
            if (neiMC.getVersion().contains("GTNH")) {
                nei.sendIMC("MillRecipeHandler", "bwm.mill", "betterwithmods:singleMachine:0", 3);
                nei.sendIMC("CauldronRecipeHandler", "bwm.cauldron", "betterwithmods:singleMachine:3", 2);
                nei.sendIMC("StokedCauldronRecipeHandler", "bwm.cauldronStoked", "betterwithmods:singleMachine:3", 2);
                nei.sendIMC("CrucibleRecipeHandler", "bwm.crucible", "betterwithmods:singleMachine:2", 2);
                nei.sendIMC("StokedCrucibleRecipeHandler", "bwm.crucibleStoked", "betterwithmods:singleMachine:2", 2);
                nei.sendIMC("KilnRecipeHandler", "bwm.kiln", "betterwithmods:kiln", 3);
                nei.sendIMC("TurntableRecipeHandler", "bwm.turntable", "betterwithmods:singleMachine:5", 3);
                nei.sendIMC("SteelAnvilRecipeHandler", "bwm.anvil", "betterwithmods:steelAnvil", 1);
            }
        }
    }

    @Override
    public void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BWPRegistry.steelAnvil), "sss", " s ", "sss", 's', "ingotSoulforgedSteel"));
        SteelCraftingManager.addSteelAnvilRecipes();

        if (Loader.isModLoaded("MineTweaker3")) {
            MTHelper.addMineTweakerCompat();
        }

        if (Config.HCTreestumps) {
            MinecraftForge.EVENT_BUS.register(new HCTreestumps());
        }

        if (Config.enablePenalties) {
            PunitiveEvents kinky = new PunitiveEvents();

            if (Config.HCArmor) {
                HCArmor.registerHCArmor();
            }
            if (Config.HCMovement) {
                HCMovement.registerHCMovement();
            }
        }
    }

    @Override
    public void postInit() {
        if (Config.patchHCWood) {
            HardcoreWoodInteractionExtensions.registerTannin();

            if (Config.patchSaw) {
                SawInteractionExtensions.setAdvancedEntityDrop(EntitySkeleton.class, SawInteractionExtensions::getSkeletonHead);
                SawInteractionExtensions.setEntityDrop(EntityZombie.class, true, Config.choppingBlockHeadDropChance, new ItemStack(Items.skull, 1, 2));
                SawInteractionExtensions.setEntityDrop(EntityCreeper.class, true, Config.choppingBlockHeadDropChance, new ItemStack(Items.skull, 1, 4));
                if (Config.forceChopPlayerHeads || (Loader.isModLoaded("TConstruct") && TConHelper.dropPlayerHeads)) {
                    SawInteractionExtensions.setAdvancedEntityDrop(EntityPlayer.class, SawInteractionExtensions::getPlayerHead);
                }
            }
        }

        if (Config.HCOres) HCOres.registerHCOres();

        if (Config.canKilnSmeltOres) KilnInteractionExtensions.addKilnOreRecipes();

        if (Config.HCFurnace) {
            HCFurnace.registerHCFurnaceEvents();
            HCFurnace.overrideCookingTime("oreIron", 1600);
            HCFurnace.overrideCookingTime("oreGold", 1600);
            HCFurnace.overrideCookingTime("cobblestone", 1600);
            HCFurnace.overrideCookingTime("sand", 1600);
        }
    }

    @Override
    public void registerRenderInformation() {

    }
}
