package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class TeamHelpCommand {

    @Command(names={ "team help", "t help", "f help", "faction help", "fac help" }, permission="")
    public static void teamHelp(Player player) {
        TeamCommand.team(player);
    }

}