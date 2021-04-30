package net.frozenorb.foxtrot.server;

import com.mongodb.BasicDBObject;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class Deathban {

    private static Map<String, Integer> deathban = new LinkedHashMap<>();
    private static int defaultMinutes = 240;

    static {
    }

    public static void load(BasicDBObject object) {
        deathban.clear();

        for (String key : object.keySet()) {
            if (key.equals("DEFAULT"))  {
                defaultMinutes = object.getInt(key);
            } else {
                deathban.put(key, object.getInt(key));
            }
        }
    }

    public static int getDeathbanSeconds(Player player) {
        int minutes = defaultMinutes;

        Rank rank = AtmosphereShared.getInstance().getProfileManager().getProfile(player.getUniqueId()).getHighestGrantOnScope(SharedAPI.getServer(Bukkit.getServerName())).getRank();
        switch(rank.getId().toLowerCase()){
            case "owner":
            case "manager":
            case "developer":
            case "system-admin":
            case "platform-admin":
            case "senior-admin":
            case "admin":
            case "senior-mod":
            case "mod+":
            case "mod":
            case "trial-mod":
                minutes = 0;
                break;
            case "partner":
            case "famous":
            case "youtube":
                minutes = 5;
                break;
            case "builder":
            case "sky+":
                minutes = 10;
                break;
            case "sky":
                minutes = 15;
                break;
            case "diamond":
                minutes = 20;
                break;
            case "nitro":
            case "gold":
                minutes = 25;
                break;
            case "iron":
                minutes = 30;
                break;
            default:
                minutes = 45;
                break;
        }

        return (int) TimeUnit.MINUTES.toSeconds(minutes);
    }

}
