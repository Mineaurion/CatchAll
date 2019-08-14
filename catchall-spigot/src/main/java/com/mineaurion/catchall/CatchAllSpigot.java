package com.mineaurion.catchall;

import com.mineaurion.catchall.commands.*;
import com.mineaurion.catchall.events.OnPlayerCommandPreprocess;
import com.mineaurion.catchall.events.OnPlayerJoinEvent;
import com.mineaurion.catchall.events.OnPlayerLoginEvent;
import com.mineaurion.catchall.events.OnPlayerQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CatchAllSpigot extends JavaPlugin {

    private static CatchAllSpigot _instance = null;

    public CatchAllSpigot() {
        super();
        _instance = this;
    }

    public static CatchAllSpigot getInstance() {
        return _instance;
    }

    @Override
    public void onEnable() {
        sendMessage("Start plugin... ");
        this.init();
    }

    @Override
    public void onDisable() {
        sendMessage("End plugin");
    }

    public void init() {
        initConfig();
        initEvents();
        initCommands();
    }

    private void initConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
        sendMessage("File config.yml loaded");
    }

    private void initEvents() {
        getServer().getPluginManager().registerEvents(new OnPlayerLoginEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerCommandPreprocess(), this);
    }

    private void initCommands() {
        // REGISTER COMMANDS HERE
        registerCommand("catchall", new CatchAllCommand());
        registerCommand("lastlogin", new LastLoginCommand());
        registerCommand("webhook", new WebhookCommand());
        registerCommand("maintenance", new MaintenanceCommand());
        registerCommand("donateur", new DonateurCommand());
        registerCommand("mchat", new MChatCommand());
    }

    private void registerCommand(String name, CommandExecutor cmd) {
        getCommand(name).setExecutor(cmd);
        sendMessage("Command : " + name + " loaded");
    }

    public void sendMessage(String msg, String player) {
        Bukkit.getPlayer(player).sendMessage(msg);
    }

    public void sendMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] " + msg);
    }
}
