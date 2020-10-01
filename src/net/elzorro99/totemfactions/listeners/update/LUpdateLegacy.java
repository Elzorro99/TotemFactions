package net.elzorro99.totemfactions.listeners.update;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.elzorro99.totemfactions.Main;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.event.EventFactionsDisband;
import net.redstoneore.legacyfactions.event.EventFactionsNameChange;

public class LUpdateLegacy implements Listener {

	private Main main = Main.getInstance();
	
	@EventHandler
	public void onDisband(EventFactionsDisband event){
		FPlayer player = event.getFPlayer();
		main.getUtilsTop().removeFaction(main.getUtilsTop().getFactions(player.getFaction().getTag(), 0));
		main.getUtilsRankFactions().updateFactionDisband(event.getFaction().getTag());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSave(EventFactionsNameChange event){
		main.getUtilsTop().replaceName(event.getOldFactionTag(), event.getFactionTag());
		main.getUtilsRankFactions().updateFactionName(event.getOldFactionTag(), event.getFactionTag());
	}
	
}
