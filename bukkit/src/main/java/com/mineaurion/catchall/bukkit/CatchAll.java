package com.mineaurion.catchall.bukkit;

import com.mineaurion.catchall.bukkit.commands.*;
import com.mineaurion.catchall.bukkit.events.OnPlayerJoinEvent;
import com.mineaurion.catchall.bukkit.events.OnPlayerQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CatchAll extends JavaPlugin {

    private static CatchAll _instance = null;

    public CatchAll() {
        super();
        _instance = this;
    }

    public static CatchAll getInstance() {
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
        getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
    }

    private void initCommands() {
        // REGISTER COMMANDS HERE
        registerCommand("catchall", new CatchAllCommand());
        registerCommand("lastlogin", new LastLoginCommand());
        registerCommand("webhook", new WebhookCommand());
        registerCommand("maintenance", new MaintenanceCommand());
        registerCommand("donateur", new DonateurCommand());
        registerCommand("mchat", new MChatCommand());
        registerCommand("glist", new GListCommand());
    }

    private void registerCommand(String name, CommandExecutor cmd) {
        getCommand(name).setExecutor(cmd);
        sendMessage("Command : " + name + " loaded");
    }

    public void sendMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] " + msg);
    }
}
