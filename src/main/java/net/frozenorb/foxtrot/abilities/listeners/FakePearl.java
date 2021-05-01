package net.frozenorb.foxtrot.abilities.listeners;

//import dev.bitvise.starbucks.Foxtrot;
//import dev.bitvise.starbucks.server.EnderpearlCooldownHandler;
//import dev.bitvise.starbucks.util.CC;
//import net.frozenorb.Foxtrot.Foxtrot;
//import net.frozenorb.Foxtrot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class FakePearl implements Listener {

    @EventHandler
    public void rightclick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, player.getItemInHand().getAmount() - 1);
        ItemMeta meta = pearl.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(CC.Color("&7Right click this pearl"));
        lore.add(CC.Color("&7to shoot a pearl to trick your enemies!"));
        meta.setLore(lore);
        meta.setDisplayName(CC.Color("&4&lFake Pearl"));
        pearl.setItemMeta(meta);
        if (!(player.getItemInHand().getType() == Material.ENDER_PEARL)) return;
        if (!(event.getItem().hasItemMeta())) return;
        if (!(event.getItem().getItemMeta().hasLore())) return;
        if (!event.getItem().getItemMeta().getLore().contains(CC.Color("&7to shoot a pearl to trick your enemies!")))
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)) {
                player.sendMessage(CC.Color("&d&lPartner Item&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getPartnerItem().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (Foxtrot.getInstance().getFakePearl().onCooldown(player)) {
                player.sendMessage(CC.Color("&b&lFake Pearl&c is on cooldown for another&c&l " + TimeUtils.formatIntoDetailedString((int) Foxtrot.getInstance().getFakePearl().getRemainingMilis(player) / 1000) + "&c!"));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            ArrayList<String> message = new ArrayList<>();
            message.add(CC.Color("&b\u00BB &eYou have used your fake pearl!"));
            message.add(CC.Color("&b\u00BB &eThis pearl will not land!"));
            message.forEach(s -> player.sendMessage(s));
            EnderPearl enderPearl = player.launchProjectile(EnderPearl.class);
            enderPearl.setMetadata("fakepearl", new FixedMetadataValue(Foxtrot.getPlugin(Foxtrot.class), player.getUniqueId()));
            Foxtrot.getInstance().getFakePearl().applyCooldown(player, 30 * 1000);
            Foxtrot.getInstance().getPartnerItem().applyCooldown(player, 10 * 1000);
            if (player.getItemInHand().getAmount() == 1) player.setItemInHand(null);
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            player.updateInventory();
            event.setCancelled(true);
            if (EnderpearlCooldownHandler.getEnderpearlCooldown().containsKey(player.getName())) {
                EnderpearlCooldownHandler.getEnderpearlCooldown().remove(player.getName());
            }
        }
    }
}
