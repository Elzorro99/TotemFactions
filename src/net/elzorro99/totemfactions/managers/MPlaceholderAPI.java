package net.elzorro99.totemfactions.managers;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import net.elzorro99.totemfactions.Main;

public class MPlaceholderAPI {

	private static Main main = Main.getInstance();
	
	@SuppressWarnings("deprecation")
	public static void initPlaceholderAPI() {
		PlaceholderAPI.registerPlaceholderHook("totem", new PlaceholderHook() {
			@Override
			public String onRequest(OfflinePlayer player, String args) {
				if(player != null && player.isOnline()) {
					return onPlaceholderRequest(player.getPlayer(), args);
				}
				return onPlaceholderRequest(null, args);
			}
			@Override
			public String onPlaceholderRequest(Player player, String args) {
				// Return plugin version
				if(args.equalsIgnoreCase("plugin_version")) {
					return main.getVersionPlugin();
				}
				// Return server version
				if(args.equalsIgnoreCase("server_version")) {
					return main.getVersionServer();
				}
				// Return current faction name
				if(args.equalsIgnoreCase("faction_name")) {
					if(main.getCurrentTotemName() == null) return main.fileConfigMessages.getString("TotemMessages.scoreboard.defaultFaction").replace("&", "§");
					return main.getCurrentFactionName();
				}
				// Return number of blocks remaining
				if(args.equalsIgnoreCase("remaining_block")) {
					return "" + main.getLocationBlocksTotem().size();
				}
				// Return number of max blocks
				if(args.equalsIgnoreCase("max_block")) {
					return "5";
				}
				// Return current totem name
				if(args.equalsIgnoreCase("name")) {
					return main.getCurrentTotemName();
				}
				// Return current totem location x
				if(args.equalsIgnoreCase("location_x")) {
					return "" + main.getCurrentTotemLocation().getBlockX();
				}
				// Return current totem location y
				if(args.equalsIgnoreCase("location_y")) {
					return "" + main.getCurrentTotemLocation().getBlockY();
				}
				// Return current totem location z
				if(args.equalsIgnoreCase("location_z")) {
					return "" + main.getCurrentTotemLocation().getBlockZ();
				}
				// Return current totem location world name
				if(args.equalsIgnoreCase("location_world")) {
					return "" + main.getCurrentTotemLocation().getWorld().getName();
				}
				// Return minutes
				if(args.equalsIgnoreCase("timer_minute")) {
					return "" + main.getTimerMinutes();
				}
				// Return secondes
				if(args.equalsIgnoreCase("timer_seconde")) {
					return "" + main.getTimerSecondes();
				}
				return null;
			}
		});
	}
	
	public static List<String> usePlaceholderAPI(List<String> args, Object obj) {
		if(obj == null) return PlaceholderAPI.setPlaceholders(null, args);
		if(obj instanceof OfflinePlayer) return PlaceholderAPI.setPlaceholders((OfflinePlayer)obj, args);
		if(obj instanceof Player) return PlaceholderAPI.setPlaceholders((Player)obj, args);
		return PlaceholderAPI.setPlaceholders(null, args);
	}
	
}
