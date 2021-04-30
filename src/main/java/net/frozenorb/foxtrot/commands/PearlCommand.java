/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PearlCommand {

    @Command(names = {"pearl create"}, permission = "foxtrot.admin")
    public static void pearlCreate(CommandSender player, @Param(name = "target", defaultValue = "self") Player target){
        EnderpearlCooldownHandler.getEnderpearlCooldown().put(player.getName(), 16_000L);
        player.sendMessage(BukkitChat.format("&aPearl cooldown created."));
    }

    @Command(names = "pearl remove", permission = "foxtrot.admin")
    public static void pearlRemove(CommandSender player, @Param(name = "target", defaultValue = "self") Player target){
        EnderpearlCooldownHandler.getEnderpearlCooldown().remove(target.getName());
        player.sendMessage(BukkitChat.format("&aPearl cooldown removed."));
    }

}
