package com.mineaurion.catchall.sponge.events;

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
        int onlineCount = Sponge.getServer().getMaxPlayers();

        if(onlineCount >= maxPlayers - 5){
            if(!player.hasPermission("mineaurion.donateur")){
                throw new Exception("Last Slots are reserved to donators (and more) players");
            }
        }

        boolean maintenance_state = plugin.getConf().get("states", "maintenance").getBoolean();
        boolean donateur_state = plugin.getConf().get("states", "donateur").getBoolean();

        if(maintenance_state){
            if(!player.hasPermission("maintenance.bypass")){
                throw new Exception("Servers is now in maintenance, Try later please");
            }
        }

        if(donateur_state){
            if(!player.hasPermission("mineaurion.donateur")){
                throw new Exception("Server is now in dontor only mode. Try later please");
            }
        }
    }
}