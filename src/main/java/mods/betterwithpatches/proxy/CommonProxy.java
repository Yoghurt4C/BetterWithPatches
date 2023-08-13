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
import mods.betterwithpatches.craft.*;
import mods.betterwithpatches.event.PunitiveEvents;
import mods.betterwithpatches.features.*;
import mods.betterwithpatches.menu.BWPMenuHandler;
import mods.betterwithpatches.util.BWPConstants;
import mods.betterwithpatches.util.BWPUtils;
import mods.betterwithpatches.util.RecipeUtils;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static mods.betterwithpatches.util.BWMaterials.*;
import static mods.betterwithpatches.util.BWMaterials.getMaterial;

public class CommonProxy implements Proxy {
    public static final boolean isMTPresent = Loader.isModLoaded("MineTweaker3");

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
        GameRegistry.addRecipe(new RecipeMultiRenderPassArmorDyes());

        if (Config.chainmailArmorRecipe) {
            RecipeUtils.removeRecipes(Items.chainmail_helmet, Items.chainmail_chestplate, Items.chainmail_leggings, Items.chainmail_boots);
            OreDictionary.registerOre("chainmail", getMaterial(CHAINMAIL));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_helmet), "CCC", "C C", 'C', "chainmail"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_chestplate), "C C", "CCC", "CCC", 'C', "chainmail"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_leggings), "CCC", "C C", "C C", 'C', "chainmail"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_boots), "C C", "C C", 'C', "chainmail"));
        }

        BWPUtils.offerOre("feather", Items.feather);
        BWPUtils.registerOre("fabricHemp", getMaterial(HEMP_CLOTH));
        GameRegistry.addRecipe(new ShapelessOreRecipe(getMaterial(PADDING), "feather", "fabricHemp"));

        BWPUtils.offerOre("paper", Items.paper);
        BWPUtils.offerOre("string", Items.string);
        BWPUtils.registerOre("hideTanned", getMaterial(TANNED_LEATHER), getMaterial(TANNED_LEATHER_CUT));
        BWPUtils.registerOre("hideScoured", getMaterial(SCOURED_LEATHER), getMaterial(SCOURED_LEATHER_CUT));
        BWPUtils.registerOre("hideBelt", getMaterial(LEATHER_BELT));
        BWPUtils.registerOre("hideStrap", getMaterial(LEATHER_STRAP));

        if (Config.patchFilteredHopper) FilteredHopperInteractions.registerFiltersAndRecipes();

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

        if (isMTPresent) {
            MTHelper.addMineTweakerCompat();
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
    public void afterInit() {
        String[] dyes = {
                "dyeBlack",
                "dyeRed",
                "dyeGreen",
                "dyeBrown",
                "dyeBlue",
                "dyePurple",
                "dyeCyan",
                "dyeLightGray",
                "dyeGray",
                "dyePink",
                "dyeLime",
                "dyeYellow",
                "dyeLightBlue",
                "dyeMagenta",
                "dyeOrange",
                "dyeWhite"
        };
        for (int i = 0; i < dyes.length; i++) {
            BWPConstants.dyeOreIds[i] = OreDictionary.getOreID(dyes[i]);
        }
    }
}
