package com.mineaurion.Sponge.event;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

import com.flowpowered.math.vector.Vector3d;
import com.mineaurion.Sponge.Main;

public class EventListener {

	int donateurConnected = 0;

	@Listener(order = Order.BEFORE_POST)
	public void onPlayerJoin(ClientConnectionEvent.Auth event) {
		final GameProfile profile = event.getProfile();
		Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
		Optional<User> user = userStorage.get().get(profile.getUniqueId());

		if (Main.getInstance().Node.getNode("Maintenance").getBoolean()) {

			if (!user.get().hasPermission("Maintenance.bypass")) {
				event.setCancelled(true);
				event.setMessage(Text.of("Maintenance"));
				return;
			}

		} else if (Main.getInstance().Node.getNode("AccesDonateur").getBoolean()) {

			if (!user.get().hasPermission("mineaurion.donateur")) {
				event.setCancelled(true);
				event.setMessage(Text.of("server reserved to donator"));
				return;
			}

		}
		int reservedSlot = (Main.getInstance().Node.getNode("reservedSlot").getInt() - donateurConnected);
		reservedSlot = reservedSlot < 0 ? 0 : reservedSlot;
		int onlinePlayer = Sponge.getServer().getOnlinePlayers().size();
		if (Sponge.getServer().getMaxPlayers() - reservedSlot <= onlinePlayer) {
			if (!user.get().hasPermission("mineaurion.donateur")) {
				event.setCancelled(true);
				event.setMessage(Text.of("You need to be donator to connect"));
				return;
			}else {
				if(Sponge.getServer().getMaxPlayers()==onlinePlayer) {
					while (true) {
						Player p = (Player)getRandomObject(Sponge.getServer().getOnlinePlayers());
						if(!p.hasPermission("mineaurion.donateur")) {
							p.kick(Text.of("Nous sommes désolé le serveur est plein, et un donateur veux ce connecter. Achete un grade pour ne plus être kick"));
							break;
						}
					}
				}
			}
		}

		if (user.get().hasPermission("mineaurion.donateur")) {
			donateurConnected++;
		}

	}
	@Listener
	public void playerDisconnect(ClientConnectionEvent.Disconnect event) {
		if(event.getTargetEntity().hasPermission("mineaurion.donateur")) {
			donateurConnected--;
		}
	}
	
	@Listener
	public void playerTeleportPortal(MoveEntityEvent.Teleport event) {
		
		if (event.getTargetEntity() instanceof Player) {
			Player player = (Player)event.getTargetEntity();

			Transform<World> location = event.getToTransform();
			WorldBorder worldborder = location.getExtent().getWorldBorder();
			
			Vector3d posplayer = new Vector3d(location.getPosition().getX(), 50, location.getPosition().getZ());
			Vector3d posborder = new Vector3d(worldborder.getCenter().getX(),50,worldborder.getCenter().getZ());

			if(posplayer.distance(posborder)> (worldborder.getDiameter()/2)) {
				event.setCancelled(true);
				player.setLocationSafely(location.getExtent().getSpawnLocation());
				Main.sendmessage("{{RED}}Tu as été teleporté au spawn du monde car tu est appararu en dehors des limites", player.getName());
			}
		}
	}
	
	
	private Object getRandomObject(Collection<?> from) {
		   Random rnd = new Random();
		   int i = rnd.nextInt(from.size());
		   return from.toArray()[i];
		}
}
