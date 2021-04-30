/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.commands;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import net.skyhcf.atmosphere.shared.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class SaleCommand {

    @Command(names = {"sale start", "storesale start"}, permission = "foxtrot.sale")
    public static void sale(CommandSender sender, @Param(name = "time") String time){
        long duration = TimeUtil.parseTime(time);
        if(duration < 0){
            sender.sendMessage(BukkitChat.format("&cInvalid time."));
            return;
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "customtimer create " + (duration / 1000L) + " &b&lStore Sale");
        sender.sendMessage(BukkitChat.format("&aStarted the store sale for " + time + "."));
    }

    @Command(names = {"sale stop", "storesale stop"}, permission = "foxtrot.sale")
    public static void saleStop(CommandSender sender){
        if(!CustomTimerCreateCommand.getCustomTimers().containsKey("&b&lStore Sale")){
            sender.sendMessage(BukkitChat.format("&cThere is currently not an ongoing sale."));
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "customtimer create 0 &b&lStore Sale");
        sender.sendMessage(BukkitChat.format("&aDeactivated the sale timer."));
    }



}
