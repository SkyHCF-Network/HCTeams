package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import net.frozenorb.qlib.command.Command;

public class KitCommand {

    @Command(names = {"kit", "mapkit"}, permission = "")
    public static void kit(Player sender) {
        sender.sendMessage("§eEnchant Limits: §7" + Foxtrot.getInstance().getServerHandler().getEnchants());
    }
}
