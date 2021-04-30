/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.util.MessageUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinMessageListener implements Listener {

    @EventHandler
    public static void sendJoinMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(MessageUtility.format("&7&m--------------------------------------"));
        player.sendMessage(MessageUtility.format("&r"));
        player.sendMessage(MessageUtility.format("&bYou have connected to &b&lKitMap"));
        player.sendMessage(MessageUtility.format("&7&oThis map started on the 9th of May!"));
        player.sendMessage(MessageUtility.format("&r"));
        player.sendMessage(MessageUtility.format("&7&m--------------------------------------"));
    }
}