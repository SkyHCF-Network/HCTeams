package net.frozenorb.foxtrot.map.kits.command;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.kits.Kit;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.scoreboard.ScoreFunction;

public class KitCommand {
    
    private static Map<String, Map<UUID, Long>> cooldownsMap = Maps.newHashMap();
    
    @Command(names = { "kits", "kit" }, permission = "")
    public static void kits_command(Player sender, @Param(name = "<kit>", defaultValue = "some-unused-value", wildcard = true) String kitName) {
        kitName = kitName.toLowerCase();
        if (kitName.equalsIgnoreCase("some-unused-value")) {
            StringBuilder kitsBuilder = new StringBuilder();
            for (Kit kit : Foxtrot.getInstance().getMapHandler().getKitManager().getKits()) {
                if (!canUse(sender, kit)) continue;

                if (kitsBuilder.length() != 0) {
                    kitsBuilder.append(',');
                    kitsBuilder.append(' ');
                }
                
                kitsBuilder.append(kit.getName());
            }

            if (kitsBuilder.length() == 0) {
                sender.sendMessage(ChatColor.RED + "You have no usable donor kits!");
                return;
            }

            sender.sendMessage(ChatColor.RED + "Your usable donor kits: " + kitsBuilder.toString());
            return;
        }

        Kit targetKit = Foxtrot.getInstance().getMapHandler().getKitManager().get(kitName);

        if (targetKit == null) {
            sender.sendMessage(ChatColor.RED + "Unable to locate kit '" + kitName + "'.");
            return;
        }

        if (SpawnTagHandler.isTagged(sender)) {
            sender.sendMessage(ChatColor.RED + "You're ineligible to use donator kits whilst you have an active Spawn Tag.");
            return;
        }

        if (sender.hasMetadata("modmode")) {
            sender.sendMessage(ChatColor.RED + "You can't do this whilst in mod mode.");
            return;
        }

        if (!canUse(sender, targetKit)) {
            sender.sendMessage(ChatColor.RED + "You're ineligible to use the donator kit '" + kitName + "'.");
            return;
        }

        Map<UUID, Long> cooldownMap = cooldownsMap.get(kitName);
        if (cooldownMap == null) {
            cooldownsMap.put(kitName, cooldownMap = Maps.newHashMap());
        }

        if (0 <= cooldownMap.getOrDefault(sender.getUniqueId(), -1L) - System.currentTimeMillis()) {
            long difference = cooldownMap.get(sender.getUniqueId()) - System.currentTimeMillis();
            sender.sendMessage(ChatColor.RED + "You're ineligible to use this kit for " + ScoreFunction.TIME_SIMPLE.apply(difference / 1000F));
            return;
        } else {
            String lowerCaseName = kitName.toLowerCase();
            cooldownMap.put(sender.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis((lowerCaseName.contains("velt") && (lowerCaseName.endsWith("+") || lowerCaseName.endsWith("plus"))) ? 15 : 30));
        }

        targetKit.apply(sender);
    }

    public static boolean canUse(Player player, Kit kit) {
        String kitName = kit.getName();

        // Don't show any of these kits as donator kits
        if (kitName.equals("Ranger") || kitName.equals("PvP") || kitName.equals("Diamond") || kitName.equals("Archer") || kitName.equals("Bard") || kitName.equals("Rogue") || kitName.equals("Miner") || kitName.equals("Builder")) {
            return false;
        }

        // Don't show any of these kitmap specific kits as donator kits either
        if (Foxtrot.getInstance().getMapHandler().isKitMap() || Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            if (kitName.equals("ArcherPython") || kitName.equals("ArcherApollo") || kitName.equals("ArcherHades") || kitName.equals("ArcherMedusa")) {
                return false;
            }
        }

        return player.hasPermission("hcteams.staff") || player.hasPermission("hcteams.youtuber") || player.hasPermission("hcteams.famous") || player.hasPermission("kit." + kitName.toLowerCase());

    }


}
