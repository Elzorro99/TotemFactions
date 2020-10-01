package net.elzorro99.totemfactions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.elzorro99.totemfactions.Main;

public class CError implements CommandExecutor {

	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("totem")) {
			Player player = (Player)sender;
			for(String messages : main.getMessagesStaff("loadError", sender)) {
				player.sendMessage(messages.replace("&", "§"));
			}
		}
		return false;
	}

}
