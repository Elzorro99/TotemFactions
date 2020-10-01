package net.elzorro99.totemfactions.runnable;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class RFireworks extends BukkitRunnable {
	
	private Location location = null;
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public void run() {
		Firework fireworks = location.getWorld().spawn(location.clone().add(0.5, 5, 0.5), Firework.class);
		FireworkMeta fireworksMeta = fireworks.getFireworkMeta();
		fireworksMeta.addEffect(FireworkEffect.builder().flicker(false)
														.trail(true)
														.with(FireworkEffect.Type.BALL_LARGE)
														.withColor(Color.BLUE)
														.withColor(Color.PURPLE)
														.withFade(Color.RED)
														.build());
		fireworksMeta.setPower(0);
		fireworks.setFireworkMeta(fireworksMeta);
		cancel();
		return;
	}
	
}
