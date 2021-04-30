/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.events.koth.commands.koth;

import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class KOTHSetTierCommand {

    @Command(names = { "KOTH SetTier" }, permission = "foxtrot.koth.admin")
    public static void setTier(CommandSender sender, @Param(name = "koth") Event event, @Param(name = "tier") Integer tier){
        if(tier > 3 || tier < 1){
            sender.sendMessage(ChatColor.RED + "The KOTH tier must be between 1 and 3!");
            return;
        }
        ((KOTH) event).setTier(tier);
        sender.sendMessage(ChatColor.GRAY + "Set tier of KOTH " + event.getName() + " to " + tier + ".");
    }

}
