package net.frozenorb.foxtrot.abilities.listeners;

//import dev.bitvise.starbucks.Foxtrot;
//import dev.bitvise.starbucks.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NinjaStar implements Listener {

    public static Map<String, Long> teleport = new HashMap<>();
    public Location loc;
    public Player target;

    @EventHandler
    public void onattack(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            teleport.put(victim.getName(), System.currentTimeMillis() + 1000L);
            this.loc = damager.getLocation();
            this.target = damager;
        }
    }

    @EventHandler
    public void rightclick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!(player.getItemInHand().getType() == Material.NETHER_STAR)) return;
        if (!(event.getItem().hasItemMeta())) return;
        if (!event.getItem().getItemMeta().getLore().contains(CC.Color("&7teleport to the player that last hit you!")))
            return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)) {
                player.sendMessage(CC.Color("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (Foxtrot.getInstance().getNinjaStar().onCooldown(player)) {
                player.sendMessage(CC.Color("&b&lNinja Star&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getNinjaStar().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (player == target || target == null) {
                player.sendMessage(ChatColor.RED + "Failed to use the Ninja Star!");
                return;
            }

            ArrayList<String> usedMessage = new ArrayList<>();
            usedMessage.add("");
            usedMessage.add(CC.Color("&b\u00BB &eYou have used your Ninja Star on " + this.target.getName() + "!"));
            usedMessage.add(CC.Color("&b\u00BB &eYou will be teleported in 5 seconds!"));
            usedMessage.add("");
            usedMessage.forEach(player::sendMessage);

            ArrayList<String> enemyMessage = new ArrayList<>();
            enemyMessage.add("");
            enemyMessage.add(CC.Color("&b\u00BB &e" + player.getName() + " has used their Ninja Star on you!"));
            enemyMessage.add(CC.Color("&b\u00BB &eThey will be teleported in 5 seconds!"));
            enemyMessage.add("");
            enemyMessage.forEach(target::sendMessage);

            new BukkitRunnable(){

                @Override
                public void run() {
                    player.teleport(target.getLocation());
                    player.sendMessage(ChatColor.GREEN + "You have been teleported!");
                }
            }.runTaskLaterAsynchronously(Foxtrot.getInstance(), (5 * 10));

            if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() -1);

            Foxtrot.getInstance().getNinjaStar().applyCooldown(player, 120 * 1000);
            Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);
        }
    }
}
