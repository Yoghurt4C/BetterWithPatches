package mods.betterwithpatches.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.client.RenderSteelAnvil;
import mods.betterwithpatches.craft.HardcoreWoodInteractionExtensions;
import mods.betterwithpatches.features.HCMovement;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
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
            if (Config.HCMovement && Config.removeSpeedPenaltyFOVChanges)
                MinecraftForge.EVENT_BUS.register(new HCMovement.HCMovementFOV());
        }
    }

    @Override
    public void postInit() {
        super.postInit();
        HardcoreWoodInteractionExtensions.fillDisplayMap();
    }

    @Override
    public void registerRenderInformation() {
        super.registerRenderInformation();
    }
}
