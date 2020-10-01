package net.elzorro99.totemfactions.listeners.packets;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.LBlockBreakFaction;
import net.elzorro99.totemfactions.utils.UPacketsInjector;
import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Material;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayOutBlockChange;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;

public class v1_7_R4 implements PInjector{
	
	private static Main main = Main.getInstance();
	
	public v1_7_R4(){}
	
	public void inject(Player player){
		ChannelDuplexHandler handler;
		EntityPlayer player_1_7 = ((CraftPlayer)player).getHandle();
		handler = new ChannelDuplexHandler(){
			public void channelRead(ChannelHandlerContext channel, Object obj) throws Exception {
				if(!main.getTotemSpawnStatus()){
					super.channelRead(channel, obj);
					return;
				}
				if(obj instanceof PacketPlayInBlockDig){
					PacketPlayInBlockDig packet = (PacketPlayInBlockDig)obj;
					List<Location> list = main.getLocationBlocksTotem();
					for(Location loc : list){
						if(player.getWorld().equals(loc.getWorld())){
							if(packet.c() == loc.getBlockX()){
								if(packet.d() == loc.getBlockY()){
									if(packet.e() == loc.getBlockZ()){
										switch(packet.g()){
											case 0:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_7.playerInteractManager;
														if(manager.isCreative()){
															if (!player_1_7.world.douseFire(null, packet.c(), packet.d(), packet.e() , packet.f())) LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
															player_1_7.playerConnection.sendPacket(new PacketPlayOutBlockChange(packet.c(), packet.d(), packet.e(), manager.world)); 
														}else{
															Block block = manager.world.getType(packet.c(), packet.d(), packet.e());
															UPacketsInjector.input(PlayerInteractManager.class, "lastDigTick", manager, UPacketsInjector.reflect(PlayerInteractManager.class, "currentTick", manager, 0));
															float f = 1.0F;
															if(block.getMaterial() != Material.AIR) {
																block.attack(manager.world, packet.c(), packet.d(), packet.e(), player_1_7);
																f = block.getDamage(player_1_7, player_1_7.world, packet.c(), packet.d(), packet.e());
																manager.world.douseFire(null, packet.c(), packet.d(), packet.e(), packet.f());
															}
															if (block.getMaterial() != Material.AIR && f >= 1.0F) {
																LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
															} else {
																int i = (int)(f * 10.0F);
														        manager.world.d(player_1_7.getId(), packet.c(), packet.d(), packet.e(), i);
																UPacketsInjector.input(PlayerInteractManager.class, "d", manager, true);
																UPacketsInjector.input(PlayerInteractManager.class, "f", manager, packet.c());
																UPacketsInjector.input(PlayerInteractManager.class, "g", manager, packet.d());
																UPacketsInjector.input(PlayerInteractManager.class, "h", manager, packet.e());
														        UPacketsInjector.input(PlayerInteractManager.class, "o", manager, i);
														      } 
														}
													}
												}.runTask(main);
												return;
											case 1:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_7.playerInteractManager;
														UPacketsInjector.input(PlayerInteractManager.class, "d", manager, false);
													    manager.world.d(player_1_7.getId(), packet.c(), packet.d(), packet.e(), -1);
													}
												}.runTask(main);
												return;
											case 2:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_7.playerInteractManager;
														if(packet.c() == (int)UPacketsInjector.reflect("f", manager, 0) && packet.d() == (int)UPacketsInjector.reflect("g", manager, 0) && packet.e() == (int)UPacketsInjector.reflect("h", manager, 0)){
															int currentTick;
															UPacketsInjector.input(PlayerInteractManager.class, "currentTick", manager, (currentTick = MinecraftServer.currentTick));
															int lastDigTick;
															int i = currentTick - (lastDigTick = ((int)UPacketsInjector.reflect(PlayerInteractManager.class, "lastDigTick", manager, 0)));
															Block block = manager.world.getType(packet.c(), packet.d(), packet.e());
															if (block.getMaterial() != Material.AIR) {
																float f = block.getDamage(manager.player, manager.player.world, packet.c(), packet.d(), packet.e()) * (i + 1);
																if (f >= 0.7F) {
																	UPacketsInjector.reflect(PlayerInteractManager.class, "d", manager, false);
																	manager.world.d(manager.player.getId(), packet.c(), packet.d(), packet.e(), -1);
																	LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
																} else if (!((boolean)UPacketsInjector.reflect(PlayerInteractManager.class, "j", manager, false))) {
																	UPacketsInjector.input(PlayerInteractManager.class, "d", manager, false);
																	UPacketsInjector.input(PlayerInteractManager.class, "j", manager, true);
																	UPacketsInjector.input(PlayerInteractManager.class, "k", manager, packet.c());
																	UPacketsInjector.input(PlayerInteractManager.class, "l", manager, packet.d());
																	UPacketsInjector.input(PlayerInteractManager.class, "m", manager, packet.e());
																	UPacketsInjector.input(PlayerInteractManager.class, "n", manager, lastDigTick);
														        } 
															} 
														}
														player_1_7.playerConnection.sendPacket(new PacketPlayOutBlockChange(packet.c(), packet.d(), packet.e(), manager.world));
													}
												}.runTask(main);
												return;
											default: break;
										}
									}
								}
							}
						}
					}
				}
				super.channelRead(channel, obj);
			}
		};
		try{
			Channel channel = (Channel) UPacketsInjector.reflect("m", ((CraftPlayer)player).getHandle().playerConnection.networkManager, null);
			try {channel.pipeline().remove("totem_handler");} catch (Exception e) {}
			channel.pipeline().addBefore("packet_handler", "totem_handler", handler);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
