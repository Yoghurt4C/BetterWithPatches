package mods.betterwithpatches;

import com.google.common.collect.ImmutableSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static mods.betterwithpatches.util.BWPConstants.*;

public class Config {
    public static int lazyGeneratorDelay, lazyCauldronDelay, hcWoodPlankLoss, choppingBlockHeadDropChance, hcFurnaceDefaultCookTime;
    public static float hcMovementDefault, hcMovementFast;
    public static boolean
            genericFixes, patchKiln, canKilnSmeltOres, patchTurntable, patchHCWood, HCTreestumps, patchSaw, forceChopPlayerHeads, dirtyStokedFlameFix, patchCookingPot, patchHCBuckets, patchCreativeTabs, enableNEICompat, patchSignPicForLwjglify, furnaceHCGunpowder,
            vanillaRecipesInAnvil, chainmailArmorRecipe,
            HCFurnace, hcFurnaceTooltip, hcFurnaceCustomFuel, HCOres, hcOreDusts,
            enablePenalties, HCArmor, HCMovement, removeSpeedPenaltyFOVChanges;
    private static boolean isInitialized = false;

    public static void tryInit() {
        if (!isInitialized) init();
    }

    private static void init() {
        String filename = MODID + ".properties";
        ImmutableSet<Entry<?>> entries = ImmutableSet.of(
                Entry.of("genericFixes", true,
                        "Crash safeguards and minor tweaks. The more intrusive patches should have their own entries. [Side: BOTH | Default: true]"),
                Entry.of("lazyGeneratorDelay", 100,
                        "The time (in ticks) it takes for a generator to recheck its validity and speed while active, but not strained. Depends on \"genericFixes\". [Side: SERVER | Default: 100]"),
                Entry.of("lazyCauldronDelay", 100,
                        "The time (in ticks) it takes for a cauldron to lazily check whether it's over a heat source (only when active). Depends on \"genericFixes\". [Side: SERVER | Default: 100]"),
                Entry.of("patchKiln", true,
                        "Various additions and fixes to the Kiln. Required for recipe manipulation. [Side: BOTH | Default: true]"),
                Entry.of("canKilnSmeltOres", true,
                        "Enables attempted theft of smelting recipes for Ores inside the Kiln."),
                Entry.of("patchTurntable", true,
                        "Various additions and fixes to the Turntable. Required for recipe manipulation. [Side: BOTH | Default: true]"),
                Entry.of("patchHCWood", true,
                        "Replaces most of the Hardcore Wood feature set. Bark is now dynamic and can drop from any block in logWood. [Side: BOTH | Default: true]"),
                Entry.of("hcWoodPlankLoss", 1,
                        "How many planks are lost when you break logs inappropriately. If set too low (4 in vanilla), planks won't drop at all. Setting to 0 disables plank loss. Depends on \"patchHCWood\". [Side: SERVER | Default: 1]"),
                Entry.of("HCTreestumps", true,
                        "Makes Log Blocks take much longer to break if they're standing on anything that resembles soil. [Side: BOTH | Default: true]"),
                Entry.of("patchSaw", true,
                        "Various additions and fixes to the Saw. Unhardcodes most of the behaviours, allows for defining custom drops for Blocks and Entities. Depends on \"patchHCWood\". [Side: BOTH | Default: true]"),
                Entry.of("choppingBlockHeadDropChance", 33,
                        "You can use this to manipulate the chance for vanilla heads to drop when a Mob gets Sawed to death on a Chopping Block. This ONLY affects default head drops defined by BetterWithPatches. [Side: SERVER | Default: 33]"),
                Entry.of("forceChopPlayerHeads", false,
                        "Enables chopping off other Players' heads using the Saw regardless of whether Tinkers Construct is present and its \"Players drop heads on death\" config option is enabled. [Side: Server | Default: false]"),
                Entry.of("dirtyStokedFlameFix", true,
                        "Extends the lifespan of Stoked Flames to hide the weird gaps in the current update system. [Side: SERVER | Default: true]"),
                Entry.of("patchCookingPot", true,
                        "Various additions and fixes to the Cauldron and Crucible. Patches the GUI to have an indicator of being Stoked. [Side: BOTH | Default: true]"),
                Entry.of("patchHCBuckets", true,
                        "Makes Hardcore Buckets actually work in a modded environment. Forces HCBuckets to be on if it's disabled in the BWM Config. [Side: SERVER | Default: true]"),
                Entry.of("patchCreativeTabs", true,
                        "Moves every BWM item into a separate creative tab added by BWP. If false, only things added by BWP use it. [Side: CLIENT | Default: true]"),
                Entry.of("enableNEICompat", true,
                        "Adds recipe views for NotEnoughItems. [Side: BOTH | Default: true]"),
                Entry.of("patchSignPicForLwjglify", true,
                        "LWJGL3ify seems to cause the SignPicture upload overlay to constantly trigger during normal play, this patch simply removes the check that causes it. [Side: CLIENT | Default: true]"),
                Entry.of("vanillaRecipesInAnvil", true,
                        "If true, the Soulforged Steel Anvil will attempt to match vanilla recipes in its grid. [Side: BOTH | Default: true]"),
                Entry.of("chainmailArmorRecipe", true,
                        "Replaces the broken vanilla recipes for Chainmail Armor with functional ones."),
                Entry.of("furnaceHCGunpowder", true,
                        "Makes Gunpowder explode when put inside a lit furnace. [Side: SERVER | Default: true]"),
                Entry.of("HCFurnace", false,
                        "Allows specifying cook times for items and changes vanilla fuel values. [Side: SERVER | Default: false]"),
                Entry.of("hcFurnaceDefaultCookTime", 200,
                        "Default number of ticks it takes to cook an item. [Side: SERVER | Default: 200]"),
                Entry.of("hcFurnaceCustomFuel", true,
                        "Overrides vanilla burn times for specific items. [Side: SERVER | Default: true]"),
                Entry.of("hcFurnaceTooltip", true,
                        "Adds furnace data to items with modified cook time. [Side: CLIENT | Default: true]"),
                Entry.of("HCOres", false,
                        "Ores only smelt into a single Nugget, making it much harder to create large amounts of metal.\nAdditionally, this feature changes the recipes of Compasses, Clocks, Buckets, and Flint and Steels to better fit the nugget smelting."),
                Entry.of("hcOreDusts", false,
                        "Makes dusts smelt into nuggets. This will probably break even more mods than HCOres itself."),
                Entry.of("enablePenalties", false,
                        "Adds hooks to support various Hardcore Features, like HCArmor. Somewhat performance intensive. [Side: BOTH | Default: false]"),
                Entry.of("removeSpeedPenaltyFOVChanges", true,
                        "Counteracts FOV changes caused by Speed Penalties. [Side: CLIENT | Default: true]"),
                Entry.of("HCArmor", true,
                        "Gives armor weights that affect movement. Depends on \"enablePenalties\". [Side: BOTH | Default: true]"),
                Entry.of("HCMovement", true,
                        "Changes player walking speed depending on the block underfoot. Depends on \"enablePenalties\". [Side: BOTH | Default: true]"),
                Entry.of("hcMovementDefault", 0.75f,
                        "Default walking speed for unknown materials; Changing this won't modify the \"actual\" default walking speed. [Side: BOTH | Default: 0.75]"),
                Entry.of("hcMovementFast", 1.2f,
                        "Default, faster walking speed for quality roads. [Side: BOTH | Default: 1.2]")
        );
        if (Files.notExists(getConfigDir()) && !getConfigDir().toFile().mkdir()) {
            L.error("Can't reach the config directory. This is probably really bad.");
        }
        Path configPath = getConfigDir().resolve(filename);
        Map<String, String> cfg = new HashMap<>();
        try {
            boolean changed = false;
            File configurationFile = configPath.toFile();
            StringBuilder content = new StringBuilder().append("#").append(MODNAME).append(" Configuration.\n");
            content.append("#Last generated at: ").append(new Date()).append("\n\n");
            if (Files.notExists(configPath) && !configurationFile.createNewFile())
                L.error("Can't create config file \"" + configurationFile + "\". This is probably bad.");
            BufferedReader r = Files.newBufferedReader(configPath, StandardCharsets.UTF_8);

            String line;
            while ((line = r.readLine()) != null) {
                if (line.startsWith("#") || line.isEmpty()) continue;
                String[] kv = line.split("=");
                if (kv.length == 2) cfg.put(kv[0], kv[1]);
            }
            r.close();

            for (Entry<?> entry : entries) {
                String key = entry.key;
                Object value = entry.value;
                Class<?> cls = entry.cls;
                if (cfg.containsKey(key)) {
                    String s = cfg.get(key);
                    if (s.equals("")) {
                        logEntryError(configurationFile, key, value, "nothing", "present");
                    } else if (cls.equals(Integer.class)) {
                        try {
                            setCfgValue(key, Integer.parseInt(s));
                        } catch (NumberFormatException e) {
                            logEntryError(configurationFile, key, value, s, "an integer");
                        }
                    } else if (cls.equals(Float.class)) {
                        try {
                            setCfgValue(key, Float.parseFloat(s));
                        } catch (NumberFormatException e) {
                            logEntryError(configurationFile, key, value, s, "a float");
                        }
                    } else if (cls.equals(Boolean.class)) {
                        if (!"true".equalsIgnoreCase(s) && !"false".equalsIgnoreCase(s)) {
                            logEntryError(configurationFile, key, value, s, "a boolean");
                        } else setCfgValue(key, Boolean.parseBoolean(s));
                    } else if (cls.equals(String.class)) {
                        setCfgValue(key, s);
                    }
                } else {
                    changed = true;
                    cfg.put(key, value.toString());
                    setCfgValue(key, value);
                }
                content.append("#").append(key).append(": ").append(entry.comment.replace("\n", "\n#")).append("\n");
                content.append(key).append("=").append(cfg.get(key)).append("\n");
            }
            if (changed) {
                Files.write(configPath, Collections.singleton(content.toString()), StandardCharsets.UTF_8);
            }
            isInitialized = true;
        } catch (IOException e) {
            L.fatal("Could not read/write config!");
            L.fatal(e);
        }
    }

    private static void logEntryError(File configurationFile, String key, Object value, String found, String expected) {
        L.error("Error processing configuration file \"" + configurationFile + "\".");
        L.error("Expected configuration value for " + key + " to be " + expected + ", found \"" + found + "\". Using default value \"" + value + "\" instead.");
        setCfgValue(key, value);
    }

    private static void setCfgValue(String k, Object v) {
        try {
            Config.class.getDeclaredField(k).set(Config.class, v);
        } catch (NoSuchFieldException e) {
            L.error("There's an odd entry in the config. Please report this to the author.");
            L.error(e);
        } catch (IllegalAccessException e) {
            L.error("Could not set the runtime config state!");
            L.error(e);
        }
    }

    private static Path getConfigDir() {
        return Paths.get(".", "config");
    }

    @SuppressWarnings("unused")
    private static class Entry<T> {
        private final String key;
        private final T value;
        private final String comment;
        private final Class<T> cls;

        private Entry(String key, T value, String comment, Class<T> cls) {
            this.key = key;
            this.value = value;
            this.comment = comment;
            this.cls = cls;
        }

        public static Entry<Integer> of(String key, int value, String comment) {
            return new Entry<>(key, value, comment, Integer.class);
        }

        public static Entry<Float> of(String key, float value, String comment) {
            return new Entry<>(key, value, comment, Float.class);
        }

        public static Entry<Boolean> of(String key, boolean value, String comment) {
            return new Entry<>(key, value, comment, Boolean.class);
        }

        public static Entry<String> of(String key, String value, String comment) {
            return new Entry<>(key, value, comment, String.class);
        }
    }
}
