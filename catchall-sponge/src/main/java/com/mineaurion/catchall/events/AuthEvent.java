package com.mineaurion.catchall.events;

import com.mineaurion.catchall.CatchAllSponge;
import com.mineaurion.catchall.SettingManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class AuthEvent implements EventListener<ClientConnectionEvent.Auth> {
    @Override
    public void handle(ClientConnectionEvent.Auth event) throws Exception {
        CatchAllSponge plugin = CatchAllSponge.getInstance();
        Optional<UserStorageService> uss = Sponge.getServiceManager().provide(UserStorageService.class);
        Optional<User> user = uss.get().get(event.getProfile().getUniqueId());

        if (!user.isPresent())
            return;

        int citizenMax = Sponge.getServer().getMaxPlayers();
        int onlineCount = Sponge.getServer().getOnlinePlayers().size();


        if (onlineCount >= citizenMax - 5) {
            if (!user.get().hasPermission("mineaurion.donateur")) {
                event.setCancelled(true);
                event.setMessage(Text.of("Last slots are reserved to donators (and more) players"));
                return;
            }
        }

        boolean maintenance_state = plugin.getConf().get("states", "maintenance").getBoolean();
        boolean donateur_state = plugin.getConf().get("states", "donateur").getBoolean();

        if (maintenance_state) {
            if (!user.get().hasPermission("Maintenance.bypass")) {
                event.setCancelled(true);
                event.setMessage(Text.of("Server is now in maintenance. Try later please"));
                return;
            }
        }

        if (donateur_state) {
            if (!user.get().hasPermission("mineaurion.donateur")) {
                event.setCancelled(true);
                event.setMessage(Text.of("Server is now in donator only mode. Try later please"));
            }
        }
    }
}