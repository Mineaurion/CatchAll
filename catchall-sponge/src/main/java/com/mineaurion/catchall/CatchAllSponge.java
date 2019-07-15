package com.mineaurion.catchall;

import com.google.inject.Inject;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

@Plugin(id = "catchall", name = "CatchAll")
public class CatchAllSponge {
    private static CatchAllSponge _instance = null;
    @Inject
    public Logger logger;

    @Inject
    private Game game;

    @Inject
    public PluginContainer pluginContainer;

    @Inject
    @ConfigDir(sharedRoot = true)
    public Path configDir;

    public SettingManager settings;

    public CatchAllSponge() {
        _instance = this;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {

    }

    @Listener
    public void init(GameInitializationEvent event) {
        settings = new SettingManager();
        logger.info("HELLO " + SettingManager.states.get("maintenance").getString());

        // register events
        // register commands
    }

    private void initConfig() {

    }

    private void initCommands() {

    }

    private void initEvents() {

    }

    private void registerCommand() {

    }

    private void registerEvent() {

    }

    public static CatchAllSponge getInstance() {
        return _instance;
    }
}
