package net.frozenorb.foxtrot;

import com.comphenix.protocol.ProtocolLibrary;
import com.mongodb.MongoClient;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.abilities.AbilityHandler;
import net.frozenorb.foxtrot.chat.ChatHandler;
import net.frozenorb.foxtrot.crates.CrateHandler;
import net.frozenorb.foxtrot.deathmessage.DeathMessageHandler;
import net.frozenorb.foxtrot.events.EventHandler;
import net.frozenorb.foxtrot.events.citadel.CitadelHandler;
import net.frozenorb.foxtrot.events.conquest.ConquestHandler;
import net.frozenorb.foxtrot.events.region.carepackage.CarePackageHandler;
import net.frozenorb.foxtrot.events.region.cavern.CavernHandler;
import net.frozenorb.foxtrot.events.region.glowmtn.GlowHandler;
import net.frozenorb.foxtrot.events.undead.AntiBurnRunnable;
import net.frozenorb.foxtrot.events.undead.UndeadRunnable;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.map.MapHandler;
import net.frozenorb.foxtrot.nametag.ClientListener;
import net.frozenorb.foxtrot.packetborder.PacketBorderThread;
import net.frozenorb.foxtrot.persist.RedisSaveTask;
import net.frozenorb.foxtrot.persist.maps.*;
import net.frozenorb.foxtrot.persist.maps.statistics.BaseStatisticMap;
import net.frozenorb.foxtrot.persist.maps.statistics.EnderPearlsUsedMap;
import net.frozenorb.foxtrot.persist.maps.statistics.ExpCollectedMap;
import net.frozenorb.foxtrot.persist.maps.statistics.ItemsRepairedMap;
import net.frozenorb.foxtrot.persist.maps.statistics.SplashPotionsBrewedMap;
import net.frozenorb.foxtrot.persist.maps.statistics.SplashPotionsUsedMap;
import net.frozenorb.foxtrot.protocol.ClientCommandPacketAdaper;
import net.frozenorb.foxtrot.protocol.SignGUIPacketAdaper;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.server.ServerHandler;
import net.frozenorb.foxtrot.tab.FoxtrotTabLayoutProvider;
import net.frozenorb.foxtrot.team.TeamHandler;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.commands.team.TeamClaimCommand;
import net.frozenorb.foxtrot.team.commands.team.subclaim.TeamSubclaimCommand;
import net.frozenorb.foxtrot.team.dtr.DTRHandler;
import net.frozenorb.foxtrot.util.Cooldown;
import net.frozenorb.foxtrot.util.Cooldowns;
import net.frozenorb.foxtrot.util.ReclaimConfig;
import net.frozenorb.foxtrot.util.RegenUtils;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import net.frozenorb.qlib.qLib;
import net.frozenorb.qlib.tab.FrozenTabHandler;
import net.minecraft.server.v1_7_R4.Item;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

public class Foxtrot extends JavaPlugin {

    public static String MONGO_DB_NAME = "Centile";
    @Getter
    private static Foxtrot instance;

    @Getter
    private MongoClient mongoPool;

    @Getter
    private ChatHandler chatHandler;
    @Getter
    private AbilityHandler abilityHandler;
    @Getter
    private PvPClassHandler pvpClassHandler;
    @Getter
    private TeamHandler teamHandler;
    @Getter
    private ServerHandler serverHandler;
    @Getter
    private MapHandler mapHandler;
    @Getter
    private CitadelHandler citadelHandler;
    @Getter
    private EventHandler eventHandler;
    @Getter
    private ConquestHandler conquestHandler;
    @Getter
    private CavernHandler cavernHandler;
    @Getter
    private GlowHandler glowHandler;
    @Getter
    private CrateHandler crateHandler;

    @Getter
    private PlaytimeMap playtimeMap;
    @Getter
    private ClaimOptionMap claimOptionMap;
    @Getter
    private OppleMap oppleMap;
    @Getter
    private DeathbanMap deathbanMap;
    @Getter
    private PvPTimerMap PvPTimerMap;
    @Getter
    private StartingPvPTimerMap startingPvPTimerMap;
    @Getter
    private DeathsMap deathsMap;
    @Getter
    private KillsMap killsMap;
    @Getter
    private ChatModeMap chatModeMap;
    @Getter
    private FishingKitMap fishingKitMap;
    @Getter
    private ToggleGlobalChatMap toggleGlobalChatMap;
    @Getter
    private ChatSpyMap chatSpyMap;
    @Getter
    private DiamondMinedMap diamondMinedMap;
    @Getter
    private GoldMinedMap goldMinedMap;
    @Getter
    private IronMinedMap ironMinedMap;
    @Getter
    private CoalMinedMap coalMinedMap;
    @Getter
    private RedstoneMinedMap redstoneMinedMap;
    @Getter
    private LapisMinedMap lapisMinedMap;
    @Getter
    private EmeraldMinedMap emeraldMinedMap;
    @Getter
    private FirstJoinMap firstJoinMap;
    @Getter
    private LastJoinMap lastJoinMap;
    @Getter
    private SoulboundLivesMap soulboundLivesMap;
    @Getter
    private FriendLivesMap friendLivesMap;
    @Getter
    private BaseStatisticMap enderPearlsUsedMap;
    @Getter
    private BaseStatisticMap expCollectedMap;
    @Getter
    private BaseStatisticMap itemsRepairedMap;
    @Getter
    private BaseStatisticMap splashPotionsBrewedMap;
    @Getter
    private BaseStatisticMap splashPotionsUsedMap;
    @Getter
    private WrappedBalanceMap wrappedBalanceMap;
    @Getter
    private ToggleFoundDiamondsMap toggleFoundDiamondsMap;
    @Getter
    private ToggleDeathMessageMap toggleDeathMessageMap;
    @Getter
    private TabListModeMap tabListModeMap;
    @Getter
    private IPMap ipMap;
    @Getter
    private WhitelistedIPMap whitelistedIPMap;
    @Getter
    private CobblePickupMap cobblePickupMap;
    @Getter
    private KDRMap kdrMap;

    @Getter
    private CombatLoggerListener combatLoggerListener;
    @Getter
    @Setter
    // for the case of some commands in the plugin,
    // a player shouldn't be able to do them in a duel
    // thus this predicate exists to test that to avoid dep. issues

    private Cooldown switcher = new Cooldown();
    public Cooldown getSwitcher(){ return this.switcher; }

    private Cooldown buildstick = new Cooldown();
    public Cooldown getAntibuildstick(){ return this.buildstick; }

    private Cooldown restricted = new Cooldown();
    public Cooldown getRestricted(){ return this.restricted; }

    private Cooldown fakepearl = new Cooldown();
    public Cooldown getFakePearl(){ return this.fakepearl; }

    private Cooldown ragesoup = new Cooldown();
    public Cooldown getRageSoup(){ return this.ragesoup; }

    private Cooldown ninjastar = new Cooldown();
    public Cooldown getNinjaStar(){ return this.ninjastar; }

    private Cooldown instantinvis = new Cooldown();
    public Cooldown getInstantInvis(){ return this.instantinvis; }

    private Cooldown potchecker = new Cooldown();
    public Cooldown getPotChecker(){ return this.potchecker; }


    private Cooldown lifesaver = new Cooldown();
    public Cooldown getLifeSaver(){ return this.lifesaver; }

    private Cooldown lifesaversafe = new Cooldown();
    public Cooldown getLifeSaverSafe(){ return this.lifesaversafe; }

    private Cooldown tricktreat = new Cooldown();
    public Cooldown getTrickOrTreat(){ return this.tricktreat; }

    private Cooldown partneritem = new Cooldown();
    public Cooldown getPartnerItem(){ return this.partneritem; }

    private Cooldown portablebard = new Cooldown();
    public Cooldown getPortableBard(){return this.portablebard; }

    private Cooldown astrokit = new Cooldown();
    public Cooldown getAstroKit(){ return this.astrokit; }
    private Predicate<Player> inDuelPredicate = (player) -> false;

    @Getter @Setter private boolean undead;

    @Override
    public void onEnable() {
        if (Bukkit.getServerName().contains(" ")) {
            System.out.println("*********************************************");
            System.out.println("               ATTENTION");
            System.out.println("SET server-name VALUE IN server.properties TO");
            System.out.println("A PROPER SERVER NAME. THIS WILL BE USED AS THE");
            System.out.println("MONGO DATABASE NAME.");
            System.out.println("*********************************************");
            this.getServer().shutdown();
            return;
        }
        if (getConfig().getString("Mongo.DBName").equalsIgnoreCase("") || getConfig().getString("Mongo.DBName").contains(" ")) {
            System.out.println("*********************************************");
            System.out.println("               ATTENTION");
            System.out.println("SET DBname VALUE IN config.yml TO");
            System.out.println("A PROPER SERVER NAME. THIS WILL BE USED AS THE");
            System.out.println("SERVER NAME.");
            System.out.println("*********************************************");
            this.getServer().shutdown();
            return;
        }

        SpigotConfig.onlyCustomTab = false; // because I know we'll forget
        instance = this;
        saveDefaultConfig();

        try {
            this.mongoPool = new MongoClient(this.getConfig().getString("Mongo.Host", "localhost"));
            Foxtrot.MONGO_DB_NAME = Bukkit.getServerName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        (new DTRHandler()).runTaskTimer(this, 20L, 1200L);
        (new RedisSaveTask()).runTaskTimerAsynchronously(this, 1200L, 1200L);
        (new PacketBorderThread()).start();

        (new UndeadRunnable()).runTaskTimer(this, 20L, 160L);
        (new AntiBurnRunnable()).runTaskTimer(this, 20L, 5L);

        setupHandlers();
        setupPersistence();
        setupListeners();
        createCooldowns();;

        FrozenTabHandler.setLayoutProvider(new FoxtrotTabLayoutProvider());

        ProtocolLibrary.getProtocolManager().addPacketListener(new SignGUIPacketAdaper());
        ProtocolLibrary.getProtocolManager().addPacketListener(new ClientCommandPacketAdaper());

        ReclaimConfig.createit();

        for (World world : Bukkit.getWorlds()) {
            world.setThundering(false);
            world.setStorm(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("mobGriefing", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setTime(1200L);
        }

/*        if (getConfig().getBoolean("legions")) {
            try {
                Field field = Item.class.getDeclaredField("maxStackSize");
                field.setAccessible(true);
                field.setInt(Item.getById(322), 6);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }*/

        // we just define this here while we're testing, if we actually
        // accept this feature it'll be moved to somewhere better
        new ServerFakeFreezeTask().runTaskTimerAsynchronously(this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        getEventHandler().saveEvents();

        for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
            getPlaytimeMap().playerQuit(player.getUniqueId(), false);
            player.setMetadata("loggedout", new FixedMetadataValue(this, true));
        }

        for (String playerName : PvPClassHandler.getEquippedKits().keySet()) {
            PvPClassHandler.getEquippedKits().get(playerName).remove(getServer().getPlayerExact(playerName));
        }


        for (Entity e : this.combatLoggerListener.getCombatLoggers()) {
            if (e != null) {
                e.remove();
            }
        }

        RedisSaveTask.save(null, false);
        getInstance().getServerHandler().save();
        if (getInstance().getMapHandler().isKitMap() || getInstance().getServerHandler().isVeltKitMap()) {
            getInstance().getMapHandler().getStatsHandler().save();
        }

        RedisSaveTask.save(null, false);
        Foxtrot.getInstance().getServerHandler().save();

        if (Foxtrot.getInstance().getMapHandler().isKitMap() || Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            Foxtrot.getInstance().getMapHandler().getStatsHandler().save();
        }

        RegenUtils.resetAll();

        qLib.getInstance().runRedisCommand((jedis) -> {
            jedis.save();
            return null;
        });
    }

    private void setupHandlers() {
        serverHandler = new ServerHandler();
        mapHandler = new MapHandler();
        mapHandler.load();

        abilityHandler = new AbilityHandler(this);

        teamHandler = new TeamHandler();
        LandBoard.getInstance().loadFromTeams();

        chatHandler = new ChatHandler();
        citadelHandler = new CitadelHandler();
        pvpClassHandler = new PvPClassHandler();
        eventHandler = new EventHandler();
        conquestHandler = new ConquestHandler();

        if (getConfig().getBoolean("glowstoneMountain", false)) {
            glowHandler = new GlowHandler();
        }

        if (getConfig().getBoolean("cavern", false)) {
            cavernHandler = new CavernHandler();
        }

        crateHandler = new CrateHandler();

        FrozenCommandHandler.registerAll(this);

        DeathMessageHandler.init();
        DTRHandler.loadDTR();
    }

    private void createCooldowns() {
        Cooldowns.createCooldown("lff");
    }


    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new JoinMessageListener(), this);
        getServer().getPluginManager().registerEvents(new MapListener(), this);
        getServer().getPluginManager().registerEvents(new UndeadListener(), this);
        getServer().getPluginManager().registerEvents(new AntiGlitchListener(), this);
        getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new ClientListener(), this);
        getServer().getPluginManager().registerEvents(new BorderListener(), this);
        getServer().getPluginManager().registerEvents((combatLoggerListener = new CombatLoggerListener()), this);
        getServer().getPluginManager().registerEvents(new CrowbarListener(), this);
        getServer().getPluginManager().registerEvents(new PearlListener(), this);
        getServer().getPluginManager().registerEvents(new DeathbanListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentLimiterListener(), this);
        getServer().getPluginManager().registerEvents(new EnderpearlCooldownHandler(), this);
        getServer().getPluginManager().registerEvents(new EndListener(), this);
        getServer().getPluginManager().registerEvents(new FoundDiamondsListener(), this);
        getServer().getPluginManager().registerEvents(new FoxListener(), this);
        getServer().getPluginManager().registerEvents(new GoldenAppleListener(), this);
        getServer().getPluginManager().registerEvents(new KOTHRewardKeyListener(), this);
        getServer().getPluginManager().registerEvents(new ConquestRewardKeyListener(), this);
        getServer().getPluginManager().registerEvents(new PvPTimerListener(), this);
        getServer().getPluginManager().registerEvents(new PotionLimiterListener(), this);
        getServer().getPluginManager().registerEvents(new NetherPortalListener(), this);
        getServer().getPluginManager().registerEvents(new PortalTrapListener(), this);
        getServer().getPluginManager().registerEvents(new SignSubclaimListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerTrackerListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnTagListener(), this);
        getServer().getPluginManager().registerEvents(new StaffUtilsListener(), this);
        getServer().getPluginManager().registerEvents(new TeamListener(), this);
        getServer().getPluginManager().registerEvents(new WebsiteListener(), this);
        getServer().getPluginManager().registerEvents(new TeamSubclaimCommand(), this);
        getServer().getPluginManager().registerEvents(new TeamClaimCommand(), this);
        getServer().getPluginManager().registerEvents(new StatTrakListener(), this);
        getServer().getPluginManager().registerEvents(new ElevatorListener(this), this);

        if (getServerHandler().isReduceArmorDamage()) {
            getServer().getPluginManager().registerEvents(new ArmorDamageListener(), this);
        }

        if (getServerHandler().isBlockEntitiesThroughPortals()) {
            getServer().getPluginManager().registerEvents(new EntityPortalListener(), this);
        }

        if (getServerHandler().isBlockRemovalEnabled()) {
            getServer().getPluginManager().registerEvents(new BlockRegenListener(), this);
        }

        // Register kitmap specific listeners
        if (getServerHandler().isVeltKitMap() || getMapHandler().isKitMap()) {
            getServer().getPluginManager().registerEvents(new KitMapListener(), this);
            getServer().getPluginManager().registerEvents(new CarePackageHandler(), this);
        }

        getServer().getPluginManager().registerEvents(new BlockConvenienceListener(), this);

        //getServer().getPluginManager().registerEvents(new ChunkLimiterListener(), this );
        //getServer().getPluginManager().registerEvents(new IPListener(), this );
        //getServer().getPluginManager().registerEvents(new Prot3Sharp3Listener(), this);
    }

    private void setupPersistence() {
        (playtimeMap = new PlaytimeMap()).loadFromRedis();
        (oppleMap = new OppleMap()).loadFromRedis();
        (deathbanMap = new DeathbanMap()).loadFromRedis();
        (PvPTimerMap = new PvPTimerMap()).loadFromRedis();
        (startingPvPTimerMap = new StartingPvPTimerMap()).loadFromRedis();
        (deathsMap = new DeathsMap()).loadFromRedis();
        (killsMap = new KillsMap()).loadFromRedis();
        (chatModeMap = new ChatModeMap()).loadFromRedis();
        (toggleGlobalChatMap = new ToggleGlobalChatMap()).loadFromRedis();
        (fishingKitMap = new FishingKitMap()).loadFromRedis();
        (soulboundLivesMap = new SoulboundLivesMap()).loadFromRedis();
        (friendLivesMap = new FriendLivesMap()).loadFromRedis();
        (chatSpyMap = new ChatSpyMap()).loadFromRedis();
        (diamondMinedMap = new DiamondMinedMap()).loadFromRedis();
        (goldMinedMap = new GoldMinedMap()).loadFromRedis();
        (ironMinedMap = new IronMinedMap()).loadFromRedis();
        (coalMinedMap = new CoalMinedMap()).loadFromRedis();
        (redstoneMinedMap = new RedstoneMinedMap()).loadFromRedis();
        (lapisMinedMap = new LapisMinedMap()).loadFromRedis();
        (emeraldMinedMap = new EmeraldMinedMap()).loadFromRedis();
        (firstJoinMap = new FirstJoinMap()).loadFromRedis();
        (lastJoinMap = new LastJoinMap()).loadFromRedis();
        (enderPearlsUsedMap = new EnderPearlsUsedMap()).loadFromRedis();
        (expCollectedMap = new ExpCollectedMap()).loadFromRedis();
        (itemsRepairedMap = new ItemsRepairedMap()).loadFromRedis();
        (splashPotionsBrewedMap = new SplashPotionsBrewedMap()).loadFromRedis();
        (splashPotionsUsedMap = new SplashPotionsUsedMap()).loadFromRedis();
        (wrappedBalanceMap = new WrappedBalanceMap()).loadFromRedis();
        (toggleFoundDiamondsMap = new ToggleFoundDiamondsMap()).loadFromRedis();
        (toggleDeathMessageMap = new ToggleDeathMessageMap()).loadFromRedis();
        (tabListModeMap = new TabListModeMap()).loadFromRedis();
        (ipMap = new IPMap()).loadFromRedis();
        (whitelistedIPMap = new WhitelistedIPMap()).loadFromRedis();
        (cobblePickupMap = new CobblePickupMap()).loadFromRedis();
        (kdrMap = new KDRMap()).loadFromRedis();
        (claimOptionMap = new ClaimOptionMap()).loadFromRedis();
    }

    public Predicate<Player> getInDuelPredicate() {
        return this.inDuelPredicate;
    }

}

