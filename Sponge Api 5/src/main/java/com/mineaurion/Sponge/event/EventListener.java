package com.mineaurion.Sponge.event;

import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import com.mineaurion.Sponge.Main;

public class EventListener {
	@Listener(order = Order.BEFORE_POST)
	public void onPlayerJoin(ClientConnectionEvent.Auth event) {
		if (Main.getInstance().Node.getNode("Maintenance").getBoolean())
		{
			final GameProfile profile = event.getProfile();
			try {
				Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
			    Optional<User> user = userStorage.get().get(profile.getUniqueId());
			    if (!user.get().hasPermission("Maintenance.bypass"))
			    {
			    	 event.setCancelled(true);
	                 event.setMessage(Text.of("Maintenance"));
			    }
			}catch(Exception ex){
				event.setCancelled(true);
				event.setMessage(Text.of("Error User Maintenance (contact Admin)"));
	            Main.sendmessage(ex.getMessage(), "console");
			}
		}
	}
}
