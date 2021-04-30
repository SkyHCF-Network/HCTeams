/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.events.undead;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;

public class AntiBurnRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if(!Foxtrot.getInstance().isUndead()) return;
        for(Entity entity : Bukkit.getWorld("world").getEntities()){
            if(entity instanceof Monster){
                Monster monster = ((Monster) entity);
                if(monster.getCustomName() != null && monster.getCustomName().contains("Undead ")){
                    monster.setFireTicks(0);
                }
            }
        }
    }
}
