package com.mineaurion.catchall.bukkit.events;

import com.mineaurion.catchall.bukkit.CatchAll;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerJoinEvent implements Listener {
    private final CatchAll main;

    public OnPlayerJoinEvent() {
        this.main = CatchAll.getInstance();
    }

    @EventHandler
    public void handleJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            this.handleEvent(player);
            event.setJoinMessage(ChatColor.DARK_PURPLE + "+" + event.getPlayer().getName());
        } catch (Exception e){
            player.kickPlayer(e.getMessage());
        }
    }

    @EventHandler
    public void handleLoginEvent(PlayerLoginEvent event){
        try {
            this.handleEvent(event.getPlayer());
        } catch (Exception e) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, e.getMessage());
        }
    }


    private void handleEvent(Player player) throws Exception {
        int maxPlayers = Bukkit.getServer().getMaxPlayers();
        int onlineCount = Bukkit.getOnlinePlayers().size();

        if(onlineCount >= maxPlayers - 5){
            if(!player.hasPermission("mineaurion.donateur")){
                throw new Exception("Last slots are reserved to donators (and more) players");
            }
        }

        boolean maintenance_state = this.main.getConfig().getBoolean("states.maintenance");
        boolean donateur_state = this.main.getConfig().getBoolean("states.donateur");

        if (maintenance_state) {
            if(!player.hasPermission("maintenance.bypass")){
                throw new Exception("Server is now in maintenance. Try again later please");
            }
        }

        if(donateur_state){
            if(!player.hasPermission("minaurion.donateur")){
                throw new Exception("Server is now in donator only mode. Try later please");
            }
        }
    }
}