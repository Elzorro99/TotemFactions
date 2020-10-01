package net.elzorro99.totemfactions;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.zcore.persist.PlayerEntityCollection;

import net.elzorro99.totemfactions.listeners.packets.PInjector;
import net.elzorro99.totemfactions.managers.MCommands;
import net.elzorro99.totemfactions.managers.MCrons;
import net.elzorro99.totemfactions.managers.MListeners;
import net.elzorro99.totemfactions.managers.MFiles;
import net.elzorro99.totemfactions.managers.MLoad;
import net.elzorro99.totemfactions.managers.MPlaceholderAPI;
import net.elzorro99.totemfactions.managers.MScoreboards;
import net.elzorro99.totemfactions.utils.UMetrics;
import net.elzorro99.totemfactions.utils.UMetricsOld;
import net.elzorro99.totemfactions.utils.UCheckUpdate;
import net.elzorro99.totemfactions.utils.UDataPack;
import net.elzorro99.totemfactions.utils.ULocationsTotems;
import net.elzorro99.totemfactions.utils.UPhysicBlocks;
import net.elzorro99.totemfactions.utils.UTopFactions;
import net.elzorro99.totemfactions.utils.UReflection.PackageType;
import net.elzorro99.totemfactions.utils.UScoreboardManager;
import net.elzorro99.totemfactions.utils.UScoreboardManager.ScoreboardInterfaces;
import net.elzorro99.totemfactions.utils.cron.CronScheduler;
import net.elzorro99.totemfactions.utils.UTitles;
import net.elzorro99.totemfactions.utils.UTopData;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private MLoad managerLoad;
	private MCommands managerCommands;
	private MListeners managerListeners;
	private MFiles managerFiles;
	private MScoreboards managerScoreboards;
	private MCrons managerCrons;
	private UMetrics utilsMetrics;
	private UMetricsOld utilsMetricsOld;
	private UCheckUpdate utilsCkeckUpdate;
	private ULocationsTotems utilsLocationsTotems;
	private UTopFactions utilsRankFactions;
	private UPhysicBlocks utilsPhysicBlocks;
	private UTitles utilsTitles;
	private UScoreboardManager utilsScorebordManager;
	private UTopData utilstop;
	public PInjector packetInjector;
	public ScoreboardInterfaces scoreboardManager;
	
	private CronScheduler cronScheduler;
	
	private int statusFaction;
	
	private boolean totemCreateStatus;
	private boolean totemSpawnStatus;
	private boolean featherBoardStatus = false;
	private boolean mvdwPlaceholderAPIStatus = false;
	private boolean placeholderAPIStatus = false;
	
	private Location currentTotemLocation;
	
	private String cronPredictor;
	private String currentTotemName = "None";
	private String currentFactionName = "None";
	private String versionPlugin = "v1.12.4";
	private String versionServer = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	private Integer versionServerSplit = Integer.parseInt(PackageType.getServerVersion().split("_")[1]);
	private Integer timerMinutes = 0;
	private Integer timerSecondes = 0;
	
	
	private HashMap<Player, ScoreboardInterfaces> scoreboardPlayer = new HashMap<>();
	private ArrayList<Player> fbList = new ArrayList<Player>();
	private ArrayList<Location> locationBlocksTotem = new ArrayList<Location>();
	
	public File fileConfig = new File(getDataFolder().getPath() + "/config.yml");
	public FileConfiguration fileConfigConfig;

	public File fileMessages = new File(getDataFolder().getPath(), "/messages.yml");
	public FileConfiguration fileConfigMessages;
	
	public File fileStats = new File(getDataFolder().getPath(), "/stats/stats.yml");
	public FileConfiguration fileConfigStats;
	
	public File fileLocations = new File(getDataFolder().getPath(), "/locations.yml");
	public FileConfiguration fileConfigLocations;
	
	@Override
	public void onEnable() {
		instance = this;
		if(getVersionServer().equals("v1_7_R4")) utilsMetricsOld = new UMetricsOld(instance);
		else utilsMetrics = new UMetrics(instance, 4193);
		utilsCkeckUpdate = new UCheckUpdate(instance, 45986);
		managerLoad = new MLoad();
		managerCommands = new MCommands();
		managerListeners = new MListeners();
		managerFiles = new MFiles();
		managerScoreboards = new MScoreboards();
		managerCrons = new MCrons();
		cronScheduler = new CronScheduler();
		utilsScorebordManager = new UScoreboardManager();
		utilsLocationsTotems = new ULocationsTotems();
		utilsRankFactions = new UTopFactions();
		utilsPhysicBlocks = new UPhysicBlocks();
		utilsTitles = new UTitles();
		utilstop = new UTopData();
		managerLoad.loadPlugin();
	}
	
	@Override
	public void onDisable() {
		managerLoad.unLoadPlugin();
	}
	
	// Get class.
	public static Main getInstance() {
		return instance;
	}
	
	public UMetrics getUtilsMetrics() {
		return utilsMetrics;
	}
	
	public UMetricsOld getUtilsMetricsOld() {
		return utilsMetricsOld;
	}
	
	public UCheckUpdate getUtilsCheckUpdate() {
		return utilsCkeckUpdate;
	}
	
	public MLoad getManagerLoad() {
		return managerLoad;
	}
	
	public MCommands getManagerCommands() {
		return managerCommands;
	}
	
	public MListeners getManagerListeners() {
		return managerListeners;
	}
	
	public MFiles getManagerFiles() {
		return managerFiles;
	}
	
	public MScoreboards getManagerScorebords() {
		return managerScoreboards;
	}
	
	public MCrons getManagerCrons() {
		return managerCrons;
	}
	
	public ULocationsTotems getUtilsLocationsTotems() {
		return utilsLocationsTotems;
	}
	
	public UTopFactions getUtilsRankFactions() {
		return utilsRankFactions;
	}
	
	public UPhysicBlocks getUtilsPhysicBlocks() {
		return utilsPhysicBlocks;
	}
	
	public UTitles getUtilsTitles() {
		return utilsTitles;
	}
	
	public UScoreboardManager getUtilsScorebordsManager() {
		return utilsScorebordManager;
	}
	
	public UTopData getUtilsTop() {
		return utilstop;
	}
	
	public CronScheduler getCronScheduler() {
		return cronScheduler;
	}
	
	// Get Int.
	public int getStatusFaction() {
		return statusFaction;
	}
	
	// Get boolean.
	public boolean getTotemCreateStatus() {
		return totemCreateStatus;
	}
	
	public boolean getTotemSpawnStatus() {
		return totemSpawnStatus;
	}
	
	public boolean getFeatherBoardStatus() {
		return featherBoardStatus;
	}
	
	public boolean getMVdWPlaceholderAPIStatus() {
		return mvdwPlaceholderAPIStatus;
	}
	
	public boolean getPlaceholderAPIStatus() {
		return placeholderAPIStatus;
	}
	
	// Get Location.
	public Location getCurrentTotemLocation() {
		return currentTotemLocation;
	}
	
	// Get String.
	public String getCronPredictor() {
		return cronPredictor;
	}
	
	public String getCurrentTotemName() {
		return currentTotemName;
	}
	
	public String getCurrentFactionName() {
		return currentFactionName;
	}
	
	public String getVersionPlugin() {
		return versionPlugin;
	}
	
	public String getVersionServer() {
		return versionServer;
	}
	
	// Ger Integer.
	public Integer getTimerMinutes() {
		return timerMinutes;
	}
	
	public Integer getTimerSecondes() {
		return timerSecondes;
	}
	
	public PInjector getPacketInjector() {
		return packetInjector;
	}
	
	// Get HashMap.
	public HashMap<Player, ScoreboardInterfaces> getScoreboardsPlayer() {
		return scoreboardPlayer;
	}
	
	// Get ArrayList.
	public ArrayList<Player> getFbList() {
		return fbList;
	}
	
	public ArrayList<Location> getLocationBlocksTotem() {
		return locationBlocksTotem;
	}
	
	// Set Int.
	public void setStatusFaction(int status) {
		statusFaction = status;
	}
	
	// Set boolean.
	public void setTotemCreateStatus(boolean status) {
		totemCreateStatus = status;
	}
	
	public void setTotemSpawnStatus(boolean status) {
		totemSpawnStatus = status;
	}
	
	public void setFeatherBoardStatus(boolean status) {
		featherBoardStatus = status;
	}
	
	public void setMVdWPlaceholderAPIStatus(boolean status) {
		mvdwPlaceholderAPIStatus = status;
	}
	
	public void setPlaceholderAPIStatus(boolean status) {
		placeholderAPIStatus = status;
	}
	
	// Set Location.
	public void setCurrentTotemLocation(Location location) {
		currentTotemLocation = location;
	}
	
	// Set String.
	public void setCronPredictor(String cronString) {
		cronPredictor = cronString;
	}
	
	public void setCurrentTotemName(String totemName) {
		currentTotemName = totemName;
	}
	
	public void setCurrentFactionName(String factionName) {
		currentFactionName = factionName;
	}
	
	// Set Integer.
	public void setTimerMinutes(Integer paramInt) {
		timerMinutes = paramInt;
	}
	
	public void setTimerSecondes(Integer paramInt) {
		timerSecondes = paramInt;
	}
	
	// Test.
	public ScoreboardInterfaces getInstanceScoreboards() {
		try {
			return scoreboardManager.getClass().newInstance();
		} catch (Exception e) {}
		return null;
	}
 	
	// Get Prefix Messages.
	public String getPrefix() {
		try {
			if(fileConfigConfig.getBoolean("TotemConfigs.settings.prefix.enable")) {
				return fileConfigConfig.getString("TotemConfigs.settings.prefix.prefix").replace("&", "§") + "§r ";
			}
		} catch (Exception e) {return "§c[Error] Prefix message.";}
		return "";
	}
	
	// Get Messages in file messages.yml.
	public List<String> getMessagesStaff(String leak, Object obj) {
		try {
			if(getPlaceholderAPIStatus()) {
				return MPlaceholderAPI.usePlaceholderAPI(fileConfigMessages.getStringList("TotemMessages.staff." + leak), obj);			
			}
			return fileConfigMessages.getStringList("TotemMessages.staff." + leak);
		} catch (Exception e) {}
		return Arrays.asList("§c[Error] Message staff. (" + leak + ")");
	}
	
	public List<String> getMessagesPublic(String leak, Object obj) {
		try {
			if(getPlaceholderAPIStatus()) {
				return MPlaceholderAPI.usePlaceholderAPI(fileConfigMessages.getStringList("TotemMessages.public." + leak), obj);
			}
			return fileConfigMessages.getStringList("TotemMessages.public." + leak);
		} catch (Exception e) {}
		return Arrays.asList("§c[Error] Message public. (" + leak + ")");
	}
	
	public List<String> getMessagesScorboard(String leak, Object obj) {
		
		try {
			if(getPlaceholderAPIStatus()) {
				return MPlaceholderAPI.usePlaceholderAPI(fileConfigMessages.getStringList("TotemMessages.scoreboard." + leak), obj);			
			}
			return fileConfigMessages.getStringList("TotemMessages.scoreboard." + leak);
		} catch (Exception e) {}
		return Arrays.asList("§c[Error] (" + leak + ")");
	}
	
	public String getMessagesTimerStatus(String leak) {
		return fileConfigMessages.getString("TotemMessages.scoreboard." + leak);
	}
	
	// Get Messages Titles.
	public String getMessagesTitles(String leak) {
		return fileConfigMessages.getString("TotemMessages.title." + leak);
	}
	
	// Get Item for break block.
	@SuppressWarnings("deprecation")
	public List<String> getItemBreakTotem() {
		List<String> list1 = fileConfigConfig.getStringList("TotemConfigs.settings.itemBreakTotem");
		List<String> list2 = new ArrayList<>(list1);
		for(String item : list1) {
			if(!isInt(item)) continue;
			list2.remove(item);
			list2.add(Material.getMaterial(Integer.parseInt(item)).toString());
		}
		return list2;
	}
	
	// Get type of bloc for totem.
	@SuppressWarnings("deprecation")
	public String getTypeTotemBlock() {
		String item = fileConfigConfig.getString("TotemConfigs.settings.typeTotemBlock");
		if(isInt(item)) item = Material.getMaterial(Integer.parseInt(item)).toString();
		return item;
	}
	
	// Get Permission in file config.yml.
	public String getPermission(String leak) {
		return fileConfigConfig.getString("TotemConfigs.permissions." + leak);
	}
	
	// Get totem name by int.
	public String getTotemNameByInt(Integer numero) {
		return fileConfigMessages.getString("TotemMessages.totemName.totem" + numero + ".name");
	}
	
	// Get totem world by int.
	public String getTotemWorldByInt(Integer numero) {
		return fileConfigLocations.getString("TotemLocations.totem" + numero + ".world");
	}
	
	// Get mode of respawn.
	public String getModeRespawn() {
		return fileConfigConfig.getString("TotemConfigs.settings.autoRespawn.modRespawn");
	}
	
	// Get Enable or Disable settings in file config.yml.
	public boolean getAutoRespawnStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.autoRespawn.enable");
	}
	
	public boolean getRewardsPlayerBreakLastBlockStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.rewards.player.breakLastBlock.enable");
	}
	
	public boolean getRewardsPlayerBreakMostBlockStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.rewards.player.breakMostBlock.enable");
	}
	
	public boolean getRewardsFactionStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.rewards.faction.enable");
	}
	
	public boolean getFireworksStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.fireworks.enable");
	}
	
	public boolean getParticleStatus() {
		if(versionServerSplit > 7 && versionServerSplit < 13) {
			return fileConfigConfig.getBoolean("TotemConfigs.settings.particles.enable");
		} else {
			return false;
		}
	}
	
	public boolean getUpdateStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.update.enable");
	}
	
	public boolean getSoundStatus() {
		if(versionServerSplit < 9) {
			return fileConfigConfig.getBoolean("TotemConfigs.settings.sounds.enable");
		} else {
			return false;
		}
	}
	
	public boolean getScoreboardTotemFactionsStatus() {
		if(versionServerSplit > 7) {
			return fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.totemFactions.enable");
		} else {
			return false;
		}
	}
    
	public boolean getScoreboardFeatherBoardStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.featherBoards.enable");
	}
	
	public String getScoreboardFeatherBoardBoard() {
		return fileConfigConfig.getString("TotemConfigs.settings.scoreboard.featherBoards.name");
	}
	
	public boolean getScoreboardStatusLevel(String level) {
		try {
			if(!getScoreboardTotemFactionsStatus()) return false;
			else if(level == "pre") {System.out.println(fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.preTotem"));return fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.preTotem");}
			else if(level == "on") {System.out.println(fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.onTotem"));return fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.onTotem");}
			else if(level == "end") {System.out.println(fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.endTotem"));return fileConfigConfig.getBoolean("TotemConfigs.settings.scoreboard.endTotem");}
			else return false;
		} catch (Exception e) {e.printStackTrace();}
		return false;
	}
	
	public boolean getTitlesStatus() {
		if(versionServerSplit > 7) {
			return fileConfigConfig.getBoolean("TotemConfigs.settings.titles.enable");
		} else {
			return false;
		}
	}
	
	public boolean getPubStatus() {
		return fileConfigConfig.getBoolean("TotemConfigs.settings.pubPlugin.enable");
	}
	
	// Verif if player has permission to execute commands.
	public boolean hasPermCommand(Player player, String action) {
		if(player.hasPermission(getPermission("totemAll"))) {
			return true;
		} else if(action == "totemDefault" && (getPermission("totemDefault") == null || getPermission("totemDefault").isEmpty() ||player.hasPermission(getPermission("totemDefault")))) {
			return true;
		} else if(action == "totemSetSpawn" && player.hasPermission(getPermission("totemSetSpawn"))) {
			return true;
		} else if(action == "totemSpawn" && player.hasPermission(getPermission("totemSpawn"))) {
			return true;
		} else if(action == "totemStop" && player.hasPermission(getPermission("totemStop"))) {
			return true;
		} else if(action == "totemReset" && player.hasPermission(getPermission("totemReset"))) {
			return true;
		} else if(action == "totemReload" && player.hasPermission(getPermission("totemReload"))) {
			return true;
		} else if(action == "totemUpdate" && player.hasPermission(getPermission("totemUpdate"))) {
			return true;
		} else if(action == "totemPoints" && player.hasPermission(getPermission("totemPoints"))) {
			return true;
		}
		return false;
	}
	
	// Error System
	public void logConsole(Level level, String error) {
		getLogger().log(level, error);
	}
	
    public boolean rightItem(Player player) {
    	for(String material : getItemBreakTotem()) {
    		if(material.equals("*")) return true;
    		if(player.getItemInHand().getType().equals(Material.getMaterial(material))) return true;
		}
		return false;
    }
    
    public boolean isInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {return false;}
	}
	
	public UDataPack<Faction, String> getFaction(FPlayer player, boolean wilderness) {
		 try {
			 	Method method = null;
		    	if(!wilderness){
		    		method = FPlayer.class.getDeclaredMethod("hasFaction", new Class[0]);
			    	boolean has = (boolean) method.invoke(player);
			    	if(!has) return null;
		    	}
		    	method = FPlayer.class.getDeclaredMethod("getFaction", new Class[0]);
		    	Faction faction = (Faction)method.invoke(player, new Object[0]);
		    	if (faction == null) return null; 
		    	method = Faction.class.getDeclaredMethod("getTag", new Class[0]);
		    	return new UDataPack<Faction, String>(faction,(String)method.invoke(faction));
		    } catch (Exception e) {}
		 return null;
	}
	
	public UDataPack<Faction, String> getFaction(Player player, boolean wilderness){
		if(getStatusFaction() == 4){
			 try {
			    	Field field = FPlayers.class.getDeclaredField("i");
			    	FPlayers fplayers = (FPlayers)field.get(null);
			    	Method method = PlayerEntityCollection.class.getDeclaredMethod("get", new Class[] { Player.class });
			    	FPlayer fplayer = (FPlayer)method.invoke(fplayers, new Object[] { player });
			    	if(!wilderness){
			    		method = FPlayer.class.getDeclaredMethod("hasFaction", new Class[0]);
				    	boolean has = (boolean) method.invoke(fplayer);
				    	if(!has) return null;
			    	}
			    	method = FPlayer.class.getDeclaredMethod("getFaction", new Class[0]);
			    	Faction faction = (Faction)method.invoke(fplayer, new Object[0]);
			    	if (faction == null) return null; 
			    	method = Faction.class.getDeclaredMethod("getTag", new Class[0]);
			    	return new UDataPack<Faction, String>(faction,(String)method.invoke(faction));
			    } catch (Exception e) {}
			 return null;
		}
	    FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
	    if(!wilderness){
	    	if(!fplayer.hasFaction()) return null;
	    }
	    Faction faction = fplayer.getFaction();
	    if (faction == null)return null;
	    return new UDataPack<Faction, String>(faction, faction.getTag());
	}
	
}
