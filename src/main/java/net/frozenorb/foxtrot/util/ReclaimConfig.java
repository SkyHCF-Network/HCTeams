package net.frozenorb.foxtrot.util;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ReclaimConfig {
    private static File customConfigFile;
    private static FileConfiguration customConfig;
    public static FileConfiguration getConfig() {
        return customConfig;
    }

    public static void createit() {
        customConfigFile = new File(Foxtrot.getInstance().getDataFolder(), "reclaim.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            Foxtrot.getInstance().saveResource("reclaim.yml", false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void reload(){
        createit();
    }
    public static void save() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
