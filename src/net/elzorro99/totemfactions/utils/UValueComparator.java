package net.elzorro99.totemfactions.utils;

import java.util.Comparator;
import java.util.Map;

import org.bukkit.entity.Player;

public class UValueComparator implements Comparator<Player> {
	
	Map<Player, Integer> base;
	
	public UValueComparator(Map<Player, Integer> base) {
		this.base = base;
	}
	
	public int compare(Player a, Player b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		}
	}
	
}
