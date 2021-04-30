/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.InventoryUtils;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ConquestRewardKeyCommand {

    @Command(names={ "conquestrewardkey" }, permission="op")
    public static void conquestRewardKey(Player sender, @Param(name = "player", defaultValue = "self") Player player, @Param(name="amount", defaultValue = "1") int amount , @Param(name="tier", defaultValue = "1") int tier) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Unable to locate player.");
            return;
        }

        if (amount == 0 || 32 < amount) {
            sender.sendMessage(ChatColor.RED + "Illegal amount! Must be between 1 and 32.");
            return;
        }

        if (tier == 0 || tier > 3) {
            sender.sendMessage(ChatColor.RED + "Illegal tier! Must be between 1 and 3.");
            return;
        }

        ItemStack stack = InventoryUtils.generateConquestRewardKey(tier);
        stack.setAmount(amount);
        Map<Integer, ItemStack> failed = player.getInventory().addItem(stack);

        if (amount == 1) {
            String msg = ChatColor.YELLOW + "Gave " + player.getName() + " a Conquest reward key." + failed == null || failed.isEmpty() ? "" : " " + failed.size() + " didn't fit.";
            org.bukkit.command.Command.broadcastCommandMessage(sender, msg);
            sender.sendMessage(msg);
        } else {
            String msg = ChatColor.YELLOW + "Gave " + player.getName() + " " + amount + " Conquest reward keys." + failed == null || failed.isEmpty() ? "" : " " + failed.size() + " didn't fit.";
            org.bukkit.command.Command.broadcastCommandMessage(sender, msg);
            sender.sendMessage(msg);
        }
    }

}
