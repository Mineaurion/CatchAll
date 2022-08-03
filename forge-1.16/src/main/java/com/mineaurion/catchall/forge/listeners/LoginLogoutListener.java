package com.mineaurion.catchall.forge.listeners;

import com.mineaurion.catchall.forge.CatchAll;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class LoginLogoutListener {
    private final CatchAll plugin;

    public LoginLogoutListener(CatchAll plugin){
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) throws Exception {
        int maxPlayers = ServerLifecycleHooks.getCurrentServer().getMaxPlayers();
        int onlineCount = ServerLifecycleHooks.getCurrentServer().getPlayerCount();

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        if(onlineCount >= maxPlayers - 5){
            if(!CatchAll.hasPermission(player, "mineaurion.donateur")){
                throw new Exception("Last slots are reserved to donators (and more) players");
            }
        }

        boolean maintenance_state = CatchAll.config.states.maintenance.get();
        boolean donateur_state = CatchAll.config.states.donateur.get();

        if(maintenance_state){
            if(!CatchAll.hasPermission(player, "maintenance.bypass")){
                player.connection.disconnect(new StringTextComponent("Servers is now in maintenance. Try later please"));
                // Throw exception pas sur du delire en vrai
            }
        }

        if(donateur_state){
            if(!CatchAll.hasPermission(player,"mineaurion.donateur")){
                player.connection.disconnect(new StringTextComponent("Server is now in donator only mode. Try later please"));
            }
        }
    }
}
