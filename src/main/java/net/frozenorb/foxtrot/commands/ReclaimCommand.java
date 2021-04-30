/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.ReclaimConfig;
import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.shared.AtmosphereShared;
import net.skyhcf.atmosphere.shared.SharedAPI;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ReclaimCommand {

    @Command(names = {"reclaim"}, permission = "")
    public static void oncommand(Player p) {

        Profile profile = AtmosphereShared.getInstance().getProfileManager().getProfile(p.getUniqueId());
        ReclaimConfig.reload();
        if(ReclaimConfig.getConfig().getStringList("reclaim." + profile.getHighestRank().getId().toLowerCase()) == null){
            p.sendMessage(BukkitChat.format("&cYour rank does not have a reclaim. You may purchase a rank at https://store.skyhcf.net."));
            return;
        }
        List<String> commands = ReclaimConfig.getConfig().getStringList("reclaim." + profile.getHighestRank().getId().toLowerCase());
        if(commands.size() == 0){
            p.sendMessage(BukkitChat.format("&cYour rank does not have a reclaim. You may purchase a rank at https://store.skyhcf.net."));
            return;
        }
        List<String> reclaimed = ReclaimConfig.getConfig().getStringList("reclaimed-users");
        if(reclaimed.contains(p.getUniqueId().toString())){
            p.sendMessage(BukkitChat.format("&cYou have already used your reclaim this map."));
            return;
        }
        reclaimed.add(profile.getUuid().toString());
        ReclaimConfig.getConfig().set("reclaimed-users", reclaimed);
        ReclaimConfig.save();
        for(String cmd : commands){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", p.getName()));
        }
        Bukkit.broadcastMessage(BukkitChat.format("&r &7* &r" + profile.getHighestRank().getColor() + profile.getUsername() + "&r &rreclaimed their " + profile.getHighestGrantOnScope(SharedAPI.getServer(Bukkit.getServerName())).getRank().getColor() + profile.getHighestGrantOnScope(SharedAPI.getServer(Bukkit.getServerName())).getRank().getDisplayName() + "&r &rrank using &b/reclaim&r."));
    }
}
