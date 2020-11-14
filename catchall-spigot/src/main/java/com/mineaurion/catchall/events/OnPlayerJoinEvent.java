package com.mineaurion.catchall.events;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerJoinEvent implements Listener {
    private CatchAllSpigot main;

    public OnPlayerJoinEvent() {
        this.main = CatchAllSpigot.getInstance();
    }

    @EventHandler
    public void Handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        int citizenMax = Bukkit.getServer().getMaxPlayers();
        int onlineCount = Bukkit.getOnlinePlayers().size();

        if (onlineCount >= citizenMax - 5)
            if (!player.hasPermission("mineaurion.donateur")) {
                player.kickPlayer("Last slots are reserved to donators (and more) players");
                return;
            }

        boolean maintenance_state = this.main.getConfig().getBoolean("states.maintenance");
        boolean donateur_state = this.main.getConfig().getBoolean("states.donateur");

        if (maintenance_state)
            if (!player.hasPermission("Maintenance.bypass")) {
                player.kickPlayer("Server is now in maintenance. Try later please");
                return;
            }
        if (donateur_state)
            if (!player.hasPermission("mineaurion.donateur")) {
                player.kickPlayer("Server is now in donator only mode. Try later please");
                return;
            }

        event.setJoinMessage(ChatColor.DARK_PURPLE + "+" + event.getPlayer().getName());

    }
}