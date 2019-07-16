package com.mineaurion.catchall.events;

import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class JoinEvent implements EventListener<ClientConnectionEvent.Join> {
    @Override
    public void handle(ClientConnectionEvent.Join event) throws Exception {
        event.setMessage(Text.builder("+" + event.getTargetEntity().getName()).color(TextColors.DARK_PURPLE).build());
    }
}