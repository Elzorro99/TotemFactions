package net.elzorro99.totemfactions.utils;

import org.bukkit.command.CommandSender;

import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.utils.UTopData.FactionsPoint;

public class UTopFactions {

	private Main main = Main.getInstance();
	
	public Integer getWinsFactions(String factionname) {
		if(main.fileConfigStats.contains("TotemStats." + factionname)) {
			Integer access = main.fileConfigStats.getInt("TotemStats." + factionname + ".wins");
			return access;
		}
		return 0;
	}
	
	public Integer getBlocksBreak(String factionname) {
		if(main.fileConfigStats.contains("TotemStats." + factionname)) {
			Integer access = main.fileConfigStats.getInt("TotemStats." + factionname + ".blocks");
			return access;
		}
		return 0;
	}
	
	public void addPointsToFaction(String factionName, int nmPoints, String type) {
		if(type.equalsIgnoreCase("win")) {
			String access = "TotemStats." + factionName + ".";
			Integer wins = main.fileConfigStats.getInt(access + "wins");
			main.fileConfigStats.set(access + "wins", wins + nmPoints);
		} else if(type.equalsIgnoreCase("block")) {
			String access = "TotemStats." + factionName + ".";
			Integer blocks = main.fileConfigStats.getInt(access + "blocks");
			main.fileConfigStats.set(access + "blocks", blocks + nmPoints);
		}
		main.getManagerFiles().saveResource(main.fileStats);
		main.getUtilsTop().initAll(main.fileConfigStats);
	}
	
	public void removePointsToFaction(String factionName, int nmPoints, String type) {
		if(type.equalsIgnoreCase("win")) {
			String access = "TotemStats." + factionName + ".";
			Integer wins = main.fileConfigStats.getInt(access + "wins");
			if(wins - nmPoints < 0) main.fileConfigStats.set(access + "wins", 0);
			else main.fileConfigStats.set(access + "wins", wins - nmPoints);
		} else if(type.equalsIgnoreCase("block")) {
			String access = "TotemStats." + factionName + ".";
			Integer blocks = main.fileConfigStats.getInt(access + "blocks");
			if(blocks - nmPoints < 0) main.fileConfigStats.set(access + "blocks", 0);
			else main.fileConfigStats.set(access + "blocks", blocks - nmPoints);
		}
		main.getManagerFiles().saveResource(main.fileStats);
		main.getUtilsTop().initAll(main.fileConfigStats);
	}
	
	public void updateFactionStats(String factionname, Integer numero, boolean win) {
		if(main.fileConfigStats.contains("TotemStats." + factionname)) {
			String access = "TotemStats." + factionname + ".";
			Integer blocks = main.fileConfigStats.getInt(access + "blocks");
			Integer wins = main.fileConfigStats.getInt(access + "wins");
			main.fileConfigStats.set(access + "blocks", blocks + numero);
			if(win) main.fileConfigStats.set(access + "wins", wins + 1);
			main.getUtilsTop().updateFaction(main.getUtilsTop().getFactions(factionname, (wins+1)));
		} else {
			String access = "TotemStats." + factionname + ".";
			main.fileConfigStats.set(access + "blocks", numero);
			int wins = ((win) ? 1 : 0);
			main.fileConfigStats.set(access + "wins", wins);
			main.getUtilsTop().updateFaction(main.getUtilsTop().getFactions(factionname, wins));
		}
		main.getManagerFiles().saveResource(main.fileStats);
		main.getUtilsTop().initAll(main.fileConfigStats);
	}
	
	public void updateFactionName(String factionOldName, String factionNewName) {
		if(main.fileConfigStats.contains("TotemStats." + factionOldName)) {
			String access = "TotemStats." + factionOldName + ".";
			Integer blocks = main.fileConfigStats.getInt(access + "blocks");
			Integer wins = main.fileConfigStats.getInt(access + "wins");
			main.fileConfigStats.set("TotemStats." + factionOldName, null);
			access = "TotemStats." + factionNewName + ".";
			main.fileConfigStats.set(access + "blocks", blocks);
			main.fileConfigStats.set(access + "wins", wins);
		}
		main.getManagerFiles().saveResource(main.fileStats);
	}
	
	public void updateFactionDisband(String factionName) {
		if(main.fileConfigStats.contains("TotemStats." + factionName)) {
			main.fileConfigStats.set("TotemStats." + factionName, null);
		}
		main.getManagerFiles().saveResource(main.fileStats);
		main.getUtilsTop().initAll(main.fileConfigStats);
	}
	
	public void sendClassementFaction(CommandSender sender, Integer page) {
		try {
			if(main.getUtilsTop().size() <= 0) {
				for(String messages : main.getMessagesPublic("noRankFactions", sender)) {
					sender.sendMessage(main.getPrefix() + messages.replace("&", "§"));
				}
				return;
			}
			double a = (main.getUtilsTop().size()/10.0);
			int b = (int)a;
			if((a-b) > 0.0) b++;
			if(page > (b-1)) page = (b-1);
			for(String messages : main.getMessagesPublic("rankFactionsStyle", sender)) {
				sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
															  .replace("<page>", ""+(page+1))
															  .replace("<totalPage>", ""+(b)));
			}
			int top = 10*page;
			for(FactionsPoint factionsPoint : main.getUtilsTop().get(page)) {
				if(factionsPoint == null) continue;
				top++;
				int block = 0;
				try {
					block = getBlocksBreak(factionsPoint.getKey());
				} catch (Exception e) {}
				for(String messages : main.getMessagesPublic("rankFactionsList", sender)) {
					sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																  .replace("<num>", top + "")
																  .replace("<faction>", factionsPoint.getKey())
																  .replace("<win>", factionsPoint.getValue() + "")
																  .replace("<blocks>", block + ""));
				}
			}
			for(String messages : main.getMessagesPublic("rankFactionsStyle", sender)) {
				sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
															  .replace("<page>", ""+(page+1))
															  .replace("<totalPage>", ""+(b)));
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
