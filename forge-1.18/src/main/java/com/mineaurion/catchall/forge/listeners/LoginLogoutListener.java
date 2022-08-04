package com.mineaurion.catchall.forge.listeners;

import com.mineaurion.catchall.common.Config;
import com.mineaurion.catchall.forge.CatchAll;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class LoginLogoutListener {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        int maxPlayers = ServerLifecycleHooks.getCurrentServer().getMaxPlayers();
        int onlineCount = ServerLifecycleHooks.getCurrentServer().getPlayerCount();

        ServerPlayer player = (ServerPlayer) event.getPlayer();

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
}
