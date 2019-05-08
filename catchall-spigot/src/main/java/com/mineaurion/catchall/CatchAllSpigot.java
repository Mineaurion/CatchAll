package com.mineaurion.catchall;

import com.mineaurion.catchall.commands.*;
import com.mineaurion.catchall.events.OnPlayerJoinEvent;
import com.mineaurion.catchall.events.OnPlayerLoginEvent;
import com.mineaurion.catchall.events.OnPlayerQuitEvent;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CatchAllSpigot extends JavaPlugin {

    private static CatchAllSpigot _instance = null;
    private int commandCount = 0;
    private int commandIgnoredCount = 0;

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
    }

    private void initCommands() {
        // REGISTER COMMANDS HERE
        registerCommand("catchall", new CatchAllCommand());
        registerCommand("lastlogin", new LastLoginCommand());
        registerCommand("webhook", new WebhookCommand());
        registerCommand("maintenance", new MaintenanceCommand());
        registerCommand("donateur", new DonateurCommand());
        registerCommand("mchat", new MChatCommand());
        sendMessage("Total : " + this.commandCount + " command(s) loaded (" + this.commandIgnoredCount + " ignored)");
    }

    private void registerCommand(String name, CommandExecutor cmd) {
        if (getConfig().getBoolean(("commands." + name))) {
            getCommand(name).setExecutor(cmd);
            sendMessage("Command : " + name + " loaded");
            this.commandCount++;
        } else {
            getCommand(name).setUsage("/" + name + " currently disabled");
            sendMessage("Command : " + name + " ignored");
            this.commandIgnoredCount++;
        }
    }

    public void sendMessage(String msg, String player) {
        Bukkit.getPlayer(player).sendMessage(msg);
    }

    public void sendMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] " + msg);
    }
}
