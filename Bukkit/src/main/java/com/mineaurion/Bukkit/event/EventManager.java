package com.mineaurion.Bukkit.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.mineaurion.Bukkit.Main;

public class EventManager implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		if (Main.getInstance().getConfig().getBoolean("Maintenance") && !event.getPlayer().isOp() && !event.getPlayer().hasPermission("Maintenance.bypass")) {
			event.getPlayer().getServer().broadcastMessage(event.getPlayer().getName() +" essaye de se connecter");
			event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Maintenance du serveur");
		}
	}
 
}
