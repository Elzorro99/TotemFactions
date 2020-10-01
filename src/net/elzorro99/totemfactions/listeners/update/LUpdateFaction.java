package net.elzorro99.totemfactions.listeners.update;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsNameChange;

import net.elzorro99.totemfactions.Main;

public class LUpdateFaction implements Listener {
	
	private Main main = Main.getInstance();

	@EventHandler
	public void onDisband(EventFactionsDisband event){
		MPlayer player = event.getMPlayer();
		main.getUtilsTop().removeFaction(main.getUtilsTop().getFactions(player.getFaction().getName(), 0));
		main.getUtilsRankFactions().updateFactionDisband(event.getFaction().getName());
	}
	
	@EventHandler
	public void onSave(EventFactionsNameChange event){
		main.getUtilsTop().replaceName(event.getFaction().getName(), event.getNewName());
		main.getUtilsRankFactions().updateFactionName(event.getFaction().getName(), event.getNewName());
	}
	
}
