/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.team.commands.team;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.yml.LangsFile;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamFocusCommand {

    @Command(names = {"f focus", "t focus", "faction focus", "team focus"}, permission = "")
    public static void focus(Player sender, @Param(name = "player") Team targetTeam){

        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
        CBWaypoint cbWaypoint = null;

        if (senderTeam == null){
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (senderTeam == targetTeam){
            sender.sendMessage(ChatColor.RED + "You cannot focus a player on your team!");
            return;
        }

        if (senderTeam.getFactionFocused() == targetTeam) {
            senderTeam.setFactionFocus(null);
            senderTeam.sendMessage(ChatColor.YELLOW + "Team's focus has been disabled.");
            CBWaypoint finalCbWaypoint = cbWaypoint;
            senderTeam.getOnlineMembers().forEach(player -> CheatBreakerAPI.getInstance().removeWaypoint(player, finalCbWaypoint));
            CheatBreakerAPI.getInstance().removeWaypoint(sender, finalCbWaypoint);
        } else {
            if(targetTeam.getHQ() == null) {
                sender.sendMessage(ChatColor.RED + "You cannot focus a team that does not have a HQ!");
                return;
            }
            senderTeam.setFactionFocus(targetTeam);
            cbWaypoint = new CBWaypoint(targetTeam.getName() + "'s HQ", targetTeam.getHQ().getBlockX(), targetTeam.getHQ().getBlockY(), targetTeam.getHQ().getBlockZ(), targetTeam.getHQ().getWorld().getUID().toString(), -16776961, true, true);
            CBWaypoint finalCbWaypoint1 = cbWaypoint;
            senderTeam.getOnlineMembers().forEach(player -> CheatBreakerAPI.getInstance().removeWaypoint(player, finalCbWaypoint1));
            CheatBreakerAPI.getInstance().removeWaypoint(sender, finalCbWaypoint1);
            senderTeam.getOnlineMembers().forEach(player -> CheatBreakerAPI.getInstance().sendWaypoint(player, finalCbWaypoint1));
            CheatBreakerAPI.getInstance().sendWaypoint(sender, finalCbWaypoint1);
            senderTeam.sendMessage(CC.Color(LangsFile.getConfig().getString("langs.messages.type.team.focus").replace("%enemy%", targetTeam.getName()).replace("%player%", sender.getName())));
        }
    }
}