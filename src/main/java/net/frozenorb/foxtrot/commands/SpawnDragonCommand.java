package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.frozenorb.qlib.command.Command;

public class SpawnDragonCommand {
    @Command(names={ "spawndragon", "spawnenderdragon" }, permission="op")
    public static void spawnDragon(Player sender) {

        if (sender.getWorld().getEntitiesByClass(EnderDragon.class).size() != 0) {
            sender.sendMessage(ChatColor.RED + "There's already an enderdragon!");
            return;
        }

        if (sender.getWorld().getEnvironment() == World.Environment.THE_END) {
            sender.getWorld().spawnEntity(sender.getLocation(), EntityType.ENDER_DRAGON);

            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.LIGHT_PURPLE + "█" + ChatColor.BLACK + "██" + ChatColor.LIGHT_PURPLE + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.LIGHT_PURPLE + "█" + ChatColor.RED + " The EnderDragon has awoken!");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.BLACK + "████████");
        } else {
            sender.sendMessage(ChatColor.RED + "You must be in the end to spawn an enderdragon.");
        }
    }
}

