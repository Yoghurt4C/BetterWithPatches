package mods.betterwithpatches.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.client.RenderSteelAnvil;
import mods.betterwithpatches.data.BWPCreativeTab;
import mods.betterwithpatches.features.HCMovement;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public static final CreativeTabs bwpTab = new BWPCreativeTab();
    public static int renderAnvil;

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();

        renderAnvil = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderAnvil, new RenderSteelAnvil());

        if (Config.enablePenalties) {
            if (Config.HCMovement) MinecraftForge.EVENT_BUS.register(new HCMovement.HCMovementFOV());
        }
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    public void registerRenderInformation() {
        super.registerRenderInformation();
    }
}
