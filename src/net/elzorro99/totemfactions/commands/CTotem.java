package net.elzorro99.totemfactions.commands;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.Faction;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.events.TotemStartEvent;
import net.elzorro99.totemfactions.runnable.RTotemCreate;
import net.elzorro99.totemfactions.runnable.RTotemNow;
import net.elzorro99.totemfactions.utils.UDataPack;
import net.elzorro99.totemfactions.utils.cron.Predictor;

public class CTotem implements CommandExecutor {

	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("totem")) {
			if(args.length == 0) {
				if(sender instanceof Player) {
					Player player = (Player)sender;
					if(!main.hasPermCommand(player, "totemDefault")) {
						for(String messages : main.getMessagesPublic("notPermission", sender)) {
							player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
						}
						return false;
					}
				}
				for(String messages : main.getMessagesPublic("commands", sender)) {
					sender.sendMessage(main.getPrefix() + messages.replace("&", "§"));
				}
				return false;
			}
			if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("setspawn")) {
					if(!(sender instanceof Player)) {
						for(String messages : main.getMessagesStaff("consoleNotAllowed", sender)) {
							main.getLogger().log(Level.INFO, messages);
						}
						return false;
					}
					Player player = (Player)sender;
					Location location = player.getLocation();
					if(!main.hasPermCommand(player, "totemSetSpawn")) {
						for(String messages : main.getMessagesPublic("notPermission", sender)) {
							player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
						}
						return false;
					}
					if(args.length == 2) {
						if(isInt(args[1])) {
							int numero = Integer.parseInt(args[1]);
							if(!(numero < 1)  && !(numero > 4)) {
								main.getUtilsLocationsTotems().setLocationsTotems(location, numero);
								for(String messages : main.getMessagesStaff("setSpawn", sender)) {
									player.sendMessage(main.getPrefix() + messages.replace("&", "§")
																				  .replace("<name>", main.getTotemNameByInt(numero))
																				  .replace("<numero>", numero + "")
																				  .replace("<world>", location.getWorld().getName())
																				  .replace("<x>", location.getBlockX() + "")
																				  .replace("<y>", location.getBlockY() + "")
																				  .replace("<z>", location.getBlockZ() + ""));
								}
								return false;
							}
						}
						for(String messages : main.getMessagesStaff("setSpawnInt", sender)) {
							player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
						}
						return false;
					} else {
						for(String messages : main.getMessagesStaff("setSpawnId", sender)) {
							player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
						}
						return false;
					}
				} else if(args[0].equalsIgnoreCase("create")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemSpawn")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					if(args.length == 2) {
						if(isInt(args[1])) {
							int numero = Integer.parseInt(args[1]);
							if(!(numero < 1)  && !(numero > 4)) {
								if(main.getTotemCreateStatus()) {
									for(String msg : main.getMessagesStaff("totemAlreadyLaunched", sender)) {
										sender.sendMessage(main.getPrefix() + msg.replace("&", "§"));
									}
									return false;
								}
								if(!main.getUtilsLocationsTotems().verifLocationTotem(numero)) {
									for(String messages : main.getMessagesStaff("locationError", sender)) {
										sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																					  .replace("<id>", numero + "")
																					  .replace("<totemName>", main.getTotemNameByInt(numero)));
									}
									return false;
								}
								main.setTotemCreateStatus(true);
								main.setCurrentTotemLocation(main.getUtilsLocationsTotems().getLocationsTotems(numero));
								main.setCurrentTotemName(main.getTotemNameByInt(numero));
								Bukkit.getPluginManager().callEvent(new TotemStartEvent(main.getCurrentTotemName(), main.getCurrentTotemLocation()));
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
								for(String messages : main.getMessagesStaff("totemInCreation", sender)) {
									sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																				  .replace("<totemName>", main.getCurrentTotemName()));
								}
								RTotemCreate start = new RTotemCreate();
								start.runTaskTimer(main, 0, 20);
								return false;
							}
						}
						for(String messages : main.getMessagesStaff("spawnTotemInt", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<type>", "create"));
						}
						return false;
					} else {
						for(String messages : main.getMessagesStaff("spawnTotemId", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<type>", "create"));
						}
						return false;
					}
				} else if(args[0].equalsIgnoreCase("now")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemSpawn")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					if(args.length == 2) {
						if(isInt(args[1])) {
							int numero = Integer.parseInt(args[1]);
							if(!(numero < 1)  && !(numero > 4)) {
								if(main.getTotemCreateStatus()) {
									for(String msg : main.getMessagesStaff("totemAlreadyLaunched", sender)) {
										sender.sendMessage(main.getPrefix() + msg.replace("&", "§"));
									}
									return false;
								}
								if(!main.getUtilsLocationsTotems().verifLocationTotem(numero)) {
									for(String messages : main.getMessagesStaff("locationError", sender)) {
										sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																					  .replace("<id>", numero + "")
																					  .replace("<totemName>", main.getTotemNameByInt(numero)));
									}
									return false;
								}
								main.setCurrentTotemLocation(main.getUtilsLocationsTotems().getLocationsTotems(numero));
								main.setCurrentTotemName(main.getTotemNameByInt(numero));
								Bukkit.getPluginManager().callEvent(new TotemStartEvent(main.getCurrentTotemName(), main.getCurrentTotemLocation()));
								if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
									if(main.getScoreboardFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
										if(!main.getFeatherBoardStatus()) {
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
								main.setTotemCreateStatus(true);
								for(String messages : main.getMessagesStaff("totemInCreation", sender)) {
									sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																				  .replace("<totemName>", main.getCurrentTotemName()));
								}
								RTotemNow start = new RTotemNow();
								start.runTaskTimer(main, 0, 20);
								return false;
							}
						}
						for(String messages : main.getMessagesStaff("spawnTotemInt", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<type>", "now"));
						}
						return false;
					} else {
						for(String messages : main.getMessagesStaff("spawnTotemId", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<type>", "now"));
						}
						return false;
					}
				} else if(args[0].equalsIgnoreCase("stop")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemStop")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					if(!main.getTotemCreateStatus()) {
						for(String messages : main.getMessagesStaff("cantTotemStop", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§"));
						}
						return false;
					}
					if(main.getTitlesStatus()) {
						for(Player players : Bukkit.getOnlinePlayers()) {
							main.getUtilsTitles().sendTitle(players, 5, 30, 5, main.getMessagesTitles("totemStop").replace("&", "§").replace("<totemName>", main.getCurrentTotemName()), "");
						}
					}
					if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
						if(main.getScoreboardFeatherBoardStatus()) {
							if(main.getFeatherBoardStatus()) {
								for(Player players : Bukkit.getOnlinePlayers()) {
									if (Bukkit.isPrimaryThread()) {
									    FeatherBoardAPI.removeScoreboardOverride(players, main.getScoreboardFeatherBoardBoard());
										FeatherBoardAPI.resetDefaultScoreboard(players);
									} else {
										Bukkit.getScheduler().runTaskLater(main, () -> {
											FeatherBoardAPI.removeScoreboardOverride(players, main.getScoreboardFeatherBoardBoard());
											FeatherBoardAPI.resetDefaultScoreboard(players);
										}, 1L);
									}
								}
							} else if(main.getScoreboardTotemFactionsStatus()) {
								for(Player players : Bukkit.getOnlinePlayers()) {
									main.getUtilsScorebordsManager().removeScorboards(players);
								}
							}
						} else if(main.getScoreboardTotemFactionsStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								main.getUtilsScorebordsManager().removeScorboards(players);
							}
						}
					}
					for(String messages : main.getMessagesStaff("stopTotemComplete", sender)) {
						sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																	  .replace("<totemName>", main.getCurrentTotemName()));
					}
					for(String messages : main.getMessagesPublic("stop", sender)) {
						Bukkit.broadcastMessage(main.getPrefix() + messages.replace("&", "§")
																	  .replace("<totemName>", main.getCurrentTotemName()));
					}
					main.getUtilsPhysicBlocks().unBuildTotem(main.getCurrentTotemLocation());
					main.setTotemCreateStatus(false);
					return false;
				} else if(args[0].equalsIgnoreCase("stats")) {
					if(!(sender instanceof Player)) {
						for(String messages : main.getMessagesStaff("consoleNotAllowed", sender)) {
							main.getLogger().log(Level.INFO, messages);
						}
						return false;
					}
					Player player = (Player)sender;
					if(!main.hasPermCommand(player, "totemDefault")) {
						for(String messages : main.getMessagesPublic("notPermission", sender)) {
							player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
						}
						return false;
					}
					if(main.getStatusFaction() == 1) {
						com.massivecraft.factions.entity.MPlayer factionplayer = com.massivecraft.factions.entity.MPlayer.get(player);
						String factionname = factionplayer.getFaction().getName();
						for(String messages : main.getMessagesPublic("stats", sender)) {
							player.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<factionName>", factionname)
																		  .replace("<wins>", main.getUtilsRankFactions().getWinsFactions(factionname) + "")
																		  .replace("<blocks>", main.getUtilsRankFactions().getBlocksBreak(factionname) + ""));
						}
					} else if(main.getStatusFaction() == 2) {
						net.redstoneore.legacyfactions.entity.FPlayer factionplayer = net.redstoneore.legacyfactions.entity.FPlayerColl.get(player);
						String factionname = factionplayer.getFaction().getTag();
						for(String messages : main.getMessagesPublic("stats", sender)) {
							player.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<factionName>", factionname)
																		  .replace("<victoir>", main.getUtilsRankFactions().getWinsFactions(factionname) + "")
																		  .replace("<blocks>", main.getUtilsRankFactions().getBlocksBreak(factionname) + ""));
						}
					} else if(main.getStatusFaction() == 3 || main.getStatusFaction() == 4) {
						try {
							UDataPack<Faction, String> data = main.getFaction(player, true);
							String factionname = data.getValue();
							for(String messages : main.getMessagesPublic("stats", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§")
																			  .replace("<factionName>", factionname)
																			  .replace("<wins>", main.getUtilsRankFactions().getWinsFactions(factionname) + "")
																			  .replace("<blocks>", main.getUtilsRankFactions().getBlocksBreak(factionname) + ""));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return false;
				} else if(args[0].equalsIgnoreCase("info")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemDefault")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					Location location1 = main.getUtilsLocationsTotems().getLocationsTotems(1);
					Location location2 = main.getUtilsLocationsTotems().getLocationsTotems(2);
					Location location3 = main.getUtilsLocationsTotems().getLocationsTotems(3);
					Location location4 = main.getUtilsLocationsTotems().getLocationsTotems(4);
					for(String messages : main.getMessagesPublic("info", sender)) {
						sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																	  .replace("<versionPlugin>", main.getVersionPlugin())
																	  .replace("<versionServer>", main.getVersionServer())
																	  .replace("<totemName1>", main.getTotemNameByInt(1))
																	  .replace("<totemName2>", main.getTotemNameByInt(2))
																	  .replace("<totemName3>", main.getTotemNameByInt(3))
																	  .replace("<totemName4>", main.getTotemNameByInt(4))
																	  .replace("<x1>", location1.getBlockX() + "").replace("<y1>", location1.getBlockY() + "").replace("<z1>", location1.getBlockZ() + "").replace("<world1>", main.getTotemWorldByInt(1))
																	  .replace("<x2>", location2.getBlockX() + "").replace("<y2>", location2.getBlockY() + "").replace("<z2>", location2.getBlockZ() + "").replace("<world2>", main.getTotemWorldByInt(2))
																	  .replace("<x3>", location3.getBlockX() + "").replace("<y3>", location3.getBlockY() + "").replace("<z3>", location3.getBlockZ() + "").replace("<world3>", main.getTotemWorldByInt(3))
																	  .replace("<x4>", location4.getBlockX() + "").replace("<y4>", location4.getBlockY() + "").replace("<z4>", location4.getBlockZ() + "").replace("<world4>", main.getTotemWorldByInt(4)));
					}
				} else if(args[0].equalsIgnoreCase("timer")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemDefault")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					DecimalFormat format = new DecimalFormat("00");
					String formatMinutes = format.format(main.getTimerMinutes());
					String formatSecondes = format.format(main.getTimerSecondes());
					if(main.getTotemCreateStatus() && !main.getTotemSpawnStatus()) {
						for(String messages : main.getMessagesPublic("timerCountdown", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<minutes>", formatMinutes)
																		  .replace("<seconds>", formatSecondes));
						}
					} else if(!main.getTotemCreateStatus() && !main.getTotemSpawnStatus()) {
						Predictor predictor = new Predictor(main.getCronPredictor());
						long next_predictor = predictor.nextMatchingTime();
						long next_millis = next_predictor - new GregorianCalendar().getTimeInMillis();
						GregorianCalendar test2 = new GregorianCalendar();
						test2.setTimeInMillis(next_predictor);
						SimpleDateFormat dateFormat = new SimpleDateFormat("'§b'EEEE'§f', '§b'dd/MM'§f', '§b'HH'h'mm'§f'", Locale.getDefault());
						for(String messages : main.getMessagesPublic("timerCron", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<date-countdown>", String.format("§b%02dh§f, §b%02dm§f, §b%02ds§f", 
																				TimeUnit.MILLISECONDS.toHours(next_millis),
																				TimeUnit.MILLISECONDS.toMinutes(next_millis) - 
																				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(next_millis)),
																				TimeUnit.MILLISECONDS.toSeconds(next_millis) - 
																				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(next_millis))))
																		  .replace("<date-schedule>", dateFormat.format(test2.getTime())));
						}
					} else {
						for(String messages : main.getMessagesPublic("timerDelay", sender)) {
							sender.sendMessage(main.getPrefix() + messages.replace("&", "§")
																		  .replace("<minutes>", formatMinutes)
																		  .replace("<seconds>", formatSecondes));
						}
					}
					return false;
				} else if(args[0].equalsIgnoreCase("rank") || args[0].equalsIgnoreCase("top") || args[0].equalsIgnoreCase("classement")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemDefault")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					if(args.length == 1) {
						main.getUtilsRankFactions().sendClassementFaction(sender, 0);
					} else if(args.length == 2) {
						if(isInt(args[1])) {
							int numero = Integer.parseInt(args[1]);
							main.getUtilsRankFactions().sendClassementFaction(sender, (numero-1));
						}
					}
					return false;
				} else if(args[0].equalsIgnoreCase("point") || args[0].equalsIgnoreCase("points")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemPoints")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					if(args.length >= 2 && args.length <= 5) {
						if(args.length == 5) {
							if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {
								if(main.fileConfigStats.contains("TotemStats." + args[2])) {
									if(args[3].equalsIgnoreCase("win") || args[3].equalsIgnoreCase("block")) {
										if(isInt(args[4])) {
											if(args[1].equalsIgnoreCase("add")) {
												main.getUtilsRankFactions().addPointsToFaction(args[2], Integer.parseInt(args[4]), args[3]);
												for(String messages : main.getMessagesStaff("pointsAddRemoveComplete", sender)) {
													sender.sendMessage(main.getPrefix() + messages.replace("&", "§").replace("<factionName>", args[2]).replace("<type>", args[3]).replace("<points>", args[4]));
												}
												return false;
											} else if(args[1].equalsIgnoreCase("remove")) {
												main.getUtilsRankFactions().removePointsToFaction(args[2], Integer.parseInt(args[4]), args[3]);
												for(String messages : main.getMessagesStaff("pointsAddRemoveComplete", sender)) {
													sender.sendMessage(main.getPrefix() + messages.replace("&", "§").replace("<factionName>", args[2]).replace("<type>", args[3]).replace("<points>", args[4]));
												}
												return false;
											}
										}
									}
								}
							}
						} else if(args.length == 3) {
							if(args[1].equalsIgnoreCase("clear")) {
								if(main.fileConfigStats.contains("TotemStats." + args[2])) {
									main.getUtilsRankFactions().updateFactionDisband(args[2]);
									for(String messages : main.getMessagesStaff("pointsClearComplete", sender)) {
										sender.sendMessage(main.getPrefix() + messages.replace("&", "§").replace("<factionName>", args[2]));
									}
									return false;
								}
							}
						}
					}
					for(String messages : main.getMessagesStaff("pointsError", sender)) {
						sender.sendMessage(main.getPrefix() + messages.replace("&", "§"));
					}
					return false;
				} else if(args[0].equalsIgnoreCase("reset")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemReset")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					main.getManagerFiles().saveStats();
					for(String messages : main.getMessagesStaff("resetComplete", sender)) {
		   				sender.sendMessage(main.getPrefix() + messages.replace("&", "§"));
		   			}
					return false;
				} else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemReload")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					PluginManager pluginmanager = main.getServer().getPluginManager();
					long timer_start = System.currentTimeMillis();
					try {
						pluginmanager.disablePlugin(pluginmanager.getPlugin("TotemFactions"));
						pluginmanager.enablePlugin(pluginmanager.getPlugin("TotemFactions"));
					} catch (Exception e) {
						for(String messages : main.getMessagesStaff("reloadError", sender)) {
							sender.sendMessage(messages.replace("&", "§"));
						}
						return false;
					}
					long timer_end = System.currentTimeMillis();
					for(String messages : main.getMessagesStaff("reloadComplete", sender)) {
						sender.sendMessage(main.getPrefix() + messages.replace("&", "§").replace("<timerMs>", timer_end-timer_start + ""));
					}
					return false;
				} else if(args[0].equalsIgnoreCase("debug")) {
					if(args.length == 1) {
						sender.sendMessage("§6Name§f: §7TotemFactions");
						sender.sendMessage("§6By§f: §7Elzorro99, jeffcheasey88");
						sender.sendMessage("§6Version§f: §7" + main.getVersionPlugin());
						sender.sendMessage("§6Download§f: §7https://goo.gl/6QSXZ2");
						sender.sendMessage("§6Discord§f: §7https://discord.gg/nZP7wZX");
					}
					return false;
				} else {
					if(sender instanceof Player) {
						Player player = (Player)sender;
						if(!main.hasPermCommand(player, "totemDefault")) {
							for(String messages : main.getMessagesPublic("notPermission", sender)) {
								player.sendMessage(main.getPrefix() + messages.replace("&", "§"));
							}
							return false;
						}
					}
					for(String messages : main.getMessagesPublic("commandNotExist", sender)) {
						sender.sendMessage(main.getPrefix() + messages.replace("&", "§").replace("<command>", args[0]));
					}
					return false;
				}
			}
		}
		return false;
	}

	public boolean isInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {return false;}
	}
	
}
