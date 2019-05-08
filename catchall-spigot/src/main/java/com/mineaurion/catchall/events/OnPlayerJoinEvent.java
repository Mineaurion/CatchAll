package com.mineaurion.catchall.events;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinEvent implements Listener {
    private CatchAllSpigot main;

    public OnPlayerJoinEvent() {
        this.main = CatchAllSpigot.getInstance();
    }

    @EventHandler
    public void Handle(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.DARK_PURPLE + "+" + event.getPlayer().getName());
    }
}