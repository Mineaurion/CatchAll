package com.mineaurion.catchall.sponge.events;

import com.mineaurion.catchall.common.Config;
import com.mineaurion.catchall.sponge.CatchAll;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class JoinEvent implements EventListener<ClientConnectionEvent.Join> {
    @Override
    public void handle(ClientConnectionEvent.Join event) {
        CatchAll plugin = CatchAll.getInstance();
        Player player = event.getTargetEntity();

        try{
            handleEvent(player, plugin);
            event.setMessage(Text.builder("+" + player.getName()).color(TextColors.DARK_PURPLE).build());
        } catch (Exception e){
            player.kick(Text.of(e.getMessage()));
        }
    }

    public static void handleEvent(Player player, CatchAll plugin) throws Exception {
        int maxPlayers = Sponge.getServer().getMaxPlayers();
        int onlineCount = Sponge.getServer().getOnlinePlayers().size();

        if(onlineCount >= maxPlayers - 5){
            if(!player.hasPermission(Config.Donateur.permission)){
                throw new Exception(Config.Donateur.messageReservedSlot);
            }
        }

        boolean maintenance_state = plugin.getConf().get("states", "maintenance").getBoolean();
        boolean donateur_state = plugin.getConf().get("states", "donateur").getBoolean();

        if(maintenance_state){
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
}
