/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeicalEffectCommand {

    @Command(names = {"speed"}, permission = "core.donator")
    public static void speed(Player player) {
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.sendMessage(ChatColor.RED + "Infinite speed has been disabled");
        } else {
            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true);
            player.addPotionEffect(speed);
            player.sendMessage(ChatColor.GREEN + "Infinite speed has been enabled");
        }
    }

    @Command(names = {"fire", "fireres"}, permission = "core.donator")
    public static void fire(Player player) {
        if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            player.sendMessage(ChatColor.RED + "Infinite Fire has been disabled");
        } else {
            PotionEffect speed = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, true);
            player.addPotionEffect(speed);
            player.sendMessage(ChatColor.GREEN + "Infinite Fire has been enabled");
        }
    }

}
