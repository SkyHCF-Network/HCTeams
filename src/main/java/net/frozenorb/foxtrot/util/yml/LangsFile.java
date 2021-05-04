/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.util.yml;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class LangsFile extends YamlConfiguration {

    private static LangsFile config;
    private final Plugin plugin;
    private final java.io.File configFile;

    public LangsFile() {
        this.plugin = main();
        this.configFile = new java.io.File(this.plugin.getDataFolder(), "langs.yml");
        saveDefault();
        reload();
    }

    public static LangsFile getConfig() {
        if (config == null) {
            config = new LangsFile();
        }
        return config;
    }

    private Plugin main() {
        return Foxtrot.getInstance();
    }

    public void reload() {
        try {
            super.load(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDefault() {
        this.plugin.saveResource("langs.yml", false);
    }
}


