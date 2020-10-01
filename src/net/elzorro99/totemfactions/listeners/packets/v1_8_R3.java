package net.elzorro99.totemfactions.listeners.packets;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.LBlockBreakFaction;
import net.elzorro99.totemfactions.utils.UPacketsInjector;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldSettings;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class v1_8_R3 implements PInjector{
	
	private static Main main = Main.getInstance();
	
	public v1_8_R3(){}
	
	public void inject(Player player){
		ChannelDuplexHandler handler;
		EntityPlayer player_1_8 = ((CraftPlayer)player).getHandle();
		handler = new ChannelDuplexHandler(){
			public void channelRead(ChannelHandlerContext channel, Object obj) throws Exception {
				if(!main.getTotemSpawnStatus()){
					super.channelRead(channel, obj);
					return;
				}
				if(obj instanceof PacketPlayInBlockDig){
					PacketPlayInBlockDig packet = (PacketPlayInBlockDig)obj;
					BlockPosition position = packet.a();
					List<Location> list = main.getLocationBlocksTotem();
					for(Location loc : list){
						if(player.getWorld().equals(loc.getWorld())){
							if(position.getX() == loc.getBlockX()){
								if(position.getY() == loc.getBlockY()){
									if(position.getZ() == loc.getBlockZ()){
										switch(packet.c().ordinal()){
											case 0:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_8.playerInteractManager;
														if(manager.isCreative()){
															if (!player_1_8.world.douseFire(null, position, packet.b())){
																LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
																player_1_8.playerConnection.sendPacket(new PacketPlayOutBlockChange(manager.world, position)); 
															}
														}else{
															Block block = getBlock(loc, position);
															WorldSettings.EnumGamemode gamemode = (WorldSettings.EnumGamemode)UPacketsInjector.reflect(PlayerInteractManager.class, "gamemode", manager, EnumGamemode.NOT_SET);
															if (gamemode.c()){
														        if (gamemode == WorldSettings.EnumGamemode.SPECTATOR) return; 
														        if(!player_1_8.cn()) {
														       		ItemStack itemstack = player_1_8.bZ();
														       		if (itemstack == null) return; 
														       		if (!itemstack.c(block)) return; 
														       	} 
															}
															UPacketsInjector.input(PlayerInteractManager.class, "lastDigTick", manager, UPacketsInjector.reflect(PlayerInteractManager.class, "currentTick", manager, 0));
															float f = 1.0F;
															if(block.getMaterial() != Material.AIR) {
																block.attack(manager.world, position, player_1_8);
																f = block.getDamage(player_1_8, player_1_8.world, position);
																manager.world.douseFire(null, position, packet.b());
															}
															if (block.getMaterial() != Material.AIR && f >= 1.0F) {
																LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
															} else {
																UPacketsInjector.input(PlayerInteractManager.class, "d", manager, true);
																UPacketsInjector.input(PlayerInteractManager.class, "f", manager, position);
														        int i = (int)(f * 10.0F);
														        manager.world.c(player_1_8.getId(), position, i);
														        UPacketsInjector.input(PlayerInteractManager.class, "k", manager, i);
														      } 
														}
													}
												}.runTask(main);
												return;
											case 1:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_8.playerInteractManager;
														UPacketsInjector.input(PlayerInteractManager.class, "d", manager, false);
													    manager.world.c(player_1_8.getId(), position, -1);
													}
												}.runTask(main);
												return;
											case 2:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_8.playerInteractManager;
														if(position.equals(UPacketsInjector.reflect(PlayerInteractManager.class, "f", manager, BlockPosition.ZERO))){
															int currentTick;
															UPacketsInjector.input(PlayerInteractManager.class, "currentTick", manager, (currentTick = MinecraftServer.currentTick));
															int lastDigTick;
															int i = currentTick - (lastDigTick = ((int)UPacketsInjector.reflect(PlayerInteractManager.class, "lastDigTick", manager, 0)));
															Block block = getBlock(loc, position);
															if (block.getMaterial() != Material.AIR) {
																float f = block.getDamage(manager.player, manager.player.world, position) * (i + 1);
																if (f >= 0.7F) {
																	UPacketsInjector.reflect(PlayerInteractManager.class, "d", manager, false);
																	manager.world.c(manager.player.getId(), position, -1);
																	LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
																} else if (!((boolean)UPacketsInjector.reflect(PlayerInteractManager.class, "h", manager, false))) {
																	UPacketsInjector.input(PlayerInteractManager.class, "d", manager, false);
																	UPacketsInjector.input(PlayerInteractManager.class, "h", manager, true);
																	UPacketsInjector.input(PlayerInteractManager.class, "i", manager, position);
																	UPacketsInjector.input(PlayerInteractManager.class, "j", manager, lastDigTick);
														        } 
															}
															
														}
														player_1_8.playerConnection.sendPacket(new PacketPlayOutBlockChange(manager.world, position)); 
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
	
	public static Block getBlock(Location loc, BlockPosition block){
		return ((CraftWorld)loc.getWorld()).getHandle().getType(block).getBlock();
	}
}
