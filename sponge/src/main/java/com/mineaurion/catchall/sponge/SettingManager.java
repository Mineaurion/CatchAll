package com.mineaurion.catchall.sponge;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingManager {
    private final CatchAll plugin;
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode node;

    public SettingManager() {
        plugin = CatchAll.getInstance();
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        Path path = Paths.get(plugin.configDir + "/catchall.conf");
        if(!Files.exists(path)) {
            plugin.getLogger().info("Create configuration file");
            Sponge.getAssetManager().getAsset(plugin, "catchall.conf").get().copyToFile(path);
        }

        loader = HoconConfigurationLoader.builder().setPath(path).build();
        node = loader.load();
    }

    public void save() {
        try {
            this.loader.save(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommentedConfigurationNode get(@NonNull Object... path) {
        return node.getNode(path);
    }
}
