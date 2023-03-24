package mods.betterwithpatches;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mods.betterwithpatches.proxy.CommonProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static mods.betterwithpatches.BetterWithPatches.MODID;
import static mods.betterwithpatches.BetterWithPatches.MODNAME;

@Mod(modid = MODID, name = MODNAME, version = "${version}", dependencies = "required-after:betterwithmods")
public class BetterWithPatches {
    public static final String MODID = "betterwithpatches", MODNAME = "BetterWithPatches";
    public static Logger L = LogManager.getLogger(MODNAME);

    public static String getModId() {
        return MODID;
    }

    @SidedProxy(clientSide = "mods.betterwithpatches.proxy.ClientProxy", serverSide = "mods.betterwithpatches.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        PROXY.init();
    }

    @Mod.EventHandler
    public void postInit(FMLInitializationEvent e) { PROXY.postInit(); }
}
