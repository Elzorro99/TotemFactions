package net.elzorro99.totemfactions.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.runnable.RTotemCreate;
import net.elzorro99.totemfactions.runnable.RTotemNow;

public class MCrons {

	private Main main = Main.getInstance();
	
	public void initCrons() {
		if(main.getAutoRespawnStatus()) {
			for(int i = 1; i<5; i++) {
				String access = "TotemLocations.totem" + i + ".";
				String stringCrons = main.fileConfigLocations.getString(access + "cron");
				predictorAdd(stringCrons);
				registerCrons(i, stringCrons);
			}
			main.getCronScheduler().start();
		}
	}
	
	private void predictorAdd(String stringCron) {
		if(main.getCronPredictor() == null) {
			main.setCronPredictor(stringCron);
		} else {
			if(stringCron.length() < 9) {return;}
			main.setCronPredictor(main.getCronPredictor() + "|" + stringCron);
		}
	}
	
	private void registerCrons(int numero, String stringCrons) {
		try {
			if(numero == 1) {
				main.getCronScheduler().schedule(stringCrons, new Runnable() {
					
					@Override
					public void run() {
						if(main.getTotemCreateStatus()) {return;}
						if(main.getUtilsLocationsTotems().verifLocationTotem(numero)) {
							main.setTotemCreateStatus(true);
							main.setCurrentTotemLocation(main.getUtilsLocationsTotems().getLocationsTotems(numero));
							main.setCurrentTotemName(main.getTotemNameByInt(numero));
							if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
								if(main.getScoreboardFeatherBoardStatus()) {
									if(!main.getFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
										for(Player players : Bukkit.getOnlinePlayers()) {
											main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
										}
									}
								} else if(main.getScoreboardTotemFactionsStatus()) {
									for(Player players : Bukkit.getOnlinePlayers()) {
										if(main.getFeatherBoardStatus()) {
											if(FeatherBoardAPI.isToggled(players)) {
												main.getFbList().add(players);
												FeatherBoardAPI.toggle(players);
											}
										}
										main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
									}
								}
							}
							if(main.getModeRespawn().equalsIgnoreCase("create")) {
								RTotemCreate start = new RTotemCreate();
								start.runTaskTimer(main, 0, 20);

							}else if(main.getModeRespawn().equalsIgnoreCase("now")) {
								RTotemNow start = new RTotemNow();
								start.runTaskTimer(main, 0, 20);
							}
						}
					}
				});
				
			} else if(numero == 2) {
				main.getCronScheduler().schedule(stringCrons, new Runnable() {
					
					@Override
					public void run() {
						if(main.getTotemCreateStatus()) {return;}
						if(main.getUtilsLocationsTotems().verifLocationTotem(numero)) {
							main.setTotemCreateStatus(true);
							main.setCurrentTotemLocation(main.getUtilsLocationsTotems().getLocationsTotems(numero));
							main.setCurrentTotemName(main.getTotemNameByInt(numero));
							if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
								if(main.getScoreboardFeatherBoardStatus()) {
									if(!main.getFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
										for(Player players : Bukkit.getOnlinePlayers()) {
											main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
										}
									}
								} else if(main.getScoreboardTotemFactionsStatus()) {
									for(Player players : Bukkit.getOnlinePlayers()) {
										if(main.getFeatherBoardStatus()) {
											if(FeatherBoardAPI.isToggled(players)) {
												main.getFbList().add(players);
												FeatherBoardAPI.toggle(players);
											}
										}
										main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
									}
								}
							}
							if(main.getModeRespawn().equalsIgnoreCase("create")) {
								RTotemCreate start = new RTotemCreate();
								start.runTaskTimer(main, 0, 20);

							}else if(main.getModeRespawn().equalsIgnoreCase("now")) {
								RTotemNow start = new RTotemNow();
								start.runTaskTimer(main, 0, 20);
							}
						}
					}
				});
				
			} else if(numero == 3) {
				main.getCronScheduler().schedule(stringCrons, new Runnable() {
					
					@Override
					public void run() {
						if(main.getTotemCreateStatus()) {return;}
						if(main.getUtilsLocationsTotems().verifLocationTotem(numero)) {
							main.setTotemCreateStatus(true);
							main.setCurrentTotemLocation(main.getUtilsLocationsTotems().getLocationsTotems(numero));
							main.setCurrentTotemName(main.getTotemNameByInt(numero));
							if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
								if(main.getScoreboardFeatherBoardStatus()) {
									if(!main.getFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
										for(Player players : Bukkit.getOnlinePlayers()) {
											main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
										}
									}
								} else if(main.getScoreboardTotemFactionsStatus()) {
									for(Player players : Bukkit.getOnlinePlayers()) {
										if(main.getFeatherBoardStatus()) {
											if(FeatherBoardAPI.isToggled(players)) {
												main.getFbList().add(players);
												FeatherBoardAPI.toggle(players);
											}
										}
										main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
									}
								}
							}
							if(main.getModeRespawn().equalsIgnoreCase("create")) {
								RTotemCreate start = new RTotemCreate();
								start.runTaskTimer(main, 0, 20);

							}else if(main.getModeRespawn().equalsIgnoreCase("now")) {
								RTotemNow start = new RTotemNow();
								start.runTaskTimer(main, 0, 20);
							}
						}
					}
				});
				
			} else if(numero == 4) {
				main.getCronScheduler().schedule(stringCrons, new Runnable() {
					
					@Override
					public void run() {
						if(main.getTotemCreateStatus()) {return;}
						if(main.getUtilsLocationsTotems().verifLocationTotem(numero)) {
							main.setTotemCreateStatus(true);
							main.setCurrentTotemLocation(main.getUtilsLocationsTotems().getLocationsTotems(numero));
							main.setCurrentTotemName(main.getTotemNameByInt(numero));
							if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
								if(main.getScoreboardFeatherBoardStatus()) {
									if(!main.getFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
										for(Player players : Bukkit.getOnlinePlayers()) {
											main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
										}
									}
								} else if(main.getScoreboardTotemFactionsStatus()) {
									for(Player players : Bukkit.getOnlinePlayers()) {
										if(main.getFeatherBoardStatus()) {
											if(FeatherBoardAPI.isToggled(players)) {
												main.getFbList().add(players);
												FeatherBoardAPI.toggle(players);
											}
										}
										main.getUtilsScorebordsManager().updateScorboards(players, null, 0, "pre");
									}
								}
							}
							if(main.getModeRespawn().equalsIgnoreCase("create")) {
								RTotemCreate start = new RTotemCreate();
								start.runTaskTimer(main, 0, 20);

							}else if(main.getModeRespawn().equalsIgnoreCase("now")) {
								RTotemNow start = new RTotemNow();
								start.runTaskTimer(main, 0, 20);
							}
						}
					}
				});
			}
		} catch (Exception e) {return;}
	}
	
}
