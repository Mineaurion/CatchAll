package com.mineaurion.catchall.bukkit.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    @EventHandler
    public void Handle(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.DARK_BLUE + "-" + event.getPlayer().getName());
    }
}