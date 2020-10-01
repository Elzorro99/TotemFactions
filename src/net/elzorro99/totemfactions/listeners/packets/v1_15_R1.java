package net.elzorro99.totemfactions.listeners.packets;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.LBlockBreakFaction;
import net.elzorro99.totemfactions.utils.UPacketsInjector;
import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EnumDirection;
import net.minecraft.server.v1_15_R1.EnumGamemode;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_15_R1.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.server.v1_15_R1.PacketPlayOutBlockBreak;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;

public class v1_15_R1 implements PInjector{
	
	private static Main main = Main.getInstance();
	
	public v1_15_R1(){}
	
	public void inject(Player player){
		ChannelDuplexHandler handler;
		EntityPlayer player_1_15 = ((CraftPlayer)player).getHandle();
		handler = new ChannelDuplexHandler(){
			public void channelRead(ChannelHandlerContext channel, Object obj) throws Exception {
				if(!main.getTotemSpawnStatus()){
					super.channelRead(channel, obj);
					return;
				}
				if(obj instanceof PacketPlayInBlockDig){
					PacketPlayInBlockDig packet = (PacketPlayInBlockDig)obj;
					BlockPosition position = packet.b();
					List<Location> list = main.getLocationBlocksTotem();
					for(Location loc : list){
						if(player.getWorld().equals(loc.getWorld())){
							if(position.getX() == loc.getBlockX()){
								if(position.getY() == loc.getBlockY()){
									if(position.getZ() == loc.getBlockZ()){
										switch(packet.d().ordinal()){
											case 0: 
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_15.playerInteractManager;
														float f = default_method(manager, position, packet.c(), packet.d(), player_1_15);
														IBlockData iblockdata = getBlock(loc, position).getBlockData();
														if (!iblockdata.isAir() && f >= 1.0F) {
															LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
														} else {
													        if ((boolean)UPacketsInjector.reflect("e", manager,false)){
													        	BlockPosition g = (BlockPosition)UPacketsInjector.reflect("g", manager, BlockPosition.ZERO);
													        	player_1_15.playerConnection.sendPacket(new PacketPlayOutBlockBreak(g, manager.world.getType(g), PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK, false, "abort destroying since another started (client insta mine, server disagreed)")); 
													        }
													        UPacketsInjector.input(PlayerInteractManager.class, "e", manager, true);
													        UPacketsInjector.input(PlayerInteractManager.class, "g", manager, position.immutableCopy());
													        UPacketsInjector.input(PlayerInteractManager.class, "j", manager, (int)(f*10.0F));
													        manager.world.a(player_1_15.getId(), position, (int)(f*10.0F));
													        player_1_15.playerConnection.sendPacket(new PacketPlayOutBlockBreak(position, manager.world.getType(position), packet.d(), true, "actual start of destroying"));
													        UPacketsInjector.input(PlayerInteractManager.class, "l", manager, UPacketsInjector.reflect("j", manager, (int)(f*10.0F)));
													      } 
													}
												}.runTask(main);
												return;
											case 1:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_15.playerInteractManager;
														default_method(manager, position, packet.c(), packet.d(), player_1_15);
														UPacketsInjector.input(PlayerInteractManager.class, "e", manager, false);
													    manager.world.a(player_1_15.getId(), position, -1);
													    player_1_15.playerConnection.sendPacket(new PacketPlayOutBlockBreak(position, manager.world.getType(position), packet.d(), true, "block action restricted"));
													}
												}.runTask(main);
												return;
											case 2:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_15.playerInteractManager;
														default_method(manager, position, packet.c(), packet.d(), player_1_15);
														IBlockData iblockdata = getBlock(loc, position).getBlockData();
														if(position.equals(UPacketsInjector.reflect(PlayerInteractManager.class, "g", manager, BlockPosition.ZERO))){
															int lastDigTick = ((int)UPacketsInjector.reflect(PlayerInteractManager.class, "lastDigTick", manager, 0));
															int k = ((int)UPacketsInjector.reflect(PlayerInteractManager.class, "currentTick", manager, 0)) - lastDigTick;
															if (!iblockdata.isAir()) {
																float f1 = iblockdata.getDamage(player_1_15, player_1_15.world, position) * (k + 1);
																if (f1 >= 0.7F) {
																	UPacketsInjector.input(PlayerInteractManager.class,"e", manager, false);
																	manager.world.a(player_1_15.getId(), position, -1);
																	manager.a(position, packet.d(),"destroyed");
																	LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
																} 
																if (!((boolean)UPacketsInjector.reflect(PlayerInteractManager.class, "i", manager, false))) {
																	UPacketsInjector.input(PlayerInteractManager.class,"e", manager, false);
																	UPacketsInjector.input(PlayerInteractManager.class,"i", manager, true);
																	UPacketsInjector.input(PlayerInteractManager.class,"j", manager, position);
																	UPacketsInjector.input(PlayerInteractManager.class,"k", manager, lastDigTick);
																}
															} 
														} 
														player_1_15.playerConnection.sendPacket(new PacketPlayOutBlockBreak(position,iblockdata, packet.d(), true,"block action restricted"));
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
		try {((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline().remove("totem_handler");} catch (Exception e) {}
		((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline().addBefore("packet_handler", "totem_handler", handler);
	}
	
	private static float default_method(PlayerInteractManager manager, BlockPosition blockposition, EnumDirection enumdirection, EnumPlayerDigType packetplayinblockdig_enumplayerdigtype, EntityPlayer player){
		float f = 1.0F;
		if (manager.isCreative()) {
	        if (!manager.world.douseFire(null, blockposition, enumdirection)) {
	          manager.a(blockposition, packetplayinblockdig_enumplayerdigtype, "creative destroy");
	        } else {
	          player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, manager.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, true, "block action restricted"));
	        } 
	        return f;
	      } 
	      if (player.a(manager.world, blockposition, ((EnumGamemode)UPacketsInjector.reflect(PlayerInteractManager.class,"gamemode", manager, EnumGamemode.NOT_SET)))) {
	        player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, manager.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, false, "block action restricted"));
	        return f;
	      }
	      UPacketsInjector.input(PlayerInteractManager.class, "lastDigTick", manager, UPacketsInjector.reflect(PlayerInteractManager.class, "currentTick", manager, 0));
	      IBlockData iblockdata = manager.world.getType(blockposition);
	      if (!iblockdata.isAir()) {
	        iblockdata.attack(manager.world, blockposition, player);
	        f = iblockdata.getDamage(player, player.world, blockposition);
	        manager.world.douseFire(null, blockposition, enumdirection);
	      } 
	      return f;
	}
	
	public static Block getBlock(Location loc, BlockPosition block){
		return ((CraftWorld)loc.getWorld()).getHandle().getType(block).getBlock();
	}
}
