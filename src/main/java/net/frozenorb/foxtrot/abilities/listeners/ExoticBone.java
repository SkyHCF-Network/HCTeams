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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ExoticBone implements Listener {

    public static HashMap<UUID, Player> hitOnce = new HashMap<>();
    public static HashMap<UUID, Player> hitTwice = new HashMap<>();
    public static HashMap<UUID, Player> hitThrice = new HashMap<>();

    @EventHandler
    public void on(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Player attacked = (Player) event.getEntity();
        Team attackerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
        ItemStack buildStick = new ItemStack(Material.BONE, attacker.getItemInHand().getAmount() - 1);
        ItemMeta meta = buildStick.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(CC.Color("&7Hit a player 3 times in a row with this item"));
        lore.add(CC.Color("&7to restrict them from building for 15 seconds!"));
        meta.setLore(lore);
        meta.setDisplayName(CC.Color("&b&lExotic Bone"));
        buildStick.setItemMeta(meta);
        if (!attacker.getItemInHand().hasItemMeta()) return;
        if (attacker.getItemInHand().getType() != Material.BONE) return;
        if (!attacker.getItemInHand().getItemMeta().getLore().contains(CC.Color("&7to restrict them from building for 15 seconds!"))) return;

        if (Foxtrot.getInstance().getPartnerItem().onCooldown(attacker)){
            attacker.sendMessage(CC.Color("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingMilis(attacker) / 1000) + "&c!"));
            attacker.updateInventory();
            return;
        }

        if (Foxtrot.getInstance().getRestricted().onCooldown(attacked)){
            attacker.sendMessage(CC.Color("&cThat player is already anti-d!"));
            event.setCancelled(true);
            return;
        }

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

        if (Foxtrot.getInstance().getAntibuildstick().onCooldown(attacker)){
            attacker.sendMessage(CC.Color("&b&lExotic Bone&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getAntibuildstick().getRemainingMilis(attacker) / 1000) + "&c!"));
            attacker.updateInventory();
            event.setCancelled(true);
            return;
        }
        if (hitThrice.containsKey(attacked.getUniqueId())){
            hitOnce.remove(attacked.getUniqueId());
            hitTwice.remove(attacked.getUniqueId());
            hitThrice.remove(attacked.getUniqueId());
            hitOnce.put(attacked.getUniqueId(), attacker);
            return;
        }
        if (!hitOnce.containsKey(attacked.getUniqueId())){
            hitOnce.put(attacked.getUniqueId(), attacker);
            return;
        }
        if (!hitTwice.containsKey(attacked.getUniqueId())){
            if (hitOnce.containsKey(attacked.getUniqueId())){
                hitTwice.put(attacked.getUniqueId(), attacker);
                return;
            } else {
                return;
            }
        }

            if (hitOnce.containsKey(attacked.getUniqueId())){
                if (hitTwice.containsKey(attacked.getUniqueId())){
                    hitThrice.put(attacked.getUniqueId(), attacker);
                    Foxtrot.getInstance().getAntibuildstick().applyCooldown(attacker, 80 * 1000);
                    Foxtrot.getInstance().getRestricted().applyCooldown(attacked, 15 * 1000);

                    ArrayList<String> attackerMsg = new ArrayList<>();
                    attackerMsg.add(CC.Color("&b\u00BB &eYou have hit " + attacked.getDisplayName() + "&e with a &b&lExotic Bone&e!"));
                    attackerMsg.add(CC.Color("&b\u00BB &eYou have restricted their building for &c&l15&c seconds!"));
                    ArrayList<String> attackedMsg = new ArrayList<>();
                    attackedMsg.add(CC.Color("&b\u00BB &eYou have been hit with a &b&lExotic Bone&e!"));
                    attackedMsg.add(CC.Color("&b\u00BB &eYour building has been restricted for &c&l15&c seconds!"));

                    attackerMsg.forEach(str -> attacker.sendMessage(CC.Color(str)));
                    attackedMsg.forEach(str -> attacked.sendMessage(CC.Color(str)));

                    attacker.setItemInHand(buildStick);
                    return;
                }
            }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if (!p.getItemInHand().hasItemMeta()) return;
        if (p.getItemInHand().getType() != Material.STICK) return;
        if (!p.getItemInHand().getItemMeta().getLore().contains(CC.Color("&7Hit a player 3 times in a row with this item"))) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                if (Foxtrot.getInstance().getAntibuildstick().onCooldown(p)) {
                    event.getPlayer().sendMessage(CC.Color("&b&lExotic Bone&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getAntibuildstick().getRemainingMilis(event.getPlayer()) / 1000) + "&c!"));
                    p.updateInventory();
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInteract2(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null) {
            if (Foxtrot.getInstance().getRestricted().onCooldown(player)) {
                event.getPlayer().sendMessage(CC.Color("&cYou are still restricted for " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getRestricted().getRemainingMilis(player) / 1000)));
                player.updateInventory();;
                return;
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if (Foxtrot.getInstance().getRestricted().onCooldown(player)){
            event.getPlayer().sendMessage(CC.Color("&cYou are still restricted for " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getRestricted().getRemainingMilis(player) / 1000)));
            player.updateInventory();;
            event.getBlock().setType(Material.AIR);
            return;
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (Foxtrot.getInstance().getRestricted().onCooldown(player)){
            event.getPlayer().sendMessage(CC.Color("&cYou are still restricted for " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getRestricted().getRemainingMilis(player) / 1000)));
            player.updateInventory();
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void fencegatechest(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (event.getClickedBlock() != null){
                if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.FENCE_GATE || event.getClickedBlock().getType() == Material.TRAPPED_CHEST || event.getClickedBlock().getType() == Material.TRAP_DOOR || event.getClickedBlock().getType() == Material.WORKBENCH || event.getClickedBlock().getType() == Material.BREWING_STAND){
                    if (Foxtrot.getInstance().getRestricted().onCooldown(event.getPlayer())){
                        event.getPlayer().sendMessage(CC.Color("&cYou are still restricted for " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getRestricted().getRemainingMilis(event.getPlayer()) / 1000)));
                        event.getPlayer().updateInventory();;
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
