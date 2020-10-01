package net.elzorro99.totemfactions.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import net.elzorro99.totemfactions.Main;

public class UCheckUpdate {
	
	private Main main;
	private URL checkURL;
	private String thisVersion;
	private String newVersion;
	
	public UCheckUpdate(Main main, Integer pluginId) {
		this.main = main;
		this.thisVersion = main.getVersionPlugin();
		try {
			checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + pluginId);
		} catch (MalformedURLException ex) {
			main.logConsole(Level.WARNING, "CHECK_UPDATE_ERROR_1");
		}
	}
	
	public boolean checkForUpdates() {
		try {
			URLConnection con = checkURL.openConnection();
			newVersion = "v" + new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			return !thisVersion.equals(newVersion);
		} catch (Exception ex) {
			main.logConsole(Level.WARNING, "CHECK_UPDATE_ERROR_2");
		}
		return false;
	}
	
	public String getThisVersion() {
		return thisVersion;
	}
	
	public String getNewVersion() {
		return newVersion;
	}
	
	
	
}
