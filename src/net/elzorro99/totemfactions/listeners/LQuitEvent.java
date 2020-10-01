package net.elzorro99.totemfactions.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.elzorro99.totemfactions.Main;

public class LQuitEvent implements Listener {

	private Main main = Main.getInstance();
	
	@EventHandler
	public void onQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(main.getScoreboardsPlayer().containsKey(player)) {
			main.getScoreboardsPlayer().remove(player);
		}
		if(main.getFbList().contains(player)) {
			main.getFbList().remove(player);
		}
	}
	
}
