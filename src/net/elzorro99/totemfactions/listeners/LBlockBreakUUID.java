package net.elzorro99.totemfactions.listeners;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.massivecraft.factions.Faction;
import be.maximvdw.featherboard.api.FeatherBoardAPI;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.events.TotemBlockBreakEvent;
import net.elzorro99.totemfactions.events.TotemEndEvent;
import net.elzorro99.totemfactions.runnable.RFireworks;
import net.elzorro99.totemfactions.runnable.RFireworksWin;
import net.elzorro99.totemfactions.runnable.RRemoveScoreboard;
import net.elzorro99.totemfactions.utils.UDataPack;
import net.elzorro99.totemfactions.utils.UParticleEffect;
import net.elzorro99.totemfactions.utils.UValueComparator;

public class LBlockBreakUUID implements Listener {

	private Main main = Main.getInstance();
	private HashMap<String, Integer> listFactions = new HashMap<>();
	private HashMap<Player, Integer> listBlocksBreakPlayers =  new HashMap<>();
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		try {
			if(main.getTotemCreateStatus()) {
				if(main.getLocationBlocksTotem().contains(block.getLocation())) {
					event.setCancelled(true);
					UDataPack<Faction, String> data = main.getFaction(player, false);
					if(data == null){
						for(String messages : main.getMessagesPublic("dontHaveFaction", player)) {
							player.sendMessage((main.getPrefix() + messages).replace("&", "§"));
						}
						return;
					}
					if(!main.rightItem(player)) {
						for(String msg : main.getMessagesPublic("breakTotemBadItem", player)) {
							player.sendMessage((main.getPrefix() + msg).replace("&", "§")
																	   .replace("<item>", main.getItemBreakTotem().toString().replace("[", "").replace("]", "")));
						}
						return;
					}
					String factionName = data.getValue();
					main.getLocationBlocksTotem().remove(block.getLocation());
					Bukkit.getPluginManager().callEvent(new TotemBlockBreakEvent(player, factionName, main.getCurrentTotemName(), main.getCurrentTotemLocation(), main.getLocationBlocksTotem().size(), block, player));
					if(!listFactions.containsKey(factionName)) {
						listFactions.put(factionName, 1);
					} else {
						listFactions.put(factionName, listFactions.get(factionName) + 1);
					}
					if(!listBlocksBreakPlayers.containsKey(player)) {
						listBlocksBreakPlayers.put(player, 1);
					} else {
						listBlocksBreakPlayers.put(player, listBlocksBreakPlayers.get(player) + 1);
					}
					if(main.getLocationBlocksTotem().size() == 4) {
						for(String msg : main.getMessagesPublic("breakFirstBlock", player)) {
							Bukkit.broadcastMessage(main.getPrefix() + msg.replace("&", "§")
																		  .replace("<player>", player.getName())
																		  .replace("<faction>", factionName)
																		  .replace("<block>", main.getLocationBlocksTotem().size() + ""));
						}
						if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
							if(main.getScoreboardFeatherBoardStatus()) {
								if(!main.getFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
									main.getUtilsScorebordsManager().updateBlock(main.getLocationBlocksTotem().size());
									main.getUtilsScorebordsManager().updateFaction(factionName);
								}
							} else if(main.getScoreboardTotemFactionsStatus()) {
								main.getUtilsScorebordsManager().updateBlock(main.getLocationBlocksTotem().size());
								main.getUtilsScorebordsManager().updateFaction(factionName);
							}
						}
						if(main.getParticleStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								UParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0.02f, 20, block.getLocation().clone().add(0.5, 0.5, 0.5), players);
							}
						}
						if(main.getSoundStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								players.playSound(main.getCurrentTotemLocation(), Sound.FIREWORK_BLAST, 1f, 0.85f);
							}
						}
						main.setCurrentFactionName(factionName);
						block.setType(Material.AIR);
					} else if(main.getCurrentFactionName() != factionName) {
						for(String msg : main.getMessagesPublic("breakCounterFaction", player)) {
							Bukkit.broadcastMessage(main.getPrefix() + msg.replace("&", "§")
																		  .replace("<player>", player.getName())
																		  .replace("<factionBlock>", main.getCurrentFactionName()));
						}
						if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
							if(main.getScoreboardFeatherBoardStatus()) {
								if(!main.getFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
									main.getUtilsScorebordsManager().updateBlock(5);
									main.getUtilsScorebordsManager().updateFaction(null);
								}
							} else if(main.getScoreboardTotemFactionsStatus()) {
								main.getUtilsScorebordsManager().updateBlock(5);
								main.getUtilsScorebordsManager().updateFaction(null);
							}
						}
						if(main.getParticleStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								UParticleEffect.REDSTONE.display(0.8f, 1.5f, 0.8f, 0.2f, 150, main.getCurrentTotemLocation().clone().add(0.5, 1.5, 0.5), players);
							}
						}
						if(main.getSoundStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								players.playSound(main.getCurrentTotemLocation(), Sound.EXPLODE, 1f, 1.4f);
							}
						}
						if(main.getFireworksStatus()) {
							RFireworks start = new RFireworks();
							start.setLocation(main.getCurrentTotemLocation());
							start.runTaskTimer(main, 0, 20);
						}
						main.getUtilsPhysicBlocks().unBuildTotem(main.getCurrentTotemLocation());
						main.getUtilsPhysicBlocks().buildTotem(main.getCurrentTotemLocation());
					} else if(!main.getLocationBlocksTotem().isEmpty()) {
						for(String msg : main.getMessagesPublic("breakOtherBlock", player)) {
							Bukkit.broadcastMessage(main.getPrefix() + msg.replace("&", "§")
																		  .replace("<player>", player.getName())
																		  .replace("<block>", main.getLocationBlocksTotem().size() + ""));
						}
						if(main.getScoreboardTotemFactionsStatus() || main.getScoreboardFeatherBoardStatus()) {
							if(main.getScoreboardFeatherBoardStatus()) {
								if(!main.getFeatherBoardStatus() && main.getScoreboardTotemFactionsStatus()) {
									main.getUtilsScorebordsManager().updateBlock(main.getLocationBlocksTotem().size());
								}
							} else if(main.getScoreboardTotemFactionsStatus()) {
								main.getUtilsScorebordsManager().updateBlock(main.getLocationBlocksTotem().size());
							}
						}
						if(main.getParticleStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								UParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0.02f, 20, block.getLocation().clone().add(0.5, 0.5, 0.5), players);
							}
						}
						if(main.getSoundStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								players.playSound(main.getCurrentTotemLocation(), Sound.FIREWORK_BLAST, 1f, 0.85f);
							}
						}
						block.setType(Material.AIR);
					} else {
						int blockEvent = 0;
						for(Entry<String, Integer> informations : listFactions.entrySet()) {
							boolean oklm = false;
							if(informations.getKey() == factionName) {oklm = true; blockEvent = informations.getValue();}
							main.getUtilsRankFactions().updateFactionStats(informations.getKey(), informations.getValue(), oklm);
						}
						for(String msg : main.getMessagesPublic("winMessages", player)) {
							Bukkit.broadcastMessage(main.getPrefix() + msg.replace("&", "§")
																		  .replace("<player>", player.getName())
																		  .replace("<faction>", factionName)
																		  .replace("<totemName>", main.getCurrentTotemName())
																		  .replace("<wins>", "" + main.getUtilsRankFactions().getWinsFactions(factionName))
																		  .replace("<blocksEvent>", "" + blockEvent)
																		  .replace("<blocksAll>", "" + main.getUtilsRankFactions().getBlocksBreak(factionName)));
						}
						Bukkit.getPluginManager().callEvent(new TotemEndEvent(player, factionName, main.getCurrentTotemName(), main.getCurrentTotemLocation(), player, getPlayerBreakMostBlock()));
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
										main.setTotemCreateStatus(false);
									}
								} else if(main.getScoreboardTotemFactionsStatus()) {
									for(Player players : Bukkit.getOnlinePlayers()) {
										main.getUtilsScorebordsManager().updateScorboards(players, factionName, blockEvent, "post");
									}
									RRemoveScoreboard start = new RRemoveScoreboard();
									start.runTaskLater(main, 100);
								} else {
									main.setTotemCreateStatus(false);
								}
							} else if(main.getScoreboardTotemFactionsStatus()) {
								for(Player players : Bukkit.getOnlinePlayers()) {
									main.getUtilsScorebordsManager().updateScorboards(players, factionName, blockEvent, "post");
								}
								RRemoveScoreboard start = new RRemoveScoreboard();
								start.runTaskLater(main, 100);
							} else {
								main.setTotemCreateStatus(false);
							}
						}
						if(main.getParticleStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								UParticleEffect.FLAME.display(0.1f, 0.1f, 0.1f, 0.02f, 20, block.getLocation().clone().add(0.5, 0.5, 0.5), players);
								UParticleEffect.EXPLOSION_NORMAL.display(0.2f, 1.5f, 0.2f, 0.15f, 150, main.getCurrentTotemLocation().clone().add(0.5, 1.5, 0.5), players);
							}
						}
						if(main.getSoundStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								players.playSound(players.getLocation(), Sound.WITHER_DEATH, 0.4f, 1f);
							}
						}
						if(main.getFireworksStatus()) {
							RFireworksWin start = new RFireworksWin();
							start.setLocation(main.getCurrentTotemLocation());
							start.runTaskTimer(main, 0, 20);
						}
						if(main.getTitlesStatus()) {
							for(Player players : Bukkit.getOnlinePlayers()) {
								main.getUtilsTitles().sendTitle(players, 5, 30, 5, "", main.getMessagesTitles("winFaction").replace("&", "§").replace("<faction>", factionName));
							}
						}
						if(main.getRewardsPlayerBreakLastBlockStatus()) {
							for(String commandes : main.fileConfigConfig.getStringList("TotemConfigs.settings.rewards.player.breakLastBlock.commands")) {
								Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), commandes.replace("<player>", player.getName()).replace("<factionName>", factionName));
							}
						}
						if(main.getRewardsPlayerBreakMostBlockStatus()) {
							for(String commandes : main.fileConfigConfig.getStringList("TotemConfigs.settings.rewards.player.breakMostBlock.commands")) {
								Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), commandes.replace("<player>", getPlayerBreakMostBlock().getName()).replace("<factionName>", factionName));
							}
						}
						if(main.getRewardsFactionStatus()) {
							for(String commandes : main.fileConfigConfig.getStringList("TotemConfigs.settings.rewards.faction.commands")) {
								try {
									Method method = Faction.class.getDeclaredMethod("getOnlinePlayers", new Class[]{});
									Object result = method.invoke(data.getKey(), new Object[]{});
									@SuppressWarnings("unchecked")
									List<Player> list = (List<Player>)result;
									for(Player playerName : list) {
										Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), commandes.replace("<player>", playerName.getName()));
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						listFactions.clear();
						listBlocksBreakPlayers.clear();
						main.getUtilsPhysicBlocks().unBuildTotem(main.getCurrentTotemLocation());
						main.setTotemCreateStatus(false);
					}
				}
			}
		} catch (Exception e) {e.printStackTrace(); }
	}
	
	private Player getPlayerBreakMostBlock() {
		UValueComparator comparateur = new UValueComparator(listBlocksBreakPlayers);
		TreeMap<Player, Integer> mapTriee = new TreeMap<Player, Integer>(comparateur);
		mapTriee.putAll(listBlocksBreakPlayers);
		return mapTriee.firstKey();
	}
	
}
