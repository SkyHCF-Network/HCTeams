/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ColorCommand {
    private List<Material> list = Arrays.asList(Material.GLASS, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.THIN_GLASS, Material.HARD_CLAY, Material.STAINED_CLAY, Material.WOOL, Material.CARPET);

    @Command(names = {"color"}, permission = "core.color")
    public void execute(final CommandSender s, @Param(name = "Color") String arg) {
        final Player p = (Player) s;
        if (!Foxtrot.getInstance().getMapHandler().isKitMap()) {
            s.sendMessage(ChatColor.RED + "This is a Kitmap command only!");
            return;
        }
        if (p.getItemInHand() == null || p.getItemInHand().getType().equals((Object) Material.AIR)) {
            p.sendMessage("§cYou are not holding any item.");
            return;
        }
        if (arg.equalsIgnoreCase("clear")) {
            if (p.getItemInHand().getType() == Material.STAINED_CLAY) {
                p.getItemInHand().setType(Material.HARD_CLAY);
            } else if (p.getItemInHand().getType() == Material.STAINED_GLASS) {
                p.getItemInHand().setType(Material.GLASS);
            } else if (p.getItemInHand().getType() == Material.STAINED_GLASS_PANE) {
                p.getItemInHand().setType(Material.THIN_GLASS);
            }
            p.getItemInHand().setDurability((short) 0);
            p.sendMessage("§eThe color of your §f" + p.getItemInHand().getType() + " §ehas been reset.");
            return;
        }
        if (this.getColor(arg) == null) {
            final StringBuilder sb = new StringBuilder();
            DyeColor[] values;
            for (int length = (values = DyeColor.values()).length, i = 0; i < length; ++i) {
                final DyeColor d = values[i];
                sb.append(String.valueOf(d) + "§7, §f");
            }
            p.sendMessage("§cColor not found! Please choose from these colors§7: " + sb.toString().trim() + ", §fClear");
            return;
        }
        if (this.list.contains(p.getItemInHand().getType())) {
            if (p.getItemInHand().getType() == Material.HARD_CLAY) {
                p.getItemInHand().setType(Material.STAINED_CLAY);
            } else if (p.getItemInHand().getType() == Material.GLASS) {
                p.getItemInHand().setType(Material.STAINED_GLASS);
            } else if (p.getItemInHand().getType() == Material.THIN_GLASS) {
                p.getItemInHand().setType(Material.STAINED_GLASS_PANE);
            }
            p.getItemInHand().setDurability((short) this.getColor(arg).getData());
            p.sendMessage("§eThe color of your §f" + p.getItemInHand().getType().name() + " §ehas been changed to " + this.getColor(arg) + "§e.");
            return;
        }
        p.sendMessage("§cThe color of this item cannot be changed.");
    }

    public DyeColor getColor(final String s) {
        DyeColor c;
        try {
            c = DyeColor.valueOf(s.toUpperCase());
        } catch (Exception e) {
            c = null;
        }
        return c;
    }
}
