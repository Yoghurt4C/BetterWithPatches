package mods.betterwithpatches.proxy;

import mods.betterwithpatches.Config;
import mods.betterwithpatches.features.HCMovement;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();

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
