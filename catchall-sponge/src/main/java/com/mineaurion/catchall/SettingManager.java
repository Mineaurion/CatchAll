package com.mineaurion.catchall;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SettingManager {
    private CatchAllSponge plugin;

    public SettingManager() {
        plugin = CatchAllSponge.getInstance();
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        Path path = Paths.get(plugin.configDir + "/settings.conf");
        if(!Files.exists(path)) {
            plugin.logger.info("Create configuration file");
            Sponge.getAssetManager().getAsset(plugin, "settings.conf").get().copyToFile(path);
        }

        ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setPath(path).build();

        CommentedConfigurationNode node = loader.load();

        states = node.getNode("states").getChildrenMap();
        commands = node.getNode("commands").getChildrenMap();
        discord = node.getNode("webhook", "discord").getChildrenMap();
    }

    public static Map<Object, ? extends CommentedConfigurationNode> states = new HashMap<>();
    public static Map<Object, ? extends CommentedConfigurationNode> commands = new HashMap<>();
    public static Map<Object, ? extends CommentedConfigurationNode> discord = new HashMap<>();
}
