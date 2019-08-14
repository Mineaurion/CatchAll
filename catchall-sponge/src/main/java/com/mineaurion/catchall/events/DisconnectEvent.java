package com.mineaurion.catchall.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class DisconnectEvent implements EventListener<ClientConnectionEvent.Disconnect> {
    @Override
    public void handle(ClientConnectionEvent.Disconnect event) throws Exception {
        event.setMessage(Text.builder("-" + event.getTargetEntity().getName()).color(TextColors.BLUE).build());
    }
}