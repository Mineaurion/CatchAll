package com.mineaurion.Bukkit.command;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mineaurion.Bukkit.Main;

public class CommandLastLogin implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lastlogin")) {
			if (sender.hasPermission("mineaurion.lastlogin")) {
				
				List<String> Player = new ArrayList<String>();

				OfflinePlayer[] offlinePlayer = Bukkit.getServer().getOfflinePlayers();

				for (OfflinePlayer P : offlinePlayer) {
					long lastlog = P.getLastPlayed();
					Calendar moisdernier = Calendar.getInstance();
					Calendar moisBetweend = Calendar.getInstance();
					if(args.length == 1) {
						moisdernier.add(Calendar.MONTH, -1 * Integer.valueOf(args[0]));
					}
					else if (args.length == 2){
						moisBetweend.add(Calendar.MONTH, 1 * Integer.valueOf(args[1]));
					}
					else {
						moisdernier.add(Calendar.MONTH, -1);
					}
					Calendar DateLog = Calendar.getInstance();
					DateLog.setTime(new Date(lastlog));
					if (args.length == 2) {
						if (DateLog.before(moisdernier) && DateLog.after(moisBetweend)) {
							Player.add(P.getName());
						}
					}else {
						if (DateLog.before(moisdernier)) {
							Player.add(P.getName());
						}
					}
				}
				File dataFile = new File(Main.getInstance().getDataFolder()+File.separator+"PlayerAFK.yml");
				try {
					dataFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);
				
				dataConfig.set("Player", Player);
				try {
					dataConfig.save(dataFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Main.sendmessage("{{RED}}You don't have permission", sender.getName());
			}
			return true;
		}
		return false;
	}

}
