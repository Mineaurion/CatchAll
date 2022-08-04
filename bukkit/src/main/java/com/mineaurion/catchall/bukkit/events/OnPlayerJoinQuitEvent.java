package com.mineaurion.catchall.bukkit.events;

import com.mineaurion.catchall.bukkit.CatchAll;
import com.mineaurion.catchall.common.Config;
import com.mineaurion.catchall.common.LuckPermsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class OnPlayerJoinQuitEvent implements Listener {
    private final CatchAll main;
    private final Scoreboard scoreboard;

    public OnPlayerJoinQuitEvent() {
        this.main = CatchAll.getInstance();
        this.scoreboard =  Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            this.handleEvent(player);
            event.setJoinMessage(ChatColor.DARK_PURPLE + "+" + event.getPlayer().getName());
            updateScoreBoard(player);
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.DARK_BLUE + "-" + event.getPlayer().getName());
        leavingScoreboard(event.getPlayer());

    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        event.setLeaveMessage(ChatColor.DARK_BLUE + "-" + event.getPlayer().getName());
        leavingScoreboard(event.getPlayer());
    }


    private void handleEvent(Player player) throws Exception {
        int maxPlayers = Bukkit.getServer().getMaxPlayers();
        int onlineCount = Bukkit.getOnlinePlayers().size();

        if(onlineCount >= maxPlayers - 5){
            if(!player.hasPermission(Config.Donateur.permission)){
                throw new Exception(Config.Donateur.messageReservedSlot);
            }
        }

        boolean maintenance_state = this.main.getConfig().getBoolean("states.maintenance");
        boolean donateur_state = this.main.getConfig().getBoolean("states.donateur");

        if (maintenance_state) {
            if(!player.hasPermission(Config.Maintenance.permission)){
                throw new Exception(Config.Maintenance.message);
            }
        }

        if(donateur_state){
            if(!player.hasPermission(Config.Donateur.permission)){
                throw new Exception(Config.Donateur.message);
            }
        }
    }

    private void updateScoreBoard(Player player){
        String metaColor = LuckPermsUtils.getMetaData(CatchAll.api, player.getUniqueId(), Config.Tablist.metaName).orElse(Config.Tablist.metaDefaultName);
        Team team;
        if(scoreboard.getTeam(metaColor) == null){
            team = scoreboard.registerNewTeam(metaColor);
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', "&" + metaColor));
        } else {
            team = scoreboard.getTeam(metaColor);
        }
        team.addPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        player.setScoreboard(scoreboard);
    }

    private void leavingScoreboard(Player player){
        String metaColor = LuckPermsUtils.getMetaData(CatchAll.api, player.getUniqueId(), Config.Tablist.metaName).orElse(Config.Tablist.metaDefaultName);
        Team team = scoreboard.getTeam(metaColor);
        if(team != null){
            team.removePlayer(player);
            if(team.getSize() < 1){
                team.unregister();
            }
        }
    }
}