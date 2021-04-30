/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.listener;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UndeadListener implements Listener {

    @EventHandler
    public void die(EntityDeathEvent e){
        if(e.getEntity() instanceof Monster){
            Monster monster = ((Monster) e.getEntity());
            if(monster.getCustomName() != null && monster.getCustomName().contains("Undead ")){
                e.getDrops().clear();
                e.getDrops().addAll(getRandomDrops());
            }
        }
    }

    private List<ItemStack> getRandomDrops(){
        List<ItemStack> items = Lists.newArrayList();
        List<ItemStack> finalItems = Lists.newArrayList();
        items.add(ItemBuilder.of(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 2).enchant(Enchantment.DURABILITY, 3).setLore(Arrays.asList("&7Obtained from the &cUndead Event&7.")).build());
        items.add(ItemBuilder.of(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.PROTECTION_FALL, 3).setLore(Arrays.asList("&7Obtained from the &cUndead Event&7.")).build());
        items.add(ItemBuilder.of(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).setLore(Arrays.asList("&7Obtained from the &cUndead Event&7.")).build());
        items.add(ItemBuilder.of(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).setLore(Arrays.asList("&7Obtained from the &cUndead Event&7.")).build());
        items.add(ItemBuilder.of(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.DURABILITY, 3).setLore(Arrays.asList("&7Obtained from the &cUndead Event&7.")).build());
        items.add(ItemBuilder.of(Material.MAGMA_CREAM, 3).build());
        items.add(ItemBuilder.of(Material.ENDER_PEARL, 2).build());
        items.add(ItemBuilder.of(Material.SPIDER_EYE, 3).build());
        items.add(ItemBuilder.of(Material.GOLDEN_APPLE).build());
        Collections.shuffle(items);
        finalItems.add(items.get(0));
        finalItems.add(items.get(1));
        return finalItems;
    }


}
