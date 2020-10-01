
package net.elzorro99.totemfactions.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

import net.elzorro99.totemfactions.Main;

public class MFiles {

	private Main main = Main.getInstance();
	
	public void initFiles() {
		setupResource("config.yml", false);
		setupResource("messages.yml", false);
		setupResource("stats.yml", false);
		setupResource("locations.yml", false);
		main.fileConfigConfig = YamlConfiguration.loadConfiguration(main.fileConfig);
		main.fileConfigMessages = YamlConfiguration.loadConfiguration(main.fileMessages);
		main.fileConfigStats = YamlConfiguration.loadConfiguration(main.fileStats);
		main.fileConfigLocations = YamlConfiguration.loadConfiguration(main.fileLocations);
		main.getUtilsTop().initAll(main.fileConfigStats);
	}
	
	public void setupResource(String fileName, boolean reset) {
		InputStream in = main.getResource(fileName);
		if (in == null) {throw new IllegalArgumentException("The embedded resource '" + fileName + "' cannot be found!");}
		File outDir1 = new File(main.getDataFolder(), "/stats");
		if(!outDir1.exists()) {
			outDir1.mkdirs();
		}
		String fileNameString = fileName.toLowerCase();
		if(fileNameString.equals("stats.yml")) {
			File outFile = new File(main.getDataFolder() + "/stats", fileName);
			if(!outFile.exists()) {
				try {
					OutputStream out = new FileOutputStream(outFile);
			    	byte[] buf = new byte['?'];
			    	int len;
			    	while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
			    	}
			    	out.close();
			        in.close();
			        return;
				} catch (Exception e) {main.logConsole(Level.WARNING, "File " + fileName + " was not found!");}
			}
		} else if(fileNameString.equals("config.yml") || fileNameString.equals("locations.yml") || fileNameString.equals("messages.yml")) {
			File outFile = new File(main.getDataFolder(), fileName);
			if(!outFile.exists()) {
				try {
					OutputStream out = new FileOutputStream(outFile);
			    	byte[] buf = new byte['?'];
			    	int len;
			    	while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
			    	}
			    	out.close();
			        in.close();
			        return;
				} catch (Exception e) {main.logConsole(Level.WARNING, "File " + fileName + " was not found!");}
			}
		};
	}
	
	public void saveStats() {
		String fileName = "stats.yml";
		InputStream in = main.getResource(fileName);
	    if (in == null) {throw new IllegalArgumentException("The embedded resource '" + fileName + "' cannot be found!");}
	    File outDir1 = new File(main.getDataFolder(), "/stats");
		if(!outDir1.exists()) {
			outDir1.mkdirs();
		}
		File outFile = new File(main.getDataFolder() + "/stats", fileName);
		if(!outFile.exists()) {
			try {
				OutputStream out = new FileOutputStream(outFile);
		    	byte[] buf = new byte['?'];
		    	int len;
		    	while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
		    	}
		    	out.close();
		        in.close();
		        return;
			} catch (Exception e) {main.logConsole(Level.WARNING, "File " + fileName + " was not found!");}
		} else {
			try {
	    		SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
	    		Files.copy(main.fileStats, new File(outDir1 + "/" +  date.format(new Date()) + ".yml"));
	    		OutputStream out = new FileOutputStream(outFile);
		    	byte[] buf = new byte['?'];
		    	int len;
		    	while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
		    	}
		    	out.close();
		        in.close();
			} catch (Exception e) {main.logConsole(Level.WARNING, "The plugin failed to save the statistics file."); e.printStackTrace();}
		}
		main.fileConfigStats = YamlConfiguration.loadConfiguration(main.fileStats);
		main.getUtilsTop().initAll(main.fileConfigStats);
	}
	
	public void saveResource(File fileName) {
		try {
			if(fileName == main.fileLocations) {
				main.fileConfigLocations.save(fileName);
			} else if(fileName == main.fileStats) {
				main.fileConfigStats.save(fileName);
			} else {
				main.getLogger().log(Level.WARNING, "The file '" + fileName.getName() + "' can not be saved! (Filename exist: location.yml or stats.yml)");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
