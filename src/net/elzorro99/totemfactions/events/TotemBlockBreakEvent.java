package net.elzorro99.totemfactions.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TotemBlockBreakEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	private String factionName;
	private String totemName;
	private Location totemLocation;
	private Integer totemRemainingBlock;
	private Block blockBreakLocation;
	private Player playerBlockBreak;

	public TotemBlockBreakEvent(Player who, String factionName, String totemName, Location totemLocation, Integer totemRemainingBlock, Block blockBreakLocation, Player playerBlockBreak) {
		super(who);
		this.factionName = factionName;
		this.totemName = totemName;
		this.totemLocation = totemLocation;
		this.totemRemainingBlock = totemRemainingBlock;
		this.blockBreakLocation = blockBreakLocation;
		this.playerBlockBreak = playerBlockBreak;
	}
	
	public String getFactionName() {
		return factionName;
	}
	
	public String getTotemName() {
		return totemName;
	}
	
	public Location getTotemLocation() {
		return totemLocation;
	}
	
	public Integer getTotemRemainingBlock() {
		return totemRemainingBlock;
	}
	
	public Block getBlockBreakLocation() {
		return blockBreakLocation;
	}
	
	public Player getPlayerBlockBreak() {
		return playerBlockBreak;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
