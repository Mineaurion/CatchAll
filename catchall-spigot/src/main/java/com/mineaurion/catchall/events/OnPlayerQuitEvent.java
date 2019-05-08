package com.mineaurion.catchall.events;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {
    private CatchAllSpigot main;

    public OnPlayerQuitEvent() {
        this.main = CatchAllSpigot.getInstance();
    }

    @EventHandler
    public void Handle(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.DARK_BLUE + "-" + event.getPlayer().getName());
    }
}