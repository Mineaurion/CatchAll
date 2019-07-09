package com.mineaurion.catchall;

import com.google.inject.Inject;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "catchall",
        name = "CatchAll"
)
public class CatchAllSponge {
    public static CatchAllSponge _instance = null;
    @Inject
    private Logger logger;
    private Game game;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        //TODO: load config
    }

    @Listener
    public void init(GameInitializationEvent event) {
        //TODO: init cmd and events
    }

    private void initConfig() {

    }

    private void initCommands() {

    }

    public static CatchAllSponge getInstance() {
        return _instance;
    }
}
