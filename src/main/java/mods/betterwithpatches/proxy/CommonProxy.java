package mods.betterwithpatches.proxy;

import mods.betterwithpatches.Config;
import mods.betterwithpatches.nei.NEIBWMConfig;

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
    }

    @Override
    public void registerRenderInformation() {

    }
}
