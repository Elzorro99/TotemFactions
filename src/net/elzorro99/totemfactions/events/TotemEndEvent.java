package net.elzorro99.totemfactions.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TotemEndEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	private String factionWinName;
	private String totemName;
	private Location totemLocation;
	private Player playerBreakLastBlock;
	private Player playerBreakMostBlock;

	public TotemEndEvent(Player who, String factionWinName, String totemName, Location totemLocation, Player playerBreakLastBlock, Player playerBreakMostBlock) {
		super(who);
		this.factionWinName = factionWinName;
		this.totemName = totemName;
		this.totemLocation = totemLocation;
		this.playerBreakLastBlock = playerBreakLastBlock;
		this.playerBreakMostBlock = playerBreakMostBlock;
	}
	
	public String getFactionWinName() {
		return factionWinName;
	}
	
	public String getTotemName() {
		return totemName;
	}
	
	public Location getTotemLocation() {
		return totemLocation;
	}
	
	public Player getPlayerBreakLastBlock() {
		return playerBreakLastBlock;
	}
	
	public Player getPlayerBreakMostBlock() {
		return playerBreakMostBlock;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
