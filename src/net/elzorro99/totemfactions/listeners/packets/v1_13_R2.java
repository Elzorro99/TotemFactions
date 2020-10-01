package net.elzorro99.totemfactions.listeners.packets;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.elzorro99.totemfactions.Main;
import net.elzorro99.totemfactions.listeners.LBlockBreakFaction;
import net.elzorro99.totemfactions.utils.UPacketsInjector;
import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumGamemode;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PacketPlayInBlockDig;
import net.minecraft.server.v1_13_R2.PacketPlayOutBlockChange;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import net.minecraft.server.v1_13_R2.ShapeDetectorBlock;

public class v1_13_R2 implements PInjector{
	
	private static Main main = Main.getInstance();
	
	public v1_13_R2(){}
	
	public void inject(Player player){
		ChannelDuplexHandler handler;
		EntityPlayer player_1_13 = ((CraftPlayer)player).getHandle();
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
														PlayerInteractManager manager = player_1_13.playerInteractManager;
														if(manager.isCreative()){
															if (!player_1_13.world.douseFire(null, position, packet.c())) LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
															player_1_13.playerConnection.sendPacket(new PacketPlayOutBlockChange(manager.world, position)); 
														}else{
															Block block = getBlock(loc, position);
															EnumGamemode gamemode = (EnumGamemode)UPacketsInjector.reflect(PlayerInteractManager.class, "gamemode", manager, EnumGamemode.NOT_SET);
															if (gamemode.d()){
														        if (gamemode == EnumGamemode.SPECTATOR) return; 
														        if(!player_1_13.dy()) {
														        	ItemStack itemstack = player_1_13.getItemInMainHand();
														            if (itemstack.isEmpty())
														              return; 
														            ShapeDetectorBlock shapedetectorblock = new ShapeDetectorBlock(manager.world, position, false);
														            if (!itemstack.a(manager.world.F(), shapedetectorblock))
														              return; 
														       	} 
															}
															UPacketsInjector.input(PlayerInteractManager.class, "lastDigTick", manager, UPacketsInjector.reflect(PlayerInteractManager.class, "currentTick", manager, 0));
															float f = 1.0F;
															IBlockData data = block.getBlockData();
															if(!data.isAir()) {
																data.attack(manager.world, position, player_1_13);
																f = data.getDamage(player_1_13, player_1_13.world, position);
																manager.world.douseFire(null, position, packet.c());
															}
															if( !block.getBlockData().isAir() && f >= 1.0F) {
																LBlockBreakFaction.instance.onBreakBlock(new BlockBreakEvent(loc.getBlock(), player));
															} else {
																UPacketsInjector.input(PlayerInteractManager.class, "d", manager, true);
																UPacketsInjector.input(PlayerInteractManager.class, "f", manager, position);
														        int i = (int)(f * 10.0F);
														        manager.world.c(player_1_13.getId(), position, i);
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
														PlayerInteractManager manager = player_1_13.playerInteractManager;
														UPacketsInjector.input(PlayerInteractManager.class, "d", manager, false);
													    manager.world.c(player_1_13.getId(), position, -1);
													}
												}.runTask(main);
												return;
											case 2:
												new BukkitRunnable(){
													@Override
													public void run(){
														PlayerInteractManager manager = player_1_13.playerInteractManager;
														if(position.equals(UPacketsInjector.reflect(PlayerInteractManager.class, "f", manager, BlockPosition.ZERO))){
															int currentTick;
															UPacketsInjector.input(PlayerInteractManager.class, "currentTick", manager, (currentTick = MinecraftServer.currentTick));
															int lastDigTick;
															int i = currentTick - (lastDigTick = ((int)UPacketsInjector.reflect(PlayerInteractManager.class, "lastDigTick", manager, 0)));
															Block block = getBlock(loc, position);
															IBlockData data = block.getBlockData();
															if (!data.isAir()) {
																float f = data.getDamage(manager.player, manager.player.world, position) * (i + 1);
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
														player_1_13.playerConnection.sendPacket(new PacketPlayOutBlockChange(manager.world, position));
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
