package net.elzorro99.totemfactions.runnable;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.events.TotemSpawnEvent;
import net.elzorro99.totemfactions.utils.UParticleEffect;

public class RTotemCreate extends BukkitRunnable {

	private Main main = Main.getInstance();
	private int timer = 300;
	private double i = 0.9;
	
	@Override
	public void run() {
		try {
			debug();
			if(timer == 300) {
				main.setTimerMinutes(5);
				main.setTimerSecondes(0);
			} else if(timer >= 240) {
				main.setTimerMinutes(4);
				main.setTimerSecondes(60-(300-timer));
			} else if(timer >= 180) {
				main.setTimerMinutes(3);
				main.setTimerSecondes(60-(240-timer));
			} else if(timer >= 120) {
				main.setTimerMinutes(2);
				main.setTimerSecondes(60-(180-timer));
			} else if(timer >= 60) {
				main.setTimerMinutes(1);
				main.setTimerSecondes(60-(120-timer));
			} else if(timer >= 0) {
				main.setTimerMinutes(0);
				main.setTimerSecondes(60-(60-timer));
			}
			if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
				if(main.getScoreboardFeatherBoardStatus()) {
					if(!main.getFeatherBoardStatus()) {
						main.getUtilsScorebordsManager().updateTimer(main.getTimerMinutes(), main.getTimerSecondes());
					}
				}
				else if(main.getScoreboardTotemFactionsStatus()) {
					main.getUtilsScorebordsManager().updateTimer(main.getTimerMinutes(), main.getTimerSecondes());
				}
			}
			if(timer == 300 || timer == 240 || timer == 180 || timer == 120 || timer == 60) {
				for(String messages : main.getMessagesPublic("timerMinutes", null)) {
					Bukkit.broadcastMessage(main.getPrefix() + messages.replace("&", "§")
																  .replace("<timer>", timer/60 + "")
																  .replace("<totemName>", main.getCurrentTotemName()));
				}
				if(main.getTitlesStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						main.getUtilsTitles().sendTitle(players, 5, 30, 5, "", main.getMessagesTitles("timerMinutes").replace("&", "§").replace("<totemName>", main.getCurrentTotemName()).replace("<timer>", timer/60 + ""));
					}
				}
				if(main.getSoundStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						players.playSound(players.getLocation(), Sound.NOTE_PLING, 1f, 1f);
					}
				}
			} else if(timer == 30 || timer == 15 || timer == 10) {
				for(String messages : main.getMessagesPublic("timerSecondes", null)) {
					Bukkit.broadcastMessage(main.getPrefix() + messages.replace("&", "§")
																  .replace("<timer>", timer + "")
																  .replace("<totemName>", main.getCurrentTotemName()));
				}
				if(main.getTitlesStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						main.getUtilsTitles().sendTitle(players, 5, 30, 5, "", main.getMessagesTitles("timerSecondes").replace("&", "§").replace("<totemName>", main.getCurrentTotemName()).replace("<timer>", timer + ""));
					}
				}
				if(main.getSoundStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						players.playSound(players.getLocation(), Sound.NOTE_PLING, 1f, 1f);
					}
				}
			} else if(timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {
				for(String messages : main.getMessagesPublic("timerSecondes", null)) {
					Bukkit.broadcastMessage(main.getPrefix() + messages.replace("&", "§")
																  .replace("<timer>", timer + "")
																  .replace("<totemName>", main.getCurrentTotemName()));
				}
				if(main.getTitlesStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						main.getUtilsTitles().sendTitle(players, 5, 10, 5, "", main.getMessagesTitles("timerSecondes").replace("&", "§").replace("<totemName>", main.getCurrentTotemName()).replace("<timer>", timer + ""));
					}
				}
				if(main.getSoundStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						players.playSound(players.getLocation(), Sound.NOTE_PLING, 1f, (float)i);
					}
				}
				i = (i-0.1);
			} else if(timer == 0) {
				for(String messages : main.getMessagesPublic("timerEnd", null)) {
					Bukkit.broadcastMessage(main.getPrefix() + messages.replace("&", "§")
																  .replace("<totemName>", main.getCurrentTotemName())
																  .replace("<x>", main.getCurrentTotemLocation().getBlockX() + "")
																  .replace("<y>", main.getCurrentTotemLocation().getBlockY() + "")
																  .replace("<z>", main.getCurrentTotemLocation().getBlockZ() + ""));
				}
				Bukkit.getPluginManager().callEvent(new TotemSpawnEvent(main.getCurrentTotemName(), main.getCurrentTotemLocation()));
				if(main.getParticleStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						UParticleEffect.EXPLOSION_NORMAL.display(0.2f, 1.5f, 0.2f, 0.15f, 150, main.getCurrentTotemLocation().clone().add(0.5, 1.5, 0.5), players);
					}
				}
				if(main.getSoundStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						players.playSound(players.getLocation(), Sound.EXPLODE, 1f, 1f);
					}
				}
				if(main.getTitlesStatus()) {
					for(Player players : Bukkit.getOnlinePlayers()) {
						main.getUtilsTitles().sendTitle(players, 5, 30, 5, "", main.getMessagesTitles("timerEnd").replace("&", "§").replace("<totemName>", main.getCurrentTotemName()));
					}
				}
				if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
					if(main.getScoreboardFeatherBoardStatus()) {
						if(main.getFeatherBoardStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								if (Bukkit.isPrimaryThread()) {
									FeatherBoardAPI.showScoreboard(players, main.getScoreboardFeatherBoardBoard());
								} else {
								    Bukkit.getScheduler().runTaskLater(main, () -> FeatherBoardAPI.showScoreboard(players, main.getScoreboardFeatherBoardBoard()), 1L);
								}
							}
						} else if(main.getScoreboardTotemFactionsStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "in");
							}
						}
					} else if(main.getScoreboardTotemFactionsStatus()) {
						for(Player players : Bukkit.getOnlinePlayers()) {
							main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "in");
						}
					}
				}
				main.getUtilsPhysicBlocks().buildTotem(main.getCurrentTotemLocation());
				RTimerTotem start = new RTimerTotem();
				start.runTaskTimer(main, 0, 20);
				cancel();
				return;
			}
			timer--;
		} catch (Exception e) {e.printStackTrace(); cancel(); return;}
	}

	private void debug() {
		if(!main.getTotemCreateStatus()) {timer = 1000; cancel(); return;}
	}
}

