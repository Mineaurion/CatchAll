package com.mineaurion.Bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineaurion.Bukkit.Main;

public class CommandDonateur implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mineaurion.donateurCommand")) {
			if (!Main.getInstance().getConfig().getBoolean("AccesDonateur")) {
				Main.getInstance().getConfig().set("AccesDonateur", true);
				Bukkit.broadcast("§cAttention le serveur est maintenant réservé au donateur", "mineaurion.fr");
				Bukkit.broadcast("§cWarning, the server is now reserved to donator", "mineaurion.en");
				Main.sendmessage("§cAttention le serveur est maintenant est réservé au donateur", "console");
				
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					if(!p.hasPermission("mineaurion.donateur")) {
						p.kickPlayer("Reserved to donator");
					}
				}
			}else {
				Main.getInstance().getConfig().set("AccesDonateur", false);
				Bukkit.broadcastMessage("Le serveur n'est plus reservé au donateur");
				Main.sendmessage("Le serveur n'est plus reservé au donateur", "console");
			}

		} else {
			sender.sendMessage("§cNo permission!");
		}

		return true;

	}

}
