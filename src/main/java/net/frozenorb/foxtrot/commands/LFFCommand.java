/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.Cooldowns;
import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LFFCommand {

    @Command(names = {"lff", "lookingforfaction"}, permission = "")
    public static void lff(Player player){
        if(Cooldowns.isOnCooldown("lff", player)){
            player.sendMessage(BukkitChat.format("&cYou cannot use this command for another &l" + TimeUtil.formatDuration(Cooldowns.getCooldownForPlayerLong("lff", player)) + "&r&c."));
            return;
        }
        Cooldowns.addCooldown("lff", player, (60 * 3));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(BukkitChat.format("&7* &r" + player.getDisplayName() + "&r &fis looking for a &bfaction&f."));
        Bukkit.broadcastMessage("");
    }

}
