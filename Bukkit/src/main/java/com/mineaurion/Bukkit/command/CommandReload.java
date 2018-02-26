package com.mineaurion.Bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mineaurion.Bukkit.Main;

public class CommandReload implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender.hasPermission("mineaurion.reload")) {
			Main.getInstance().initConfig();
			Main.getInstance().initPlugin();
			Main.getInstance().initCommand();
			sender.sendMessage("§aMineaurion Bukkit reloaded!");
		} else {
			sender.sendMessage("§cNo permission!");
		}
		
		return true;
	}

}
