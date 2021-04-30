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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.function.Predicate;

public class PotChecker implements Listener {

    public static Predicate<ItemStack> INSTANT_HEALTH = itemStack -> {
        if (itemStack.getType() != Material.POTION){
            return false;
        }

        PotionType potionType = Potion.fromItemStack(itemStack).getType();
        return potionType == PotionType.INSTANT_HEAL;
    };

    public static int countStacksMatching(ItemStack[] items, Predicate<ItemStack> predicate){
        if (items == null){
            return 0;
        }

        int amountMatching = 0;

        for (ItemStack item : items){
            if (item != null && predicate.test(item)){
                amountMatching++;
            }
        }

        return amountMatching;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player attacked = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (!attacker.getItemInHand().hasItemMeta()) return;
        if (attacker.getItemInHand().getType() != Material.STICK) return;
        if (!attacker.getItemInHand().getItemMeta().getLore().contains(CC.Color("&7check how many potions they have left!"))) return;

        Team attackerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);

        if (DTRBitmask.SAFE_ZONE.appliesAt(attacked.getLocation())){
            event.setCancelled(true);
            attacker.sendMessage(CC.Color(CC.prefix + " &cYou cannot use ability items in Spawn's territory!"));
            return;
        }

        if (attackerTeam != null){
            if (attackerTeam.isOwner(attacked.getUniqueId()) || attackerTeam.isCoLeader(attacked.getUniqueId()) || attackerTeam.isCaptain(attacked.getUniqueId()) || attackerTeam.isMember(attacked.getUniqueId())){
                event.setCancelled(true);
                attacker.updateInventory();
                return;
            }
        }

        if (Foxtrot.getInstance().getPartnerItem().onCooldown(attacker)){
            attacker.sendMessage(CC.Color("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingMilis(attacker) / 1000) + "&c!"));
            attacker.updateInventory();
            return;
        }

        if (Foxtrot.getInstance().getPotChecker().onCooldown(attacker)){
            attacker.sendMessage(CC.Color("&b&lPot Checker&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPotChecker().getRemainingMilis(attacker) / 1000) + "&c!"));
            attacker.updateInventory();
            event.setCancelled(true);
            return;
        }

        Foxtrot.getInstance().getPotChecker().applyCooldown(attacker, 45 * 1000);
        Foxtrot.getInstance().getPartnerItem().applyCooldown(attacker, 10 * 1000);

        int total = countStacksMatching(attacked.getInventory().getContents(), INSTANT_HEALTH);

        ArrayList<String> attackerMsg = new ArrayList<>();
        attackerMsg.add("");
        attackerMsg.add(CC.Color("&b\u2764&e " + attacked.getName() + "&e has &c" + total + "&e potions left!"));
        attackerMsg.add("");
        attackerMsg.forEach(attacker::sendMessage);

        if (attacker.getItemInHand().getAmount() == 1) attacker.setItemInHand(null);

        attacker.getItemInHand().setAmount(attacker.getItemInHand().getAmount() - 1);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if (!p.getItemInHand().hasItemMeta()) return;
        if (p.getItemInHand().getType() != Material.STICK) return;
        if (!p.getItemInHand().getItemMeta().getLore().contains(CC.Color("&7check how many potions they have left!"))) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                if (Foxtrot.getInstance().getPotChecker().onCooldown(p)) {
                    event.getPlayer().sendMessage(CC.Color("&b&lPot Checker&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPotChecker().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
                    p.updateInventory();
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

}
