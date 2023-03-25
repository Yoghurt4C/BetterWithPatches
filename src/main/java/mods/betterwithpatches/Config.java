package mods.betterwithpatches;

import com.google.common.collect.ImmutableSet;
import mods.betterwithpatches.util.BWPConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static mods.betterwithpatches.util.BWPConstants.L;

public class Config {
    public static int lazyGeneratorDelay;
    private static boolean isInitialized = false;

    public static void tryInit() {
        if (!isInitialized) init();
    }

    private static void init() {
        String filename = BWPConstants.MODID + ".properties";
        ImmutableSet<? extends Entry<? extends Serializable>> entries = ImmutableSet.of(
                Entry.of("lazyGeneratorDelay", 100,
                        "The time (in ticks) it takes for a generator to recheck its validity and speed while active, but not strained. [Side: SERVER | Default: 100]")
        );
        if (Files.notExists(getConfigDir()) && !getConfigDir().toFile().mkdir()) {
            L.error("[" + BWPConstants.MODID + "] Can't reach the config directory. This is probably really bad.");
        }
        Path configPath = getConfigDir().resolve(filename);
        Map<String, String> cfg = new HashMap<>();
        try {
            boolean changed = false;
            File configurationFile = configPath.toFile();
            StringBuilder content = new StringBuilder().append("#").append(BWPConstants.MODNAME).append(" Configuration.\n");
            content.append("#Last generated at: ").append(new Date()).append("\n\n");
            if (Files.notExists(configPath) && !configurationFile.createNewFile())
                L.error("[" + BWPConstants.MODID + "] Can't create config file \"" + configurationFile + "\". This is probably bad.");
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
            L.fatal("[" + BWPConstants.MODID + "] Could not read/write config!");
            L.fatal(e);
        }
    }

    private static void logEntryError(File configurationFile, String key, Object value, String found, String expected) {
        L.error("[" + BWPConstants.MODID + "] Error processing configuration file \"" + configurationFile + "\".");
        L.error("[" + BWPConstants.MODID + "] Expected configuration value for " + key + " to be " + expected + ", found \"" + found + "\". Using default value \"" + value + "\" instead.");
        setCfgValue(key, value);
    }

    private static void setCfgValue(String k, Object v) {
        try {
            Config.class.getDeclaredField(k).set(Config.class, v);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            L.error("[" + BWPConstants.MODID + "] Could not set the runtime config state!");
            L.error(e);
        }
    }

    private static Path getConfigDir() {
        return Paths.get(".").resolve("config");
    }

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
