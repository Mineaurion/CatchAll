package com.mineaurion.Bukkit.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mineaurion.Bukkit.Main;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;

public class CommandWebhook implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mineaurion.webhook")) {
			if (args.length == 0) {
				sender.sendMessage("§cuse /webhook (ton message)!");
			} else {
				String message = "";
				int i = 0;
				while (i < args.length) {
					message = message + " " + args[i];
					i++;
				}
				message = ChatColor.translateAlternateColorCodes('&', message);
				message = ChatColor.stripColor(message);
				message.replace("<player>", sender.getName());

				DiscordMessage dm = DiscordMessage.builder().username("Minecraft Request") // Player's name
						.content(message) // Player's message
						.avatarUrl("https://www.residentadvisor.net/images/reviews/2012/specialrequest-ep1ep2.jpg") // Avatar
						.build();

				Main.getInstance().temmie.sendMessage(dm);
			}
		} else {
			sender.sendMessage("§cNo permission!");
		}
		return true;

	}

}
