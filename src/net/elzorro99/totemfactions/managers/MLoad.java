package net.elzorro99.totemfactions.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.packets.PInjector;
import net.elzorro99.totemfactions.listeners.packets.v1_10_R1;
import net.elzorro99.totemfactions.listeners.packets.v1_11_R1;
import net.elzorro99.totemfactions.listeners.packets.v1_12_R1;
import net.elzorro99.totemfactions.listeners.packets.v1_13_R2;
import net.elzorro99.totemfactions.listeners.packets.v1_14_R1;
import net.elzorro99.totemfactions.listeners.packets.v1_15_R1;
import net.elzorro99.totemfactions.listeners.packets.v1_7_R4;
import net.elzorro99.totemfactions.listeners.packets.v1_8_R3;
import net.elzorro99.totemfactions.listeners.packets.v1_9_R2;
import net.elzorro99.totemfactions.runnable.RPubPlugin;

public class MLoad {

	private Main main = Main.getInstance();
	
	public List<String> autoriseVersions = new ArrayList<>();
	
	public void loadPlugin() {
		// Setup startup.
		long timer_start = System.currentTimeMillis();
		main.logConsole(Level.INFO, "=== START LOADING ===");
		main.logConsole(Level.INFO, "Check Server version...");
		main.logConsole(Level.INFO, "Check Faction plugin...");
		main.logConsole(Level.INFO, "Check FeatherBoard plugin...");
		main.logConsole(Level.INFO, "Check PlaceholderAPI plugin...");
		main.logConsole(Level.INFO, "Check TotemFactions plugin update...");
		main.logConsole(Level.INFO,"---");
		// Verif serveur version.
		try {
			autoriseVersions.add("v1_7_R4");
			autoriseVersions.add("v1_8_R3");
			autoriseVersions.add("v1_9_R2");
			autoriseVersions.add("v1_10_R1");
			autoriseVersions.add("v1_11_R1");
			autoriseVersions.add("v1_12_R1");
			autoriseVersions.add("v1_13_R2");
			autoriseVersions.add("v1_14_R1");
			autoriseVersions.add("v1_15_R1");
			if(autoriseVersions.contains(main.getVersionServer())){
				main.logConsole(Level.INFO, "Server version compatible. OK");
			} else {
				main.logConsole(Level.SEVERE, "Server version not compatible with the plugin TotemFactions! (1.7.X - 1.15.X Obligatory)");
				main.setStatusFaction(0);
			}
		} catch (Exception e) {main.logConsole(Level.SEVERE, "Erreur fatal: CHECK_VERSION_SERVEUR"); main.setStatusFaction(0); e.printStackTrace();}
		// Vérif faction plugin.
		try {
			if(Class.forName("com.massivecraft.factions.entity.MPlayer") != null) {
				main.setStatusFaction(1);
				main.logConsole(Level.INFO, "Faction(MassiveCraft) plugin compatible. OK");
				switch(main.getVersionServer()) {
					case "v1_7_R4": main.packetInjector =  new v1_7_R4(); break;
					case "v1_8_R3": main.packetInjector =  new v1_8_R3(); break;
					case "v1_9_R2": main.packetInjector =  new v1_9_R2(); break;
					case "v1_10_R1": main.packetInjector =  new v1_10_R1(); break;
					case "v1_11_R1": main.packetInjector =  new v1_11_R1(); break;
					case "v1_12_R1": main.packetInjector =  new v1_12_R1(); break;
					case "v1_13_R2": main.packetInjector =  new v1_13_R2(); break;	
					case "v1_14_R1": main.packetInjector =  new v1_14_R1(); break;
					case "v1_15_R1": main.packetInjector =  new v1_15_R1(); break;
					default: break;
				}
			}
		} catch (Exception e1) {
			try {
				if(Class.forName("net.redstoneore.legacyfactions.entity.FPlayer") != null) {
					main.setStatusFaction(2);
					main.logConsole(Level.INFO, "Faction(Legacy) plugin compatible. OK");
				}
			} catch (Exception e2) {
				try {
					if(Class.forName("com.massivecraft.factions.FPlayer") != null) {
						try {
							if(Class.forName("de.erethon.factionsone.FactionsOneAPI") != null) {
								main.setStatusFaction(4);
								main.logConsole(Level.INFO, "Faction(One) plugin compatible. OK");
							}
						} catch (Exception e) {
							main.setStatusFaction(3);
							main.logConsole(Level.INFO, "Faction(UUID) plugin compatible. OK");
						}
					}
				} catch (Exception e3) {
					main.setStatusFaction(0);
					main.logConsole(Level.SEVERE, "No faction plugin is installed on your server or it is no longer compabible!");
				}
			}
		}
		// Vérif featherboard plugin.
		try {
			if(Class.forName("be.maximvdw.featherboard.api.FeatherBoardAPI") != null) {
				main.logConsole(Level.INFO, "FeatherBoard plugin detected. OK");
				main.setFeatherBoardStatus(true);
			}
		} catch (Exception e) {main.logConsole(Level.INFO, "FeatherBoard plugin not detected. OK");}
		// Vérif mvdwplaceholderapi plugin.
		try {
			if(Class.forName("be.maximvdw.placeholderapi.PlaceholderAPI") != null) {
				main.logConsole(Level.INFO, "MVdWPlaceholderAPI plugin detected. OK");
				main.setMVdWPlaceholderAPIStatus(true);
			}
		} catch (Exception e) {main.logConsole(Level.INFO, "MVdWPlaceholderAPI plugin not detected. OK");}
		// Vérif placeholderapi plugin.
		try {
			if(Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin") != null) {
				main.logConsole(Level.INFO, "PlaceholderAPI plugin detected. OK");
				main.setPlaceholderAPIStatus(true);
			} else {
				main.logConsole(Level.INFO, "PlaceholderAPI plugin not detected. OK");
			}
		} catch (Exception e) {main.logConsole(Level.INFO, "PlaceholderAPI plugin not detected. OK");}
		try {
			if(main.getUtilsCheckUpdate().checkForUpdates()) {
				main.logConsole(Level.WARNING, "An update of the TotemFactions plugin is available! (Your version: " + main.getUtilsCheckUpdate().getThisVersion() + " -> New version: " + main.getUtilsCheckUpdate().getNewVersion() + ")");
			} else {
				main.logConsole(Level.INFO, "The TotemFactions plugin is up to date. OK");
			}
		} catch (Exception e) {main.logConsole(Level.WARNING, "Erreur: CHECK_UPDATE_PLUGIN"); e.printStackTrace();}
		// Fin du setup startup.
		if(main.getStatusFaction() == 0) msgError(); else msgTrue();
		long timer_end = System.currentTimeMillis();
		main.logConsole(Level.INFO, "=== LOADING TERMINATED (Delai: " + (timer_end-timer_start) + "ms) ===");
		// Setup plugin.
		main.getManagerCommands().initCommands();
		main.getManagerFiles().initFiles();
		main.getManagerListeners().initEvents();
		main.getManagerScorebords().initScoreboards();
		if(main.getStatusFaction() != 0) main.getManagerCrons().initCrons();
		if(main.getPlaceholderAPIStatus()) MPlaceholderAPI.initPlaceholderAPI();
		if(main.getMVdWPlaceholderAPIStatus()) MMVdWPlaceholderAPI.initMVdWPlaceholderAPI();
		if(main.getPubStatus()) {
			RPubPlugin start = new RPubPlugin();
			start.runTaskTimer(main, 600, 12000);
		}
		main.setCurrentFactionName(main.fileConfigMessages.getString("TotemMessages.scoreboard.defaultFaction").replace("&", "§"));
		if(main.getStatusFaction() == 1) {
			for(Player players : Bukkit.getOnlinePlayers()) {
				PInjector injector = main.getPacketInjector();
				if(injector != null) injector.inject(players);
			}
		}
	}
	
	public void unLoadPlugin() {
		if(main.getStatusFaction() == 0) {return;}
		if(main.getCronScheduler().isStarted()) {
			main.getCronScheduler().stop();
		}
		for(int i = 1; i < 5; i++) {
			try {
				main.getUtilsPhysicBlocks().unBuildTotem(main.getUtilsLocationsTotems().getLocationsTotems(i));
			} catch (Exception e) {}
		}
		if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
			if(main.getScoreboardFeatherBoardStatus()) {
				if(main.getFeatherBoardStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						if (Bukkit.isPrimaryThread()) {
						    FeatherBoardAPI.removeScoreboardOverride(players, main.getScoreboardFeatherBoardBoard());
							FeatherBoardAPI.resetDefaultScoreboard(players);
						} else {
							Bukkit.getScheduler().runTaskLater(main, () -> {
								FeatherBoardAPI.removeScoreboardOverride(players, main.getScoreboardFeatherBoardBoard());
								FeatherBoardAPI.resetDefaultScoreboard(players);
							}, 1L);
						}
					}
				} else if(main.getScoreboardTotemFactionsStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						main.getUtilsScorebordsManager().removeScorboards(players);
					}
				}
			} else if(main.getScoreboardTotemFactionsStatus()) {
				for(Player players : Bukkit.getOnlinePlayers()) {
					main.getUtilsScorebordsManager().removeScorboards(players);
				}
			}
		}
		main.setCurrentFactionName("None");
		main.setCurrentTotemLocation(null);
		main.setCurrentTotemName("None");
		main.setTotemCreateStatus(false);
		main.setTotemSpawnStatus(false);
		msgFalse();
	}
	
	public void msgTrue() {
		main.logConsole(Level.INFO,"---");
		main.logConsole(Level.INFO,"Plugin TotemFactions");
		main.logConsole(Level.INFO,"By Elzorro99");
		main.logConsole(Level.INFO,"Version: " + main.getVersionPlugin());
		main.logConsole(Level.INFO,"Minecraft: " + main.getVersionServer());
		main.logConsole(Level.INFO,"Enable: true");
	}
	

	public void msgError() {
		main.logConsole(Level.INFO,"---");
		main.logConsole(Level.INFO,"Plugin TotemFactions");
		main.logConsole(Level.INFO,"By Elzorro99");
		main.logConsole(Level.INFO,"Version: " + main.getVersionPlugin());
		main.logConsole(Level.INFO,"Minecraft: " + main.getVersionServer());
		main.logConsole(Level.INFO,"Enable: ERREUR");
	}
	
	public void msgFalse() {
		main.logConsole(Level.INFO,"---");
		main.logConsole(Level.INFO,"Plugin TotemFactions");
		main.logConsole(Level.INFO,"By Elzorro99");
		main.logConsole(Level.INFO,"Version: " + main.getVersionPlugin());
		main.logConsole(Level.INFO,"Minecraft: " + main.getVersionServer());
		main.logConsole(Level.INFO,"Enable: false");
		main.logConsole(Level.INFO, "---");
	}
}
