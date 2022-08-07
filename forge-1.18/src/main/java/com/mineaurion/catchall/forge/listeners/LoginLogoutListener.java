package com.mineaurion.catchall.forge.listeners;

import com.mineaurion.catchall.common.Config;
import com.mineaurion.catchall.forge.CatchAll;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class LoginLogoutListener {

    private final ServerScoreboard scoreboard;

    public LoginLogoutListener(ServerScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        int maxPlayers = ServerLifecycleHooks.getCurrentServer().getMaxPlayers();
        int onlineCount = ServerLifecycleHooks.getCurrentServer().getPlayerCount();

        ServerPlayer player = (ServerPlayer) event.getPlayer();
        updateScoreBoard(player);

        if(onlineCount >= maxPlayers - 5){
            if(!CatchAll.hasPermission(player, Config.Donateur.permission)){
                player.connection.disconnect(new TextComponent(Config.Donateur.messageReservedSlot));
            }
        }

        if(CatchAll.config.states.maintenance.get()){
            if(!CatchAll.hasPermission(player, Config.Maintenance.permission)){
                player.connection.disconnect(new TextComponent(Config.Maintenance.message));
            }
        }

        if(CatchAll.config.states.donateur.get()){
            if(!CatchAll.hasPermission(player,Config.Donateur.permission)){
                player.connection.disconnect(new TextComponent(Config.Donateur.message));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        leavingScoreBoard((ServerPlayer) event.getPlayer());
    }

    private void updateScoreBoard(ServerPlayer player){
        String nameColor = CatchAll.getMetaData(player.getUUID(), Config.Tablist.metaName).orElse(Config.Tablist.metaDefaultName);
        PlayerTeam team = scoreboard.getPlayerTeam("tablist-" + nameColor);
        if(team == null) {
            team = scoreboard.addPlayerTeam("tablist-" + nameColor);
            ChatFormatting chatFormatting = ChatFormatting.getByCode(nameColor.charAt(0));
            team.setColor(chatFormatting != null ? chatFormatting : ChatFormatting.WHITE);
        }
        scoreboard.addPlayerToTeam(player.getScoreboardName(), team);
    }

    private void leavingScoreBoard(ServerPlayer player){
        for (PlayerTeam team: player.getScoreboard().getPlayerTeams()) {
            if(team.getName().contains("tablist-") && team.getPlayers().contains(player.getScoreboardName())){
                try {
                    scoreboard.removePlayerFromTeam(player.getScoreboardName(), team);
                } catch (IllegalStateException e){
                    CatchAll.logger.error("Error remove player " + player.getDisplayName() + " from team " + team.getName());
                }
                if(team.getPlayers().size() == 0){
                    scoreboard.removePlayerTeam(team);
                }
            }
        }
    }
}
