package net.elzorro99.totemfactions.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;

public class RTimerTotem extends BukkitRunnable {

	private Main main = Main.getInstance();
	private boolean setup = false;
	
	@Override
	public void run() {
		if(!setup) {main.setTimerMinutes(0); main.setTimerSecondes(0); setup = true;}
		if(main.getTimerSecondes() > 59) {
			main.setTimerMinutes(main.getTimerMinutes() + 1);
			main.setTimerSecondes(0);
		}
		if(main.getTimerMinutes() > 89) {
			if(main.getTitlesStatus()) {
				for(Player players : Bukkit.getOnlinePlayers()) {
					main.getUtilsTitles().sendTitle(players, 5, 30, 5, main.getMessagesTitles("totemStop").replace("&", "§").replace("<totemName>", main.getCurrentTotemName()), "");
				}
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
			for(String messages : main.getMessagesStaff("totemAutoStop", null)) {
				Bukkit.broadcastMessage(main.getPrefix() + messages.replace("&", "§"));
			}
			main.getUtilsPhysicBlocks().unBuildTotem(main.getCurrentTotemLocation());
			main.setTotemCreateStatus(false);
		}
		if(!main.getTotemCreateStatus() || !main.getTotemSpawnStatus()) {cancel(); return;}
		main.setTimerSecondes(main.getTimerSecondes() + 1);
	}

}
