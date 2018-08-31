package com.mineaurion.Bukkit.event;


import java.util.Collection;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mineaurion.Bukkit.Main;

public class EventManager implements Listener {
	
	int donateurConnected = 0;
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerLogin(final PlayerLoginEvent event) {
		if (Main.getInstance().getConfig().getBoolean("Maintenance") && !event.getPlayer().isOp() && !event.getPlayer().hasPermission("Maintenance.bypass")) {
			event.getPlayer().getServer().broadcastMessage(event.getPlayer().getName() +" essaye de se connecter");
			event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Maintenance du serveur");
			return;
		}else if(Main.getInstance().getConfig().getBoolean("AccesDonateur") && !event.getPlayer().isOp() && !event.getPlayer().hasPermission("mineaurion.donateur")) {
			event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "server reserved to donator");
			return;
		}
		int reservedSlot = (Main.getInstance().getConfig().getInt("reservedSlot")-donateurConnected);
		reservedSlot = reservedSlot<0 ? 0 : reservedSlot;
		int onlinePlayer = Main.getInstance().getServer().getOnlinePlayers().size();
		if(Main.getInstance().getServer().getMaxPlayers()-(reservedSlot)<= onlinePlayer ) {
			if(!event.getPlayer().hasPermission("mineaurion.donateur")) {
				event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You need to be donator to connect");
				return;
			}else {
				if(Main.getInstance().getServer().getMaxPlayers()==onlinePlayer ) {
					while (true) {
						Player p = (Player)getRandomObject(Main.getInstance().getServer().getOnlinePlayers());
						if(!p.hasPermission("mineaurion.donateur")) {
							p.kickPlayer("Nous sommes désolé le serveur est plein, et un donateur veux ce connecter. Achete un grade pour ne plus être kick");
							break;
						}
					}
				}
			}
		}

		if(event.getPlayer().hasPermission("mineaurion.donateur")) {
			donateurConnected++;
		}
		
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		if(event.getPlayer().hasPermission("mineaurion.donateur")) {
			donateurConnected--;
		}
	}
	

	private Object getRandomObject(Collection<?> from) {
		   Random rnd = new Random();
		   int i = rnd.nextInt(from.size());
		   return from.toArray()[i];
		}
 
}
