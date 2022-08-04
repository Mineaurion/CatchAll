package com.mineaurion.catchall.bukkit;

import com.mineaurion.catchall.bukkit.commands.*;
import com.mineaurion.catchall.bukkit.events.OnPlayerJoinQuitEvent;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CatchAll extends JavaPlugin {

    private static CatchAll _instance = null;
    public static LuckPerms api;

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
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(provider != null){
            api = provider.getProvider();
            this.init();
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
        }
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
        getServer().getPluginManager().registerEvents(new OnPlayerJoinQuitEvent(), this);
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
