package com.mineaurion.Sponge.event;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import com.mineaurion.Sponge.Main;

public class EventListener {
	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event) {
		if(Main.getInstance().Node.getNode("Maintenance").getBoolean() && !event.getTargetEntity().hasPermission("Maintenance.bypass")) {
			MessageChannel messageChannel = MessageChannel.TO_PLAYERS;
			messageChannel.send(Text.of(event.getTargetEntity().getName() + " essaye de se connecter"));
			event.getTargetEntity().kick(Text.of("Maintenance"));
		}
	}
}
