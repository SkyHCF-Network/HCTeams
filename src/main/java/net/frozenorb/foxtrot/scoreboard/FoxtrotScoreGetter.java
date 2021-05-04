package net.frozenorb.foxtrot.scoreboard;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sun.javafx.UnmodifiableArrayList;
import net.frozenorb.basic.Basic;
import net.frozenorb.command.TPSCommand;
import net.frozenorb.foxtrot.nametag.ClientListener;
import net.frozenorb.foxtrot.team.TeamHandler;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.util.Cooldown;
import net.frozenorb.foxtrot.util.Cooldowns;
import net.frozenorb.foxtrot.util.yml.LangsFile;
import net.frozenorb.qlib.util.TPSUtils;
import net.frozenorb.qmodsuite.qModSuite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bson.types.ObjectId;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.CustomTimerCreateCommand;
import net.frozenorb.foxtrot.commands.EOTWCommand;
import net.frozenorb.foxtrot.events.conquest.game.ConquestGame;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.EventType;
import net.frozenorb.foxtrot.events.dtc.DTC;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.listener.GoldenAppleListener;
import net.frozenorb.foxtrot.map.stats.StatsEntry;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ArcherClass;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.BardClass;
import net.frozenorb.foxtrot.server.ServerHandler;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.commands.team.TeamStuckCommand;
import net.frozenorb.foxtrot.util.Logout;
import net.frozenorb.qlib.autoreboot.AutoRebootHandler;
import net.frozenorb.qlib.scoreboard.ScoreFunction;
import net.frozenorb.qlib.scoreboard.ScoreGetter;
import net.frozenorb.qlib.util.LinkedList;
import net.frozenorb.qlib.util.TimeUtils;
import net.frozenorb.foxtrot.util.Cooldown;
import org.spigotmc.TicksPerSecondCommand;

import javax.naming.Name;

public class FoxtrotScoreGetter implements ScoreGetter {
    private static DecimalFormat df = new DecimalFormat("0.00");
//    private Object Team;

    public void getScores(LinkedList<String> scores, Player player) {
        String spawnTagScore = getSpawnTagScore(player);
        String enderpearlScore = getEnderpearlScore(player);
        String pvpTimerScore = getPvPTimerScore(player);
        String archerMarkScore = getArcherMarkScore(player);
        String bardEffectScore = getBardEffectScore(player);
        String bardEnergyScore = getBardEnergyScore(player);
        String fstuckScore = getFStuckScore(player);
        String logoutScore = getLogoutScore(player);
        String homeScore = getHomeScore(player);
        String appleScore = getAppleScore(player);

        if (Foxtrot.getInstance().getMapHandler().isKitMap() && !player.hasMetadata("frozen") || Foxtrot.getInstance().getServerHandler().isVeltKitMap() && !player.hasMetadata("frozen")) {
            StatsEntry stats = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(player.getUniqueId());

            scores.add("&b&lKills&7: &f" + stats.getKills());
            scores.add("&b&lDeaths&7: &f" + stats.getDeaths());
            scores.add("&b&lKillstreak&7: &f" + stats.getKillstreak());
        }

        if(Foxtrot.getInstance().getClaimOptionMap().isScoreboardClaimEnabled(player.getUniqueId())){
            String claim = "&7Wilderness";
            if(Foxtrot.getInstance().getServerHandler().isWarzone(player.getLocation())){
                claim = "&cWarzone";
            }else {
                claim = LandBoard.getInstance().getTeam(player.getLocation()).getName(player);
            }
            scores.add("&6&lClaim&7: &r" + claim);
        }

        if (spawnTagScore != null && !player.hasMetadata("frozen")) {
            scores.add("&4&lCombat&7: &c" + spawnTagScore);
        }

        if(Cooldowns.isOnCooldown("ability", player)){
            scores.add("&d&lAbility Item&7: &c" + ScoreFunction.TIME_FANCY.apply(Cooldowns.getCooldownForPlayerLong("ability", player) / 1000.0f));
        }

        if (homeScore != null && !player.hasMetadata("frozen")) {
            scores.add("&9&lHome§7: &c" + homeScore);
        }

        if (appleScore != null && !player.hasMetadata("frozen")) {
            scores.add("&e&lApple&7: &c" + appleScore);
        }

        if (enderpearlScore != null && !player.hasMetadata("frozen")) {
            scores.add("&e&lEnderpearl&7: &c" + enderpearlScore);
        }

        if (pvpTimerScore != null && !player.hasMetadata("frozen")) {
            if (Foxtrot.getInstance().getStartingPvPTimerMap().get(player.getUniqueId())) {
                scores.add("&a&lStarting Timer&7: &c" + pvpTimerScore);
            } else {
                scores.add("&a&lPvP Timer&7: &c" + pvpTimerScore);
            }
        }

        Iterator<Map.Entry<String, Long>> iterator = CustomTimerCreateCommand.getCustomTimers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> timer = iterator.next();
            if (timer.getValue() < System.currentTimeMillis()) {
                iterator.remove();
                continue;
            }

            if (timer.getKey().equals("&a&lSOTW") && !player.hasMetadata("frozen")) {
                if (CustomTimerCreateCommand.hasSOTWEnabled(player.getUniqueId())) {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&a&l&mSOTW &a&mends in &a&l&m" + getTimerScore(timer)));
                } else {
                    scores.add(ChatColor.translateAlternateColorCodes('&', "&a&lSOTW &aends in &a&l" + getTimerScore(timer)));
                }
            } else {
                scores.add(ChatColor.translateAlternateColorCodes('&', timer.getKey()) + "&7: &c" + getTimerScore(timer));
            }
        }

        for (Event event : Foxtrot.getInstance().getEventHandler().getEvents() ) {
            if (!event.isActive() || event.isHidden() || player.hasMetadata("frozen")) {
                continue;
            }

            String displayName;

            switch (event.getName()) {
            case "EOTW":
                displayName = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "EOTW";
                break;
            case "Citadel":
                displayName = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Citadel";
                break;
            default:
                displayName = ChatColor.BLUE.toString() + ChatColor.BOLD + event.getName();
                break;
            }

            if (event.getType() == EventType.DTC) {
                scores.add(displayName + "&7: &c" + ((DTC) event).getCurrentPoints());
            } else {
                scores.add(displayName + "&7: &c" + ScoreFunction.TIME_SIMPLE.apply((float) ((KOTH) event).getRemainingCapTime()));
            }
        }

        if (EOTWCommand.isFfaEnabled() && !player.hasMetadata("frozen")) {
            long ffaEnabledAt = EOTWCommand.getFfaActiveAt();
            if (System.currentTimeMillis() < ffaEnabledAt) {
                long difference = ffaEnabledAt - System.currentTimeMillis();
                scores.add("&4&lFFA&7: &c" + ScoreFunction.TIME_SIMPLE.apply(difference / 1000F));
            }
        }

        if(player.hasMetadata("ModMode") || player.hasMetadata("modmode")) {
            scores.add("&eTPS&7: &r" + formatAdvancedTps(TPSUtils.getTPS()));
            scores.add("&eChat&7: &r" + (Basic.getInstance().getChatManager().isMuted() ? "&cMuted" : (Basic.getInstance().getChatManager().isSlowed() ? "&6Slowed (15s)" : "&aNormal")));
            scores.add("&eOnline&7: &r" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers());
        }

        if (archerMarkScore != null && !player.hasMetadata("frozen")) {
            scores.add("&6&lArcher Mark&7: &c" + archerMarkScore);
        }

        if (bardEffectScore != null && !player.hasMetadata("frozen")) {
            scores.add("&a&lBard Effect&7: &c" + bardEffectScore);
        }

        if (bardEnergyScore != null && !player.hasMetadata("frozen")) {
            scores.add("&b&lBard Energy&7: &c" + bardEnergyScore);
        }

        if (fstuckScore != null && !player.hasMetadata("frozen")) {
            scores.add("&4&lStuck&7: &c" + fstuckScore);
        }

        if (logoutScore != null && !player.hasMetadata("frozen")) {
            scores.add("&4&lLogout&7: &c" + logoutScore);
        }

        if (Cooldowns.isOnCooldown("grapple", player)) {
        scores.add((String) ("&c&lGrappling Hook&7: &f" + ScoreFunction.TIME_SIMPLE.apply((Float) (Cooldowns.getCooldownForPlayerLong("grapple", player) / 1000.0f))));
    }
        if (Foxtrot.getInstance().getPartnerItem().onCooldown(player)) {
            scores.add((String) ("&d&lPartner Item&7: &c" + df.format(Foxtrot.getInstance().getPartnerItem().getRemainingMilis(player) / 1000.0f) + "&cs"));
        }

        ConquestGame conquest = Foxtrot.getInstance().getConquestHandler().getGame();

        if (conquest != null && !player.hasMetadata("frozen")) {
            if (scores.size() != 0) {
                scores.add("&c&7&m--------------------");
            }

            scores.add("&e&lConquest:");
            int displayed = 0;

            for (Map.Entry<ObjectId, Integer> entry : conquest.getTeamPoints().entrySet()) {
                Team resolved = Foxtrot.getInstance().getTeamHandler().getTeam(entry.getKey());

                if (resolved != null) {
                    scores.add("  " + resolved.getName(player) + "&7: &f" + entry.getValue());
                    displayed++;
                }

                if (displayed == 3) {
                    break;
                }
            }

            if (displayed == 0) {
                scores.add("  &7No scores yet");
            }
            if (Foxtrot.getInstance().getTeamHandler().getTeam(player) != null) {
                    Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
                    if (team.getFactionFocused() != null) {
                        for (String sb : LangsFile.getConfig().getStringList("langs.scoreboard.type.teamFocus")) {
                            Team focusedTeam = team.getFactionFocused();

                            sb = sb.replace("%arrow%" , "\u00BB");
                            sb = sb.replace("%team%", focusedTeam.getName());
                            sb = sb.replace("%dtr%", ClientListener.getDTRColor(focusedTeam) + Team.DTR_FORMAT.format(focusedTeam.getDTR()) + ClientListener.getDTRSuffix(focusedTeam));
                            sb = sb.replace("%online%", focusedTeam.getOnlineMemberAmount() + "");

                            scores.add(sb);

                        }

                    }

            }
        }

        /* Handle timers
        Collection<Timer> timers = this.plugin.getTimerManager().getTimers();
        for (Timer timer : timers) {
            if (timer instanceof PlayerTimer) {
                PlayerTimer playerTimer = (PlayerTimer) timer;
                long remaining3 = playerTimer.getRemaining(player);
                if (remaining3 <= 0L) {
                    continue;
                }
                String timerName = playerTimer.getName();
                if (timerName.length() > 14) {
                    timerName = timerName;
                }
                lines.add(new SidebarEntry(timer.getScoreboardPrefix(), timerName, ": " + ChatColor.WHITE + DurationUtils.getRemaining(remaining3, true)));
            }
            if (timer instanceof GlobalTimer) {
                GlobalTimer playerTimer = (GlobalTimer) timer;
                long remaining3 = playerTimer.getRemaining();
                if (remaining3 <= 0L) {
                    continue;

         */

        if (AutoRebootHandler.isRebooting()) {
            scores.add("&4&lReboot&7: &c" + TimeUtils.formatIntoMMSS(AutoRebootHandler.getRebootSecondsRemaining()));
        }
        if (player.hasMetadata("frozen")) {
            scores.add("&4&lFrozen&7:");
            scores.add("&r &7» &rYou have been frozen.");
            scores.add("&r &7» &fJoin our TeamSpeak3&7:");
            scores.add("&r &7» &bts.skyhcf.net");
        }
        if (!scores.isEmpty()) {
            // 'Top' and bottom.
            scores.addFirst("&a&7&m--------------------");
            scores.add(scores.size(), (""));
            scores.add(scores.size(), ("&7&owww.skyhcf.net"));
            scores.add("&b&7&m--------------------");
        }
    }

    public String getAppleScore(Player player) {
        if (GoldenAppleListener.getCrappleCooldown().containsKey(player.getUniqueId()) && GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) >= System.currentTimeMillis()) {
            float diff = GoldenAppleListener.getCrappleCooldown().get(player.getUniqueId()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getHomeScore(Player player) {
        if (ServerHandler.getHomeTimer().containsKey(player.getName()) && ServerHandler.getHomeTimer().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = ServerHandler.getHomeTimer().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getFStuckScore(Player player) {
        if (TeamStuckCommand.getWarping().containsKey(player.getName())) {
            float diff = TeamStuckCommand.getWarping().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
    }

    public String getLogoutScore(Player player) {
        Logout logout = ServerHandler.getTasks().get(player.getName());

        if (logout != null) {
            float diff = logout.getLogoutTime() - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return null;
    }

    public String getSpawnTagScore(Player player) {
        if (SpawnTagHandler.isTagged(player)) {
            float diff = SpawnTagHandler.getTag(player);

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    private static String formatAdvancedTps(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + Math.min(Math.round(tps * 100.0D) / 100.0, 20.0);
    }

    public String getEnderpearlScore(Player player) {
        if (EnderpearlCooldownHandler.getEnderpearlCooldown().containsKey(player.getName()) && EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = EnderpearlCooldownHandler.getEnderpearlCooldown().get(player.getName()) - System.currentTimeMillis();

            if (diff >= 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getPvPTimerScore(Player player) {
        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
            int secondsRemaining = Foxtrot.getInstance().getPvPTimerMap().getSecondsRemaining(player.getUniqueId());

            if (secondsRemaining >= 0) {
                return (ScoreFunction.TIME_SIMPLE.apply((float) secondsRemaining));
            }
        }

        return (null);
    }

    public String getTimerScore(Map.Entry<String, Long> timer) {
        long diff = timer.getValue() - System.currentTimeMillis();

        if (diff > 0) {
            return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
        } else {
            return (null);
        }
    }

    public String getArcherMarkScore(Player player) {
        if (ArcherClass.isMarked(player)) {
            long diff = ArcherClass.getMarkedPlayers().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_FANCY.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getBardEffectScore(Player player) {
        if (BardClass.getLastEffectUsage().containsKey(player.getName()) && BardClass.getLastEffectUsage().get(player.getName()) >= System.currentTimeMillis()) {
            float diff = BardClass.getLastEffectUsage().get(player.getName()) - System.currentTimeMillis();

            if (diff > 0) {
                return (ScoreFunction.TIME_SIMPLE.apply(diff / 1000F));
            }
        }

        return (null);
    }

    public String getBardEnergyScore(Player player) {
        if (BardClass.getEnergy().containsKey(player.getName())) {
            float energy = BardClass.getEnergy().get(player.getName());

            if (energy > 0) {
                // No function here, as it's a "raw" value.
                return (String.valueOf(BardClass.getEnergy().get(player.getName())));
            }
        }

        return (null);
    }



}