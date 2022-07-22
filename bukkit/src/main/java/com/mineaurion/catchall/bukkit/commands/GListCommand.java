package com.mineaurion.catchall.bukkit.commands;

import com.mineaurion.catchall.common.QueryServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
public class GListCommand implements CommandExecutor {

    public GListCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', QueryServer.getServers()));
            return true;
        } catch (Exception e){
            sender.sendMessage(e.getMessage());
            return false;
        }
    }
}
