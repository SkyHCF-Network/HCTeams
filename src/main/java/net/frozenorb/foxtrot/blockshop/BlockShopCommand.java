/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.blockshop;

import net.frozenorb.foxtrot.blockshop.menu.BlockShopMenu;
import net.frozenorb.foxtrot.commands.CustomTimerCreateCommand;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class BlockShopCommand {

    @Command(names = "blockshop", permission = "")
    public static void blockShop(Player player){
        if(!CustomTimerCreateCommand.isSOTWTimer()){
            if(!LandBoard.getInstance().getTeam(player.getLocation()).hasDTRBitmask(DTRBitmask.SAFE_ZONE) || player.getGameMode() == GameMode.CREATIVE){
               new BlockShopMenu().openMenu(player);
            }else{
                player.sendMessage(BukkitChat.format("&cYou must be in &aSpawn &cto use this command."));
            }
        }else{
            new BlockShopMenu().openMenu(player);
        }
    }

}
