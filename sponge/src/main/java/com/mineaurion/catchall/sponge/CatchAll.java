package com.mineaurion.catchall.sponge;

import com.google.inject.Inject;
import com.mineaurion.catchall.sponge.events.AuthEvent;
import com.mineaurion.catchall.sponge.events.DisconnectEvent;
import com.mineaurion.catchall.sponge.events.JoinEvent;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

@Plugin(
        id = "catchall",
        name = "CatchAll",
        url = "https://mineaurion.com",
        description = "Mineaurion utils",
        authors = {
                "Yann151924",
                "Ashk2a"
        },
        version = "@projectVersion@"
)
public class CatchAll {
    private static CatchAll _instance = null;
    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = true)
    public Path configDir;

    public SettingManager conf;

    public CatchAll() {
        _instance = this;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        // Settings
        conf = new SettingManager();
    }

    @Listener
    public void init(GameInitializationEvent event) {
        EventManager eventManager = Sponge.getEventManager();
        eventManager.registerListener(this, ClientConnectionEvent.Auth.class, new AuthEvent());
        eventManager.registerListener(this, ClientConnectionEvent.Join.class, new JoinEvent());
        eventManager.registerListener(this, ClientConnectionEvent.Disconnect.class, new DisconnectEvent());
        CommandManager cm = new CommandManager();
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

    public static CatchAll getInstance() {
        return _instance;
    }
}
