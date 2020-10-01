package net.elzorro99.totemfactions.utils;

import java.text.DecimalFormat;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import net.elzorro99.totemfactions.Main;

public class UScoreboardManager {

	private Main main = Main.getInstance();
	private String status;
	
	public void removeScorboards(Player player) {
		try {
			if(main.getScoreboardsPlayer().containsKey(player)) {
				main.getScoreboardsPlayer().get(player).destroy();
				main.getScoreboardsPlayer().remove(player);
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void updateScorboards(Player player, String factionName, int blockEvent, String status) {
		try {
			removeScorboards(player);
			if(status != null) this.status = status;
			ScoreboardInterfaces sc = main.scoreboardManager.getClass().newInstance();
			sc.init(player, main.fileConfigMessages.getString("TotemMessages.scoreboard.scoreboardName").replace("&", "§").replace("<totemName>", main.getCurrentTotemName()));
			sc.create();
			int i = 0;
			if(status == "pre") {
				DecimalFormat format = new DecimalFormat("00");
				String formatMinutes = format.format(main.getTimerMinutes());
				String formatSeconds = format.format(main.getTimerSecondes());
				for(String messages : main.getMessagesScorboard("scoreboardLinePreEvent", player)) {
					sc.setLine(i, messages.replace("&", "§")
										  .replace("<x>", main.getCurrentTotemLocation().getBlockX() + "")
										  .replace("<y>", main.getCurrentTotemLocation().getBlockY() + "")
										  .replace("<z>", main.getCurrentTotemLocation().getBlockZ() + "")
										  .replace("<world>", main.getCurrentTotemLocation().getWorld().getName())
										  .replace("<minutes>", formatMinutes)
										  .replace("<seconds>", formatSeconds));
					i++;
				}
			} else if(status == "in") {
				for(String messages : main.getMessagesScorboard("scoreboardLineInEvent", player)) {
					sc.setLine(i, messages.replace("&", "§")
										  .replace("<x>", main.getCurrentTotemLocation().getBlockX() + "")
										  .replace("<y>", main.getCurrentTotemLocation().getBlockY() + "")
										  .replace("<z>", main.getCurrentTotemLocation().getBlockZ() + "")
										  .replace("<world>", main.getCurrentTotemLocation().getWorld().getName())
										  .replace("<faction>", main.fileConfigMessages.getString("TotemMessages.scoreboard.defaultFaction").replace("&", "§"))
										  .replace("<block>", 5 + ""));
					i++;
				}
			} else if(status == "post") {
				for(String messages : main.getMessagesScorboard("scoreboardLinePostEvent", player)) {
					sc.setLine(i, messages.replace("&", "§")
										  .replace("<faction>", main.getCurrentFactionName())
										  .replace("<wins>", main.getUtilsRankFactions().getWinsFactions(factionName) + "")
										  .replace("<blockEvent>", blockEvent + "")
										  .replace("<blockAll>", main.getUtilsRankFactions().getBlocksBreak(factionName) + ""));
					i++;
				}
			}
			main.getScoreboardsPlayer().put(player, sc);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void setupOnJoin(Player player) {
		updateScorboards(player, null, 5, getScStatus());
		if(status != "in") return;
		updateFaction(main.getCurrentFactionName());
		updateBlock(main.getLocationBlocksTotem().size());
	}
	
	public String getScStatus() {
		return status;
	}
	
	public void updateFaction(String factionName) {
		for(Entry<Player, ScoreboardInterfaces> map : main.getScoreboardsPlayer().entrySet()) {
			if(map.getKey() == null) {return;}
			if(!map.getKey().isOnline()) {return;}
			int i = 0;
			for(String messages : main.getMessagesScorboard("scoreboardLineInEvent", map.getKey())) {
				if(messages.contains("<faction>")) {
					if(factionName == null) {
						map.getValue().setLine(i, messages.replace("&", "§").replace("<faction>", main.fileConfigMessages.getString("TotemMessages.scoreboard.defaultFaction")));
					} else {
						map.getValue().setLine(i, messages.replace("&", "§").replace("<faction>", factionName));
					}
				}
				i++;
			}
		}
	}
	
	public void updateBlock(int numero) {
		for(Entry<Player, ScoreboardInterfaces> map : main.getScoreboardsPlayer().entrySet()) {
			if(map.getKey() == null) {return;}
			if(!map.getKey().isOnline()) {return;}
			int i = 0;
			for(String messages : main.getMessagesScorboard("scoreboardLineInEvent", map.getKey())) {
				if(messages.contains("<block>")) {
					map.getValue().setLine(i, messages.replace("&", "§").replace("<block>", numero + ""));
				}
				i++;
			}
		}
	}
	
	public void updateTimer(int minutes, int seconds) {
		DecimalFormat format = new DecimalFormat("00");
		String formatMinutes = format.format(minutes);
		String formatSeconds = format.format(seconds);
		for(Entry<Player, ScoreboardInterfaces> map : main.getScoreboardsPlayer().entrySet()) {
			if(map.getKey() == null) {return;}
			if(!map.getKey().isOnline()) {return;}
			int i = 0;
			for(String messages : main.getMessagesScorboard("scoreboardLinePreEvent", map.getKey())) {
				if(messages.contains("<minutes>") || messages.contains("<secondes>")) {
					map.getValue().setLine(i, messages.replace("&", "§").replace("<minutes>", formatMinutes + "")
																		.replace("<seconds>", formatSeconds + ""));
				}
				i++;
			}
		}
	}
	
	public static interface ScoreboardInterfaces {
		
		void init(Player player, String objective);
		
		void create();
		void destroy();
		void removeLine(int line);
		void setLine(int line, String text);
		void setObjectiveName(String objective);
		
		String getLine(int line);
		
		Object getTeam(int i);
	}
}