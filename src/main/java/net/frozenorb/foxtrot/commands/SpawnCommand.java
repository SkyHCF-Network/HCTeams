package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SpawnCommand {

    @Command(names={ "spawn" }, permission="")
    public static void spawn(Player sender) {
        Player player = (Player) sender;
        if(player.getGameMode() == GameMode.CREATIVE){
            player.teleport(Foxtrot.getInstance().getServerHandler().getSpawnLocation());
        }else{
            player.sendMessage(BukkitChat.format("&cThe /spawn command is disabled, to get to spawn, you must walk there. Spawn is located at 0,0."));
        }
    }

}
