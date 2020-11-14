package com.mineaurion.catchall;

import com.google.inject.Inject;
import com.mineaurion.catchall.events.AuthEvent;
import com.mineaurion.catchall.events.DisconnectEvent;
import com.mineaurion.catchall.events.JoinEvent;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;


import java.nio.file.Path;

@Plugin(id = "catchall", name = "CatchAll")
public class CatchAllSponge {
    private static CatchAllSponge _instance = null;
    @Inject
    private Logger logger;

    @Inject
    private Game game;

    @Inject
    public PluginContainer pluginContainer;

    @Inject
    @ConfigDir(sharedRoot = true)
    public Path configDir;

    public SettingManager conf;

    public CatchAllSponge() {
        _instance = this;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        // Settings
        conf = new SettingManager();
    }

    @Listener
    public void init(GameInitializationEvent event) {
        registerEvents();
        CommandManager cm = new CommandManager();
    }

    private void registerEvents() {
        Sponge.getEventManager().registerListener(this, ClientConnectionEvent.Auth.class, new AuthEvent());
        Sponge.getEventManager().registerListener(this, ClientConnectionEvent.Join.class, new JoinEvent());
        Sponge.getEventManager().registerListener(this, ClientConnectionEvent.Disconnect.class, new DisconnectEvent());
    }

    public void sendMessage(Text text) {
        Sponge.getServer().getConsole().sendMessage(text);
    }

    public SettingManager getConf() {
        return conf;
    }

    public Logger getLogger() {
        return logger;
    }

    public void reload() {
        conf = new SettingManager();
    }

    public static CatchAllSponge getInstance() {
        return _instance;
    }
}
