/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class UndeadCommand {

    @Command(names = {"undead start", "undeadevent start"}, permission = "foxtrot.undeadevent")
    public static void undeadStart(CommandSender sender){
        if(Foxtrot.getInstance().isUndead()){
            sender.sendMessage(ChatColor.RED + "The Undead Event has already been started.");
            return;
        }
        Foxtrot.getInstance().setUndead(true);
        for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1F, 1F);
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l████████&r"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l████████&r"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l█&8&l██&r&f&l██&8&l██&r&f&l█&r &c[Undead Event]"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l███&7&l██&r&l███&r &cThe &4Undead Event &chas commenced!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l█&7&l██████&r&l█&r &cUndead monsters will spawn in claims"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l████████&r &crandomly around the map."));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f&l████████&r"));
    }

    @Command(names = {"undead stop", "undeadevent stop"}, permission = "foxtrot.undeadevent")
    public static void undeadStop(CommandSender sender){
        if(!Foxtrot.getInstance().isUndead()){
            sender.sendMessage(ChatColor.RED + "The Undead Event is not active.");
            return;
        }
        Foxtrot.getInstance().setUndead(false);
        killUndeadEntities();
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a&lThe Undead Event has ended and all monsters have been cleared."));
    }

    @Command(names = {"undead removeentities", "undeadevent removeentities"}, permission = "foxtrot.undeadevent")
    public static void undeadRemoveEntities(CommandSender sender){
        killUndeadEntities();
        sender.sendMessage(ChatColor.GREEN + "Despawned all undead entities.");
    }

    private static void killUndeadEntities(){
        for(Entity entity : Bukkit.getWorld("world").getLivingEntities()){
            if(entity instanceof Monster){
                Monster monster = ((Monster) entity);
                if(monster.getCustomName() != null && monster.getCustomName().contains("Undead ")){
                    monster.remove();
                }
            }
        }
    }

}
