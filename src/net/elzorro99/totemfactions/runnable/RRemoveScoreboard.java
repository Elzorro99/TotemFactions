package net.elzorro99.totemfactions.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;

public class RRemoveScoreboard extends BukkitRunnable {

	private Main main = Main.getInstance();
	
	@Override
	public void run() {
		main.setTotemCreateStatus(false);
		if(main.getScoreboardTotemFactionsStatus()) {
			for(Player players : Bukkit.getOnlinePlayers()) {
				main.getUtilsScorebordsManager().removeScorboards(players);
				if(main.getFeatherBoardStatus()) {
					if(!FeatherBoardAPI.isToggled(players)) {
						if(main.getFbList().contains(players)) {
							main.getFbList().remove(players);
							FeatherBoardAPI.toggle(players);
						}
					}
				}
			}
		}
	}

}