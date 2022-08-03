package com.mineaurion.catchall.forge.listeners;

import com.mineaurion.catchall.forge.CatchAll;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class TablistNameFormatListener {

    @SubscribeEvent
    public void scheduleChangeColorTablist(PlayerEvent.TabListNameFormat event){
        Player player = event.getPlayer();
        Optional<String> nameColor = CatchAll.getMetaData(player.getUUID(), "namecolor");
        event.setDisplayName(new TextComponent("§" + nameColor.orElse("f") + player.getDisplayName().getString()));
    }
}
