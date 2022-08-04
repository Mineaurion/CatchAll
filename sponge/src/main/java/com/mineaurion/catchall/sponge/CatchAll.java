package com.mineaurion.catchall.sponge;

import com.google.inject.Inject;
import com.mineaurion.catchall.common.LuckPermsUtils;
import com.mineaurion.catchall.sponge.events.AuthEvent;
import com.mineaurion.catchall.sponge.events.DisconnectEvent;
import com.mineaurion.catchall.sponge.events.JoinEvent;
import net.luckperms.api.LuckPerms;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "catchall",
        name = "CatchAll",
        url = "https://mineaurion.com",
        description = "Mineaurion utils",
        authors = {
                "Yann151924",
                "Ashk2a"
        },
        version = "@projectVersion@",
        dependencies = {
            @Dependency(id= "luckperms", optional = true)
        }
)
public class CatchAll {
    private static CatchAll _instance = null;
    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = true)
    public Path configDir;

    public SettingManager conf;

    private LuckPerms api;

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
        Optional<ProviderRegistration<LuckPerms>> provider = Sponge.getServiceManager().getRegistration(LuckPerms.class);
        provider.map(ProviderRegistration::getProvider).ifPresent(luckPerms -> Task.builder()
                .name("update-color-name")
                .interval(1, TimeUnit.SECONDS)
                .execute(() -> {
                    for(Player player: Sponge.getServer().getOnlinePlayers()){
                        updateName(luckPerms, player);
                    }
                }).submit(this)
        );
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

    private void updateName(LuckPerms api, Player player){
        TabList tabList = player.getTabList();
        for(TabListEntry entry: tabList.getEntries()){
            StringBuilder newName = new StringBuilder();
            Sponge.getServer().getPlayer(entry.getProfile().getUniqueId()).ifPresent(p -> {
                String metaColor = LuckPermsUtils.getMetaData(api, p.getUniqueId(), "namecolor").orElse("f");
                newName
                        .append("&")
                        .append(metaColor)
                        .append(p.getName())
                ;
            });
            entry.setDisplayName(TextSerializers.FORMATTING_CODE.deserialize(newName.toString()));
        }
    }
}
