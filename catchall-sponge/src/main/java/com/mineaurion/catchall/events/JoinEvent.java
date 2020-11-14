package com.mineaurion.catchall.events;

import com.mineaurion.catchall.CatchAllSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class JoinEvent implements EventListener<ClientConnectionEvent.Join> {
    @Override
    public void handle(ClientConnectionEvent.Join event) throws Exception {
        CatchAllSponge plugin = CatchAllSponge.getInstance();
        Optional<UserStorageService> uss = Sponge.getServiceManager().provide(UserStorageService.class);
        Player p = event.getTargetEntity();
        int citizenMax = Sponge.getServer().getMaxPlayers();
        int onlineCount = Sponge.getServer().getOnlinePlayers().size();

        if (onlineCount >= citizenMax - 5) {
            if (!p.hasPermission("mineaurion.donateur")) {
                p.kick(Text.of("Last slots are reserved to donators (and more) players"));
                return;
            }
        }

        boolean maintenance_state = plugin.getConf().get("states", "maintenance").getBoolean();
        boolean donateur_state = plugin.getConf().get("states", "donateur").getBoolean();

        if (maintenance_state) {
            if (!p.hasPermission("Maintenance.bypass")) {
                p.kick(Text.of("Server is now in maintenance. Try later please"));
                return;
            }
        }

        if (donateur_state) {
            if (!p.hasPermission("mineaurion.donateur")) {
                p.kick(Text.of("Server is now in donator only mode. Try later please"));
                return;
            }
        }
        event.setMessage(Text.builder("+" + event.getTargetEntity().getName()).color(TextColors.DARK_PURPLE).build());
    }
}