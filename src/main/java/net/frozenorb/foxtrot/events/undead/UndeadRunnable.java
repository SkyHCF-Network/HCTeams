/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.events.undead;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.undead.entities.SkeletonEntity;
import net.frozenorb.foxtrot.events.undead.entities.ZombieEntity;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.Claim;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class UndeadRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if(Foxtrot.getInstance().isUndead()){
            for(Team team : Foxtrot.getInstance().getTeamHandler().getTeams()){
                if(!team.hasDTRBitmask(DTRBitmask.SAFE_ZONE) || !team.hasDTRBitmask(DTRBitmask.KOTH) || !team.hasDTRBitmask(DTRBitmask.CITADEL) || !team.hasDTRBitmask(DTRBitmask.CONQUEST)){
                    for(Claim claim : team.getClaims()){
                        Location spawnLocation = LocationUtil.getRandomLocation(new Location(Bukkit.getWorld("world"), claim.getX1(), 0, claim.getZ1()), new Location(Bukkit.getWorld("world"), claim.getX2(), 0, claim.getZ2()));
                        spawnLocation.setY(Bukkit.getWorld("world").getHighestBlockYAt(spawnLocation.clone()));
                        UndeadEntity undeadEntity = new ZombieEntity();
                        if(new Random().nextBoolean()) undeadEntity = new SkeletonEntity();
                        Entity entity = Bukkit.getWorld("world").spawnEntity(spawnLocation, undeadEntity.getEntityType());
                        ((Monster) entity).setCustomNameVisible(true);
                        ((Monster) entity).setCustomName(ChatColor.translateAlternateColorCodes('&', undeadEntity.getName()));
                        ((Monster) entity).setMaxHealth(undeadEntity.getMaxHealth());
                        ((Monster) entity).setHealth(undeadEntity.getMaxHealth());
                        ((Monster)entity).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, true));
                        ((Monster) entity).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5, true));
                    }
                }
            }
        }
    }
}
