package net.elzorro99.totemfactions.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import net.elzorro99.totemfactions.Main;

public class UPhysicBlocks {

private Main main = Main.getInstance();
	
	public void buildTotem(Location locationtotem) {
		main.setTotemSpawnStatus(true);
		Block block = locationtotem.getBlock();
		for(int a = 0; a != 5; a++) {
			block.setType(Material.getMaterial(main.getTypeTotemBlock()));
			main.getLocationBlocksTotem().add(block.getLocation());
			block = block.getRelative(BlockFace.UP);
		}
	}
	
	public void unBuildTotem(Location locationtotem) {
		main.setTotemSpawnStatus(false);
		Block block = locationtotem.getBlock();
		for(int a = 0; a != 5; a++) {
			block.setType(Material.AIR);
			block = block.getRelative(BlockFace.UP);
		}
		main.getLocationBlocksTotem().clear();
		main.setCurrentFactionName(main.fileConfigMessages.getString("TotemMessages.scoreboard.defaultFaction").replace("&", "§"));
		main.setCurrentTotemName("None");
	}
	
}
