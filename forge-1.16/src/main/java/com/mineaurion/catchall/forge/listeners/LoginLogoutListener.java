package com.mineaurion.catchall.forge.listeners;

import com.mineaurion.catchall.common.Config;
import com.mineaurion.catchall.forge.CatchAll;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class LoginLogoutListener {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) throws Exception {
        int maxPlayers = ServerLifecycleHooks.getCurrentServer().getMaxPlayers();
        int onlineCount = ServerLifecycleHooks.getCurrentServer().getPlayerCount();

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        if(onlineCount >= maxPlayers - 5){
            if(!CatchAll.hasPermission(player, Config.Donateur.permission)){
                throw new Exception(Config.Donateur.messageReservedSlot);
            }
        }

        if(CatchAll.config.states.maintenance.get()){
            if(!CatchAll.hasPermission(player, Config.Maintenance.permission)){
                player.connection.disconnect(new StringTextComponent(Config.Maintenance.message));
            }
        }

        if(CatchAll.config.states.donateur.get()){
            if(!CatchAll.hasPermission(player,Config.Donateur.permission)){
                player.connection.disconnect(new StringTextComponent(Config.Donateur.message));
            }
        }
    }

}
