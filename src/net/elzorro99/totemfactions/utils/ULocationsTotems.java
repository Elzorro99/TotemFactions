package net.elzorro99.totemfactions.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.elzorro99.totemfactions.Main;

public class ULocationsTotems {

	private Main main = Main.getInstance();
	
	public void setLocationsTotems(Location location, int numero) {
		String access = "TotemLocations.totem" + numero + ".";
		main.fileConfigLocations.set(access + "x", location.getBlockX());
		main.fileConfigLocations.set(access + "y", location.getBlockY());
		main.fileConfigLocations.set(access + "z", location.getBlockZ());
		main.fileConfigLocations.set(access + "world", location.getWorld().getName());
		main.getManagerFiles().saveResource(main.fileLocations);
	}
	
	public Location getLocationsTotems(int numero) {
		String access = "TotemLocations.totem" + numero + ".";
		int x = main.fileConfigLocations.getInt(access + "x");
		int y = main.fileConfigLocations.getInt(access + "y");
		int z = main.fileConfigLocations.getInt(access + "z");
		World world = Bukkit.getWorld(main.fileConfigLocations.getString(access + "world"));
		return new Location(world, x, y, z);
	}
	
	public boolean verifLocationTotem(int numero) {
		String access = "TotemLocations.totem" + numero + ".world";
		if(main.fileConfigLocations.getString(access) != null) {
			World w = Bukkit.getWorld(main.fileConfigLocations.getString(access));
			if(w != null) {
				if(w.getLoadedChunks().length > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
