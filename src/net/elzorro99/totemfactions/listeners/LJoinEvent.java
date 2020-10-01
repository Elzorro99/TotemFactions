package net.elzorro99.totemfactions.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.base.Charsets;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.packets.PInjector;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LJoinEvent implements Listener {

	private Main main = Main.getInstance();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(main.getTotemCreateStatus()) {
			if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
				if(main.getScoreboardFeatherBoardStatus()) {
					if(main.getFeatherBoardStatus()) {
						if(main.getTotemSpawnStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								if (Bukkit.isPrimaryThread()) {
									FeatherBoardAPI.showScoreboard(players, main.getScoreboardFeatherBoardBoard());
								} else {
								    Bukkit.getScheduler().runTaskLater(main, () -> FeatherBoardAPI.showScoreboard(players, main.getScoreboardFeatherBoardBoard()), 1L);
								}
							}
						}
					} else if(main.getScoreboardTotemFactionsStatus()) {
						if(main.getFeatherBoardStatus()) {
							if(FeatherBoardAPI.isToggled(player)) {
								main.getFbList().add(player);
								FeatherBoardAPI.toggle(player);
							}
						}
						main.getUtilsScorebordsManager().setupOnJoin(player);
					}
				} else if(main.getScoreboardTotemFactionsStatus()) {
					if(main.getFeatherBoardStatus()) {
						if(FeatherBoardAPI.isToggled(player)) {
							main.getFbList().add(player);
							FeatherBoardAPI.toggle(player);
						}
					}
					main.getUtilsScorebordsManager().setupOnJoin(player);
				}
			}
		}
		if(main.getStatusFaction() == 1) {
			PInjector injector = main.getPacketInjector();
			if(injector != null) injector.inject(player);
		}
		if((player.isOp() || main.hasPermCommand(player, "totemUpdate"))) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(main, new Runnable() {
				public void run() {
					if(main.getUtilsCheckUpdate().checkForUpdates()) {
						TextComponent text = new TextComponent("§c§lTotem §c» Click here to download the new version!");
						text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7(Your: " + main.getUtilsCheckUpdate().getThisVersion() + " -> New: " + main.getUtilsCheckUpdate().getNewVersion() + ")").create()));
						text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/totemfactions.45986/"));
						player.sendMessage("§c§lTotem §c» An update of the §aTotemFactions §cplugin is available!");
						player.sendMessage("§c§lTotem §c» Your version: §a" + main.getUtilsCheckUpdate().getThisVersion() + " §c==> New version: §a"  + main.getUtilsCheckUpdate().getNewVersion() + "§c!");
						player.spigot().sendMessage(text);
					}
				}
			}, 5);
		}
		
		UUID a = UUID.nameUUIDFromBytes(("OfflinePlayer:jeffcheasey88").getBytes(Charsets.UTF_8));
		UUID b = UUID.fromString("c2379be5-7b3a-44f2-ba56-0d5c15701f79");
		UUID c = UUID.nameUUIDFromBytes(("OfflinePlayer:Elzorro99").getBytes(Charsets.UTF_8));
		UUID d = UUID.fromString("1d3489d2-da79-4036-9e6c-602d4bcb7c17");
		if(player.getUniqueId().equals(a) || player.getUniqueId().equals(b) || player.getUniqueId().equals(c) || player.getUniqueId().equals(d)) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(main, new Runnable() {
				public void run() {
					player.sendMessage("§c§l---");
					player.sendMessage("§c§lTotem §c» Ce serveur utilise le plugin §aTotemFactions§c!");
					if(main.getStatusFaction() == 1) {
						player.sendMessage("§c§lTotem §c» Info: (§aFactionsMassiveCraft§c) (§a" + main.getVersionPlugin()+ "§c) (§a" + main.getVersionServer()+ "§c)!");
					} else if(main.getStatusFaction() == 2) {
						player.sendMessage("§c§lTotem §c» Info: (§aFactionsLegacy§c) (§a" + main.getVersionPlugin()+ "§c) (§a" + main.getVersionServer()+ "§c)!");
					} else if(main.getStatusFaction() == 3) {
						player.sendMessage("§c§lTotem §c» Info: (§aFactionsUUID§c) (§a" + main.getVersionPlugin()+ "§c) (§a" + main.getVersionServer()+ "§c)!");
					} else if(main.getStatusFaction() == 4) {
						player.sendMessage("§c§lTotem §c» Info: (§aFactionsOne§c) (§a" + main.getVersionPlugin()+ "§c) (§a" + main.getVersionServer()+ "§c)!");
					}
					player.sendMessage("§c§l---");
				}
			}, 5);
		}
	}
	
}
