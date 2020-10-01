package net.elzorro99.totemfactions.listeners.update;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionRenameEvent;

import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.utils.UDataPack;
import net.elzorro99.totemfactions.utils.UPacketsInjector;

public class LUpdateUUID implements Listener{
	
	private Main main = Main.getInstance();
	
	@EventHandler
	public void onLeave(FPlayerLeaveEvent event){
		FPlayer player = null;
		if(main.getStatusFaction() == 4) player = (FPlayer)UPacketsInjector.reflect(FPlayerLeaveEvent.class, "FPlayer", event, null);
		else player = event.getfPlayer();
		UDataPack<Faction, String> data = main.getFaction(player, true);
		main.getUtilsTop().removeFaction(main.getUtilsTop().getFactions(data.getValue(), 0));
		main.getUtilsRankFactions().updateFactionDisband(data.getValue());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSave(FactionRenameEvent event){
		Bukkit.broadcastMessage("RENAME");
		main.getUtilsTop().replaceName(event.getOldFactionTag(), event.getFactionTag());
		main.getUtilsRankFactions().updateFactionName(event.getOldFactionTag(), event.getFactionTag());
	}
	
}
