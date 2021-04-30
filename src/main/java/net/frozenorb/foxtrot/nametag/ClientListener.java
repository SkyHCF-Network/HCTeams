/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.nametag;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import com.cheatbreaker.api.object.MinimapStatus;
import com.cheatbreaker.nethandler.obj.ServerRule;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.moonsworth.client.nethandler.server.LCPacketTeammates;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.nametag.FoxtrotNametagProvider;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientListener implements Listener {


    public ClientListener() {
        Bukkit.getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {
            for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                    CheatBreakerAPI.getInstance().overrideNametag(onlinePlayer, fetchNametag(onlinePlayer, player), player);
                    LunarClientAPI.getInstance().overrideNametag(onlinePlayer, fetchNametag(onlinePlayer, player), player);
                });
            }
        }, 0, 40);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
        CheatBreakerAPI.getInstance().changeServerRule(player, ServerRule.SERVER_HANDLES_WAYPOINTS, true);
        CheatBreakerAPI.getInstance().changeServerRule(player, com.moonsworth.client.nethandler.obj.ServerRule.SERVER_HANDLES_WAYPOINTS, true);
        CheatBreakerAPI.getInstance().setMinimapStatus(player, MinimapStatus.FORCED_OFF);
        CheatBreakerAPI.getInstance().sendWaypoint(player, new CBWaypoint("Spawn", player.getWorld().getSpawnLocation(), Color.fromBGR(12, 249, 31).asRGB(), true, true));


    }


    public List<String> fetchNametag(Player target, Player viewer) {
        String nameTag = (target.hasMetadata("invisible") ? ChatColor.GRAY + "*" : "") + new FoxtrotNametagProvider().fetchNametag(target, viewer).getPrefix() + target.getName();
        List<String> tag = new ArrayList<>();
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(target);
        //FancyMessage dtrMessage = new FancyMessage(ChatColor.YELLOW + "DTR: " + getDTRColor(team) + Team.DTR_FORMAT.format(Team.getDTR()) + getDTRSuffix());

        if (team != null) {
            if (team.isMember(viewer.getUniqueId())) {
                tag.add(ChatColor.GOLD + "[" + ChatColor.DARK_GREEN + team.getName(viewer) + ChatColor.GRAY + " §7｜§r " + getDTRColor(team) + Team.DTR_FORMAT.format(team.getDTR()) + getDTRSuffix(team) + ChatColor.GOLD + "]");
            } else {
                tag.add(ChatColor.GOLD + "[" + ChatColor.RED + team.getName(viewer) + " §7｜§r " + getDTRColor(Foxtrot.getInstance().getTeamHandler().getTeam(target)) + Team.DTR_FORMAT.format(Foxtrot.getInstance().getTeamHandler().getTeam(target).getDTR()) + getDTRSuffix(Foxtrot.getInstance().getTeamHandler().getTeam(target)) + ChatColor.GOLD + "]");
            }
        }

        if (target.hasMetadata("modmode")) tag.add(ChatColor.GRAY + "[Mod Mode]");
        tag.add(nameTag);
        return tag;
    }


    public static ChatColor getDTRColor(Team team) {
        ChatColor dtrColor = ChatColor.GREEN;

        if (team.getDTR() / team.getMaxDTR() <= 0.25) {
            if (team.isRaidable()) {
                dtrColor = ChatColor.DARK_RED;
            } else {
                dtrColor = ChatColor.YELLOW;
            }
        }

        return (dtrColor);
    }




    public static String getDTRSuffix(Team team) {
        if (DTRHandler.isRegenerating(team)) {
            if (team.getOnlineMemberAmount() == 0) {
                return (ChatColor.GRAY + "◀");
            } else {
                return (ChatColor.GREEN + "▲");
            }
        } else if (DTRHandler.isOnCooldown(team)) {
            return (ChatColor.RED + "■");
        } else {
            return (ChatColor.GREEN + "◀");
        }


    }
}





