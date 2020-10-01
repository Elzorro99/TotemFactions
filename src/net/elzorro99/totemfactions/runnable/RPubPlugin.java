package net.elzorro99.totemfactions.runnable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.elzorro99.totemfactions.Main;

public class RPubPlugin extends BukkitRunnable {

	private Main main = Main.getInstance();

	@Override
	public void run() {
		if(main.getPubStatus()) {
			Bukkit.broadcastMessage("§f[§6Totem§f] " + "§6Rejoin nous sur le discord du plugin §bTotemFactions§f!");
			Bukkit.broadcastMessage("§f[§6Totem§f] " + "§6Lien: §7https://discord.gg/nZP7wZX");
			Bukkit.broadcastMessage("§f[§6Totem§f] " + "§6Discord communautaire on parle de dev§f, §6support§f.");
			Bukkit.broadcastMessage("§f[§6Totem§f] " + "§6Désactiver ce messages dans le fichier (config.yml)§f.");
			Bukkit.broadcastMessage("§f[§6Totem§f] " + "§6Puis utiliser la command§f: (/totem reload)");
		}
	}
	
}
