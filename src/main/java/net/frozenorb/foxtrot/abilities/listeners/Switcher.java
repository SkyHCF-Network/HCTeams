package net.frozenorb.foxtrot.abilities.listeners;

//import dev.bitvise.starbucks.Foxtrot;
//import dev.bitvise.starbucks.team.Team;
//import dev.bitvise.starbucks.team.dtr.DTRBitmask;
//import dev.bitvise.starbucks.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class Switcher implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack switcher = new ItemStack(Material.SNOW_BALL, player.getItemInHand().getAmount() -1);
        ItemMeta meta = switcher.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(CC.Color("&7Switch locations with any"));
        lore.add(CC.Color("&7enemy within 7 blocks of you"));
        meta.setLore(lore);
        meta.setDisplayName(CC.Color("&b&lSwitcher Ball"));
        switcher.setItemMeta(meta);
        if (!(player.getItemInHand().getType() == Material.SNOW_BALL)) return;
        if (!(event.getItem().hasItemMeta())) return;
        if (!event.getItem().getItemMeta().getLore().contains(CC.Color("&7enemy within 7 blocks of you"))) return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK){
            if (event.getClickedBlock() != null){
                if (Foxtrot.getInstance().getSwitcher().onCooldown(player)){
                    event.getPlayer().sendMessage(CC.Color("&b&lSwitcher Ball&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getSwitcher().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
                    player.updateInventory();
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
            if (Foxtrot.getInstance().getSwitcher().onCooldown(player)){
                event.getPlayer().sendMessage(CC.Color("&b&lSwitcher Ball&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getSwitcher().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
                player.updateInventory();;
                event.setCancelled(true);
                return;
            }

            if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)){
                player.sendMessage(CC.Color("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingInt(event.getPlayer()) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }


            Snowball snowball = player.launchProjectile(Snowball.class);
            snowball.setMetadata("switcher", new FixedMetadataValue(Foxtrot.getPlugin(Foxtrot.class), player.getUniqueId()));
            Foxtrot.getInstance().getSwitcher().applyCooldown(player, 12 * 1000);
            Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);
            if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            player.updateInventory();;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Snowball)) return;
        Snowball snowball = (Snowball) event.getDamager();
        if (!(snowball.getShooter() instanceof Player) && !(event.getEntity() instanceof Player)) return;

        if (!snowball.hasMetadata("switcher")) return;

        Player shooter = (Player) snowball.getShooter();
        Player hit = (Player) event.getEntity();
        Team shooterTeam = Foxtrot.getInstance().getTeamHandler().getTeam(shooter);
        Location loc = shooter.getLocation();
        if (hit.getLocation().distance(shooter.getLocation()) > 8){
            shooter.sendMessage(CC.Color(CC.prefix + " &cThat player is out of range!"));
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(shooter.getLocation())){
            event.setCancelled(true);
            shooter.sendMessage(CC.Color("&cYou cannot use ability items in Spawn's territory!"));
            return;
        }

        if (DTRBitmask.CITADEL.appliesAt(shooter.getLocation())){
            event.setCancelled(true);
            shooter.sendMessage(CC.Color("&cYou cannot use ability items in Citadel's territory!"));
            return;
        }

        // Check if the hit player is in shooter player's team

        if (shooterTeam != null){
            if (shooterTeam.isOwner(hit.getUniqueId()) || shooterTeam.isCoLeader(hit.getUniqueId()) || shooterTeam.isCaptain(hit.getUniqueId()) || shooterTeam.isMember(hit.getUniqueId())){
                shooter.sendMessage(CC.Color( "&cYou cannot switcher players on your team!"));
                event.setCancelled(true);
                return;
            }
        }

        if (DTRBitmask.KOTH.appliesAt(hit.getLocation())){
            event.setCancelled(true);
            shooter.sendMessage(CC.Color(" &cYou cannot switch with people on cap zones!"));
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(hit.getLocation())){
            shooter.sendMessage(CC.Color("&cYou cannot use ability items in Spawn's territory!"));
        }

        shooter.teleport(hit);
        hit.teleport(loc);
        ArrayList<String> shooterMsg = new ArrayList<>();
        shooterMsg.add(CC.Color("&b\u00BB&e You have hit " + hit.getDisplayName() + "&e with a &b&lSwitcher Ball!"));
        shooterMsg.add(CC.Color("&b\u00BB&e You have switched places with them!"));
        ArrayList<String> hitMsg = new ArrayList<>();
        hitMsg.add(CC.Color("&b\u00BB&e You have been hit by &c" + shooter.getDisplayName() + "&e with a &b&lSwitcher Ball!"));
        hitMsg.add(CC.Color("&b\u00BB&e You have switched places with them!"));

        shooterMsg.forEach(str -> shooter.sendMessage(CC.Color(str)));
        hitMsg.forEach(str -> hit.sendMessage(CC.Color(str)));
    }
}