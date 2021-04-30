/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.events.undead.entities;

import net.frozenorb.foxtrot.events.undead.UndeadEntity;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

public class SkeletonEntity implements UndeadEntity {

    @Override
    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', "&c&lUndead Skeleton");
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SKELETON;
    }

    @Override
    public Double getMaxHealth() {
        return 100.0;
    }
}
