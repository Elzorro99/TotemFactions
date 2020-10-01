package net.elzorro99.totemfactions.managers;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import net.elzorro99.totemfactions.Main;

public class MMVdWPlaceholderAPI {

	private static Main main = Main.getInstance();
	
	public static void initMVdWPlaceholderAPI() {
		// Return plugin version
		PlaceholderAPI.registerPlaceholder(main, "totem_plugin_version", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return main.getVersionPlugin();
			}
		});
		// Return server version
		PlaceholderAPI.registerPlaceholder(main, "totem_server_version", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return main.getVersionServer();
			}
		});
		// Return current faction name
		PlaceholderAPI.registerPlaceholder(main, "totem_faction_name", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				if(main.getCurrentTotemName() == null) return main.fileConfigMessages.getString("TotemMessages.scoreboard.defaultFaction").replace("&", "§");
				return main.getCurrentFactionName();
			}
		});
		// Return number of blocks remaining
		PlaceholderAPI.registerPlaceholder(main, "totem_remaining_block", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "" + main.getLocationBlocksTotem().size();
			}
		});
		// Return number of max blocks
		PlaceholderAPI.registerPlaceholder(main, "totem_max_block", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "5";
			}
		});
		// Return current totem name
		PlaceholderAPI.registerPlaceholder(main, "totem_name", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return main.getCurrentTotemName();
			}
		});
		// Return current totem location x
		PlaceholderAPI.registerPlaceholder(main, "totem_location_x", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "" + main.getCurrentTotemLocation().getBlockX();
			}
		});
		// Return current totem location y
		PlaceholderAPI.registerPlaceholder(main, "totem_location_y", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "" + main.getCurrentTotemLocation().getBlockY();
			}
		});
		// Return current totem location z
		PlaceholderAPI.registerPlaceholder(main, "totem_location_z", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "" + main.getCurrentTotemLocation().getBlockZ();
			}
		});
		// Return current totem location world name
		PlaceholderAPI.registerPlaceholder(main, "totem_location_world", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "" + main.getCurrentTotemLocation().getWorld().getName();
			}
		});
		// Return minutes
		PlaceholderAPI.registerPlaceholder(main, "totem_timer_minute", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "" + main.getTimerMinutes();
			}
		});
		// Return secondes
		PlaceholderAPI.registerPlaceholder(main, "totem_timer_seconde", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent arg0) {
				return "" + main.getTimerSecondes();
			}
		});
	}
	
}
