/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.events.undead.entities;

import net.frozenorb.foxtrot.events.undead.UndeadEntity;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

public class ZombieEntity implements UndeadEntity {

    @Override
    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', "&a&lUndead Zombie");
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ZOMBIE;
    }

    @Override
    public Double getMaxHealth() {
        return 85.0;
    }

}
