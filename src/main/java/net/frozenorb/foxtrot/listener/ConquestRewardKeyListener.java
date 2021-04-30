/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.InventoryUtils;
import net.frozenorb.qlib.qLib;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ConquestRewardKeyListener implements Listener {

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getItem() == null || event.getClickedBlock().getType() != Material.ENDER_CHEST || !DTRBitmask.SAFE_ZONE.appliesAt(event.getClickedBlock().getLocation()) || !InventoryUtils.isSimilar(event.getItem(), ChatColor.RED + "Conquest Reward Key")) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST && DTRBitmask.SAFE_ZONE.appliesAt(event.getClickedBlock().getLocation())) {
                openConquestLoot(event.getPlayer(), event.getClickedBlock());
            }

            return;
        }

        event.setCancelled(true);

        int open = 0;

        for (ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                open++;
            }
        }

        if (open < 3) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must have at least 5 open inventory slots to use a Conquest reward key!");
            return;
        }

        Block block = event.getClickedBlock().getRelative(BlockFace.DOWN, 3);

        if (block.getType() != Material.TRAPPED_CHEST) {
            return;
        }

        ItemStack stack = event.getPlayer().getItemInHand();
        if (stack.getAmount() == 1) {
            event.getPlayer().setItemInHand(null);
        } else {
            stack.setAmount(stack.getAmount() - 1);
            event.getPlayer().setItemInHand(stack);
        }
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.FIREWORK_BLAST, 1F, 1F);

        Chest chest = (Chest) block.getState();
        ItemStack[] lootTables = chest.getBlockInventory().getContents();
        final List<ItemStack> loot = new ArrayList<>();
        int given = 0;
        int tries = 0;
        int tier = NumberUtils.toInt(stack.getItemMeta().getLore().get(2).replaceAll("[^\\d.]", ""));

        toploop:
        while (given < 3 && tries < 100) {
            tries++;

            ItemStack chosenItem = lootTables[((tier-1)*9) + qLib.RANDOM.nextInt(9)];

            if (chosenItem == null || chosenItem.getType() == Material.AIR || chosenItem.getAmount() == 0) {
                continue;
            }

            for (ItemStack givenLoot : loot) {
                if (givenLoot.getType() == chosenItem.getType()) {
                    continue toploop; // 'continue's while loop, not for loop.
                }
            }

            given++;
            loot.add(chosenItem);
        }

        final StringBuilder builder = new StringBuilder();

        for (ItemStack itemStack : loot) {
            String displayName = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? ChatColor.RED.toString() + ChatColor.ITALIC + ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()) : ChatColor.BLUE.toString() + itemStack.getAmount() + "x " + ChatColor.YELLOW + WordUtils.capitalize(itemStack.getType().name().replace("_", " ").toLowerCase());

            builder.append(ChatColor.YELLOW).append(displayName).append(ChatColor.GOLD).append(", ");
        }

        if (builder.length() > 2) {
            builder.setLength(builder.length() - 2);
        }

        Bukkit.broadcastMessage(ChatColor.YELLOW + "[Conquest] " + ChatColor.GOLD + event.getPlayer().getName() + " is obtaining loot from a " + ChatColor.YELLOW + "Conquest Key" + ChatColor.GOLD + ".");

        new BukkitRunnable() {

            public void run() {
                new BukkitRunnable() {

                    public void run() {
                        Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.YELLOW + "[Conquest] " + ChatColor.GOLD + event.getPlayer().getName() + " obtained " + ChatColor.YELLOW + builder.toString() + ChatColor.GOLD + "," + ChatColor.GOLD + " from a " + ChatColor.BLUE.toString() + ChatColor.ITALIC + "Conquest Key" + ChatColor.GOLD + ".");
                    }

                }.runTaskAsynchronously(Foxtrot.getInstance());

                for (ItemStack lootItem : loot) {
                    event.getPlayer().getInventory().addItem(lootItem);
                }

                event.getPlayer().updateInventory();
            }

        }.runTaskLater(Foxtrot.getInstance(), 20 * 5L);
    }

    private void openConquestLoot(Player player, Block clickedBlock) {
        Block block = clickedBlock.getRelative(BlockFace.DOWN, 3);

        if (block.getType() != Material.TRAPPED_CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        ItemStack[] lootTables = chest.getBlockInventory().getContents();

        Inventory inventory = Bukkit.createInventory(player, 27, "Conquest Loot");
        inventory.setContents(lootTables);

        player.openInventory(inventory);
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (player.getOpenInventory() != null && player.getOpenInventory().getTitle() != null && player.getOpenInventory().getTitle().equals("Conquest Loot")) {
                event.setCancelled(true);
            }
        }
    }
    
}
