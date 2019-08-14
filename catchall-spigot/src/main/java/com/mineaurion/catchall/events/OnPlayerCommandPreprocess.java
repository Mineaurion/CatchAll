package com.mineaurion.catchall.events;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.sql.SQLException;

public class OnPlayerCommandPreprocess implements Listener {
    CatchAllSpigot plugin;

    public OnPlayerCommandPreprocess() {
        plugin = CatchAllSpigot.getInstance();
    }

    @EventHandler
    public void handler(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/op")
                && event.getPlayer() != null) {
            event.getPlayer().sendMessage("Unknow command. Type \"/help\" for help.");
            event.setCancelled(true);
        }
    }
}