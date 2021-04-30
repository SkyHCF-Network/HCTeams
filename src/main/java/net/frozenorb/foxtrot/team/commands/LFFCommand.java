/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.team.commands;

import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.entity.Player;

public class LFFCommand {
    @Command(names = {"lff", "lookingforfaction"}, permission = "")
    public static void lff(final Player player) {
        player.sendMessage(BukkitChat.format("&cThis command is temporarily disabled."));
    }
}
