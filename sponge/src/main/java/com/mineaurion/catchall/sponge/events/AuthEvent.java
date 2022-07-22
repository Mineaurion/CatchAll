package com.mineaurion.catchall.sponge.events;

import com.mineaurion.catchall.sponge.CatchAll;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class AuthEvent implements EventListener<ClientConnectionEvent.Auth> {
    
    @Override
    public void handle(ClientConnectionEvent.Auth event) {
        Optional<UserStorageService> uss = Sponge.getServiceManager().provide(UserStorageService.class);
        Optional<User> user = uss.get().get(event.getProfile().getUniqueId());

        if (!user.isPresent()){
            return;
        }

        try {
            handleEvent(user.get());
        } catch (Exception e){
            event.setCancelled(true);
            event.setMessage(Text.of(e.getMessage()));
        }
    }

    public void handleEvent(User user) throws Exception{
        if(user.getPlayer().isPresent()){
            JoinEvent.handleEvent(user.getPlayer().get(), CatchAll.getInstance());
        }
    }
}