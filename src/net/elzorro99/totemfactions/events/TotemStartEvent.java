package net.elzorro99.totemfactions.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TotemStartEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private String totemName;
	private Location totemLocation;

	public TotemStartEvent(String totemName, Location totemLocation) {
		this.totemName = totemName;
		this.totemLocation = totemLocation;
	}
	
	public String getTotemName() {
		return totemName;
	}
	
	public Location getTotemLocation() {
		return totemLocation;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}


}
