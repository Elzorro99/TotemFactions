package net.elzorro99.totemfactions.managers;

import org.bukkit.plugin.PluginManager;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.LBlockBreakFaction;
import net.elzorro99.totemfactions.listeners.LBlockBreakLegacy;
import net.elzorro99.totemfactions.listeners.LBlockBreakUUID;
import net.elzorro99.totemfactions.listeners.LJoinEvent;
import net.elzorro99.totemfactions.listeners.LQuitEvent;
import net.elzorro99.totemfactions.listeners.update.LUpdateFaction;
import net.elzorro99.totemfactions.listeners.update.LUpdateLegacy;
import net.elzorro99.totemfactions.listeners.update.LUpdateUUID;

public class MListeners {

	private Main main = Main.getInstance();
	
	public void initEvents() {
		PluginManager pluginmanager = main.getServer().getPluginManager();
		pluginmanager.registerEvents(new LQuitEvent(), main);
		pluginmanager.registerEvents(new LJoinEvent(), main);
		if(main.getStatusFaction() == 1) {
			pluginmanager.registerEvents(new LBlockBreakFaction(), main);
			pluginmanager.registerEvents(new LUpdateFaction(), main);
		} else if(main.getStatusFaction() == 2) {
			pluginmanager.registerEvents(new LBlockBreakLegacy(), main);
			pluginmanager.registerEvents(new LUpdateLegacy(), main);
		} else if(main.getStatusFaction() == 3 || main.getStatusFaction() == 4) {
			pluginmanager.registerEvents(new LBlockBreakUUID(), main);
			pluginmanager.registerEvents(new LUpdateUUID(), main);
		}
	}
	
}