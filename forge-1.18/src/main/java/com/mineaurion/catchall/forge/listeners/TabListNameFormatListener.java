package com.mineaurion.catchall.forge.listeners;

import com.mineaurion.catchall.common.Config;
import com.mineaurion.catchall.forge.CatchAll;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TabListNameFormatListener {

    @SubscribeEvent
    public void scheduleChangeColorTabList(PlayerEvent.TabListNameFormat event){
        Player player = event.getPlayer();
        String nameColor = CatchAll.getMetaData(player.getUUID(), Config.Tablist.metaName).orElse(Config.Tablist.metaDefaultName);
        event.setDisplayName(new TextComponent("§" + nameColor + player.getDisplayName().getString()));
    }
}
