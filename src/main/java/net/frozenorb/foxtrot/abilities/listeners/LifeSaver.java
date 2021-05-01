package net.frozenorb.foxtrot.abilities.listeners;

//import dev.bitvise.starbucks.StarbucksPlugin;
//import dev.bitvise.starbucks.team.dtr.DTRBitmask;
//import dev.bitvise.starbucks.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class LifeSaver implements Listener {

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if (!p.getItemInHand().hasItemMeta()) return;
        if (p.getItemInHand().getType() != Material.WATCH) return;
        if (event.getAction().equals(Action.PHYSICAL)) return;
        if (!p.getItemInHand().getItemMeta().getLore().contains(CC.Color("&7during this cooldown if you go down to 2 hearts you will be healed fully!"))) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                if (Foxtrot.getInstance().getLifeSaver().onCooldown(p)) {
                    event.getPlayer().sendMessage(CC.Color("&4&lLife Saver&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getLifeSaver().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
                    p.updateInventory();
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!player.getItemInHand().hasItemMeta()) return;
        if (player.getItemInHand().getType() != Material.WATCH) return;
        if (event.getAction().equals(Action.PHYSICAL)) return;
        if (!player.getItemInHand().getItemMeta().getLore().contains(CC.Color("&7during this cooldown if you go down to 2 hearts you will be healed fully!"))) return;

        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())){
            player.sendMessage(CC.Color(CC.prefix + " &fYou cannot use &4ability&f items in &aSpawn&f's territory!"));
            return;
        }

        if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)){
            player.sendMessage(CC.Color("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingInt(event.getPlayer()) / 1000) + "&c!"));
            player.updateInventory();
            event.setCancelled(true);
            return;
        }

        if (Foxtrot.getInstance().getLifeSaver().onCooldown(player)){
            event.getPlayer().sendMessage(CC.Color("&4&lLife Saver&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getLifeSaver().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
            player.updateInventory();;
            event.setCancelled(true);
            return;
        }

        Foxtrot.getInstance().getLifeSaver().applyCooldown(player, 80 * 1000);
        Foxtrot.getInstance().getLifeSaverSafe().applyCooldown(player, 30 * 1000);
        Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);

        ArrayList<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.Color("&b\u2764 &cYou have used your Life Saver"));
        message.add(CC.Color("&b\u2764 &cYour Life Saver is active for 30 seconds!"));
        message.add("");
        message.forEach(player::sendMessage);

        if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);

        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Player player = (Player) event.getEntity();
        if (Foxtrot.getInstance().getLifeSaverSafe().onCooldown(player) && (player.getHealth() < 3.0) || player.getHealth() == 3.0){
            player.setHealth(player.getMaxHealth());
            Foxtrot.getInstance().getLifeSaverSafe().cooldownRemove(player);
            ArrayList<String> message = new ArrayList<>();
            message.add("");
            message.add(CC.Color("&b\u2764 &eYour Life Save has been used!"));
            message.add(CC.Color("&b\u2764 &eYou have been healed fully!"));
            message.add("");
            message.forEach(player::sendMessage);
            return;
        }
    }
}
