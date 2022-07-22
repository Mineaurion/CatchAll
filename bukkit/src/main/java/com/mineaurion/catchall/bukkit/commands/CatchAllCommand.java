package com.mineaurion.catchall.bukkit.commands;

import com.mineaurion.catchall.bukkit.CatchAll;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;


public class CatchAllCommand implements CommandExecutor {
    private final CatchAll main;

    public CatchAllCommand() {
        this.main = CatchAll.getInstance();
        StringBuilder usage = new StringBuilder();
        Map<String, Map<String, Object>> map = this.main.getDescription().getCommands();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                usage.append("/")
                        .append(entry.getKey())
                        .append(" - ")
                        .append(entry.getValue().get("description"))
                        .append("\n");
        }
        this.main.getCommand("catchall").setUsage(usage.toString());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0){
            return false;
        }

        switch (args[0]) {
            case "reload":
                main.reloadConfig();
                this.main.init();
                sender.sendMessage("Plugin reloaded");
                break;
            case "help":
            default:
                return false;
        }

        return true;
    }
}