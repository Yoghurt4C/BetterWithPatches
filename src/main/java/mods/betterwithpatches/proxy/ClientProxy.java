package mods.betterwithpatches.proxy;

import cpw.mods.fml.common.Loader;
import mods.betterwithpatches.Config;
import mods.betterwithpatches.compat.BWPNatura;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
        if (Config.patchHCWood) {
            if (Loader.isModLoaded("Natura")) {
                BWPNatura.addBarkOverrides();
            }
        }
    }

    @Override
    public void registerRenderInformation() {
        super.registerRenderInformation();
    }
}
