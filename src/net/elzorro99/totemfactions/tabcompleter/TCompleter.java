package net.elzorro99.totemfactions.tabcompleter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.elzorro99.totemfactions.Main;

public class TCompleter implements TabCompleter {

	private Main main = Main.getInstance();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {return null;}
		if(!label.equalsIgnoreCase("totem")) {return null;}
		List<String> list = new ArrayList<>();
		Player player = (Player)sender;
		if(args.length == 1) {
			if(!main.hasPermCommand(player, "totemDefault")) {
				return null;
			}
			if(args[0].startsWith("se")) {
				list.add("setspawn");
				return list;
			} else if(args[0].startsWith("sta")) {
				list.add("stats");
				return list;
			} else if(args[0].startsWith("sto")) {
				list.add("stop");
				return list;
			} else if(args[0].startsWith("s")) {
				list.add("setspawn");
				list.add("stats");
				list.add("stop");
				return list;
			} else if(args[0].startsWith("ra")) {
				list.add("rank");
				return list;
			} else if(args[0].startsWith("rel")) {
				list.add("reload");
				return list;
			} else if(args[0].startsWith("res")) {
				list.add("reset");
				return list;
			} else if(args[0].startsWith("r")) {
				list.add("rank");
				list.add("reload");
				list.add("reset");
				return list;
			} else if(args[0].startsWith("c")) {
				list.add("create");
				return list;
			} else if(args[0].startsWith("n")) {
				list.add("now");
				return list;
			} else if(args[0].startsWith("i")) {
				list.add("info");
				return list;
			} else if(args[0].startsWith("t")) {
				list.add("timer");
				return list;
			} else if(args[0].startsWith("p")) {
				list.add("points");
				return list;
			} else {
				list.add("setspawn");
				list.add("create");
				list.add("now");
				list.add("stop");
				list.add("top");
				list.add("stats");
				list.add("info");
				list.add("timer");
				list.add("points");
				list.add("reset");
				list.add("reload");
				return list;
			}
		} else if(args.length == 2) {
			if(!args[0].equalsIgnoreCase("debug") && !main.hasPermCommand(player, "totemDefault")) {
				return list;
			}
			if(args[0].equalsIgnoreCase("setspawn") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("now")) {
				list.add("1");
				list.add("2");
				list.add("3");
				list.add("4");
				return list;
			} else if(args[0].equalsIgnoreCase("points")) {
				if(args[1].startsWith("a")) {
					list.add("add");
					return list;
				} else if(args[1].startsWith("r")) {
					list.add("remove");
					return list;
				} else if(args[1].startsWith("c")) {
					list.add("clear");
					return list;
				} else {
					list.add("add");
					list.add("remove");
					list.add("clear");
					return list;
				}
			}
		} else if(args.length == 4) {
			if(args[0].equals("points")) {
				if(args[3].startsWith("w")) {
					list.add("win");
					return list;
				} else if(args[3].startsWith("b")) {
					list.add("block");
					return list;
				} else {
					list.add("win");
					list.add("block");
					return list;
				}
			}
		}
		return list;
	}

}
