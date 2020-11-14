package com.mineaurion.catchall.events;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerLoginEvent implements Listener {
    private CatchAllSpigot main;

    public OnPlayerLoginEvent() {
        this.main = CatchAllSpigot.getInstance();
    }

    @EventHandler
    public void Handle(PlayerLoginEvent event){
        Player player = event.getPlayer();

        int citizenMax = Bukkit.getServer().getMaxPlayers();
        int onlineCount = Bukkit.getOnlinePlayers().size();

        if (onlineCount >= citizenMax - 5)
            if (!player.hasPermission("mineaurion.donateur"))
                event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Last slots are reserved to donators (and more) players");

        boolean maintenance_state = this.main.getConfig().getBoolean("states.maintenance");
        boolean donateur_state = this.main.getConfig().getBoolean("states.donateur");

        if (maintenance_state)
            if (!player.hasPermission("Maintenance.bypass"))
                event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Server is now in maintenance. Try later please");
        if (donateur_state)
            if (!player.hasPermission("mineaurion.donateur"))
                event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Server is now in donator only mode. Try later please");
    }

}
