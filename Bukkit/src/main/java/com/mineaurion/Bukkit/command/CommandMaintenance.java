package com.mineaurion.Bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineaurion.Bukkit.Main;

public class CommandMaintenance implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mineaurion.maintenance")) {
			if (!Main.getInstance().getConfig().getBoolean("Maintenance")) {
				Main.getInstance().getConfig().set("Maintenance", true);
				Bukkit.broadcast("§cAttention le serveur est maintenant en maintenance", "mineaurion.fr");
				Bukkit.broadcast("§cWArning, the server is now in maintenance", "mineaurion.en");
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					if(!p.hasPermission("Maintenance.bypass")) {
						p.kickPlayer("Maintenance");
					}
				}
			}else {
				Main.getInstance().getConfig().set("Maintenance", false);
				Bukkit.broadcastMessage("Le serveur n'est plus en maintenance");
			}

		} else {
			sender.sendMessage("§cNo permission!");
		}

		return true;

	}

}
