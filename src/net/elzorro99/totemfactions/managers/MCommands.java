package net.elzorro99.totemfactions.managers;

import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.commands.CError;
import net.elzorro99.totemfactions.commands.CTotem;
import net.elzorro99.totemfactions.tabcompleter.TCompleter;

public class MCommands {

	private Main main = Main.getInstance();
	
	public void initCommands() {
		if(main.getStatusFaction() == 0) {main.getCommand("totem").setExecutor(new CError()); return;};
		main.getCommand("totem").setExecutor(new CTotem());
		main.getCommand("totem").setTabCompleter(new TCompleter());
	}

}
