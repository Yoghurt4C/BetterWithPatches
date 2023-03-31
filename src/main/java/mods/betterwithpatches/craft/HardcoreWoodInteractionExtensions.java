package mods.betterwithpatches.craft;

import mods.betterwithpatches.util.BWPConstants;
import net.minecraft.item.ItemStack;

import java.util.Hashtable;

public interface HardcoreWoodInteractionExtensions {
    Hashtable<String, int[]> overrides = new Hashtable<>();
    Hashtable<String, ItemStack[]> woodProducts = new Hashtable<>();//todo implement
    Hashtable<String, Integer> tannin = new Hashtable<>();

    /**
     * @param logId Identifier of the log, e.g. "minecraft:log2"
     * @param meta  Ints specifying the meta values for the override.
     *              Upright vanilla logs use [0, 1, 2, 3], which is the default.
     *              However, minecraft:log2 only uses [0, 1], so it needs an override.
     *              This exists to *specifically* define edge cases like that.
     */
    static void overrideLogMeta(String logId, int... meta) {
        if (logId.contains(":")) overrides.put(logId, meta);
        else
            BWPConstants.L.warn("Tried to add a Hardcore Wood Bark Override for {}, which isn't a valid identifier and will be skipped!", logId);
    }

    /**
     * @param logId  Identifier of the log, e.g. "minecraft:log2"
     * @param meta   Meta for the log, usually ranges from 0 to 3.
     * @param amount Amount of bark required for Tanning recipes in the Cauldron. Defaults to 8 if not overridden.
     */
    static void overrideTanninAmount(String logId, int meta, int amount) {
        if (logId.contains(":")) tannin.put(logId + "@" + meta, amount);
        else
            BWPConstants.L.warn("Tried to add a Hardcore Wood Tannin Override for {}, which isn't a valid identifier and will be skipped!", logId);
    }

    static void addVanillaLogOverrides() {
        overrideLogMeta("minecraft:log2", 0, 1);
    }

    static void addVanillaTanninOverrides() {
        overrideTanninAmount("minecraft:log", 0, 5);
        overrideTanninAmount("minecraft:log", 1, 3);
        overrideTanninAmount("minecraft:log", 2, 8);
        overrideTanninAmount("minecraft:log", 3, 2);
        overrideTanninAmount("minecraft:log2", 0, 4);
        overrideTanninAmount("minecraft:log2", 1, 2);
    }
}
