package net.elzorro99.totemfactions.listeners.packets;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.LBlockBreakFaction;
import net.elzorro99.totemfactions.utils.UPacketsInjector;
import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.EnumGamemode;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.Material;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_10_R1.PacketPlayOutBlockChange;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;

public class v1_10_R1 implements PInjector{
	
	private static Main main = Main.getInstance();
	
	public v1_10_R1(){}
	
	public void inject(Player player){
		ChannelDuplexHandler handler;
		EntityPlayer player_1_10 = ((CraftPlayer)player).getHandle();
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
														PlayerInteractManager manager = player_1_10.playerInteractManager;
														if(manager.isCreative()){
															if (!player_1_10.world.douseFire(null, position, packet.b())) LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
															player_1_10.playerConnection.sendPacket(new PacketPlayOutBlockChange(manager.world, position)); 
														}else{
															IBlockData iblockdata = manager.world.getType(position);
														    Block block = iblockdata.getBlock();
															EnumGamemode gamemode = (EnumGamemode)UPacketsInjector.reflect(PlayerInteractManager.class, "gamemode", manager, EnumGamemode.NOT_SET);
															if (gamemode.c()){
														        if (gamemode == EnumGamemode.SPECTATOR) return; 
														        if(!player_1_10.cZ()) {
														       		ItemStack itemstack = player_1_10.getItemInMainHand();
														       		if (itemstack == null) return; 
														       		if (!itemstack.a(block)) return; 
														       	} 
															}
															UPacketsInjector.input(PlayerInteractManager.class, "lastDigTick", manager, UPacketsInjector.reflect(PlayerInteractManager.class, "currentTick", manager, 0));
															float f = 1.0F;
															if(block.getBlockData().getMaterial() != Material.AIR) {
																block.attack(manager.world, position, player_1_10);
																f = iblockdata.a(player_1_10, player_1_10.world, position);
																manager.world.douseFire(null, position, packet.b());
															}
															if (block.getBlockData().getMaterial() != Material.AIR && f >= 1.0F) {
																LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
															} else {
																UPacketsInjector.input(PlayerInteractManager.class, "d", manager, true);
																UPacketsInjector.input(PlayerInteractManager.class, "f", manager, position);
														        int i = (int)(f * 10.0F);
														        manager.world.c(player_1_10.getId(), position, i);
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
														PlayerInteractManager manager = player_1_10.playerInteractManager;
														UPacketsInjector.input(PlayerInteractManager.class, "d", manager, false);
													    manager.world.c(player_1_10.getId(), position, -1);
													}
												}.runTask(main);
												return;
											case 2:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_10.playerInteractManager;
														if(position.equals(UPacketsInjector.reflect(PlayerInteractManager.class, "f", manager, BlockPosition.ZERO))){
															int currentTick;
															UPacketsInjector.input(PlayerInteractManager.class, "currentTick", manager, (currentTick = MinecraftServer.currentTick));
															int lastDigTick;
															int i = currentTick - (lastDigTick = ((int)UPacketsInjector.reflect(PlayerInteractManager.class, "lastDigTick", manager, 0)));
															IBlockData iblockdata = manager.world.getType(position);
														    Block block = iblockdata.getBlock();
															if (block.getBlockData().getMaterial() != Material.AIR) {
																float f = iblockdata.a(manager.player, manager.player.world, position) * (i + 1);
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
														player_1_10.playerConnection.sendPacket(new PacketPlayOutBlockChange(manager.world, position)); 
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
