package com.mineaurion.Bukkit.command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import com.mineaurion.Bukkit.Main;

public class CommandLanceTomate implements CommandExecutor {

	public HashMap<String,Long> cooldowns = new HashMap<String,Long>();
	int cooldownTime = 60; //En seconde
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lancetomate")) {
			if (sender.hasPermission("mineaurion.lanceTomate")) {
				if(args.length==0){
					Main.sendmessage("{{RED}}Utilise /lancetomate <Player>", sender.getName());
					return true;
				}
				Player target = Bukkit.getPlayer(args[0]);
				if(target==null||!target.isOnline()){
					Main.sendmessage("{{RED}}Le joueur n'est pas en ligne", sender.getName());
					return true;
				}
				if(cooldowns.containsKey(sender.getName())){
					long secondsLeft = ((cooldowns.get(sender.getName())/1000)+cooldownTime)- (System.currentTimeMillis()/1000);
					if(secondsLeft>0){
						Main.sendmessage("{{YELLOW}}You can't use that commands for another "+secondsLeft+" seconds", sender.getName());
						return true;
					}else{
						lanceTomate(sender,target);
						return true;
					}
				}
				lanceTomate(sender,target);
				return true;
				
			}else{
				Main.sendmessage("{{RED}}You don't have permission", sender.getName());
			}
			return true;
		}
		return false;
	}

	private void lanceTomate(CommandSender sender, Player target) {
		cooldowns.put(sender.getName(), System.currentTimeMillis());
		
		
		Location eyeLoc = target.getEyeLocation();
		Vector vec = target.getLocation().getDirection().multiply(10);
		Location frontlocation = eyeLoc.add(vec);
		frontlocation.getDirection().setY(eyeLoc.getY());
		Snowball snowball = target.getWorld().spawn(frontlocation, Snowball.class);
		snowball.setVelocity(frontlocation.getDirection().multiply(-1.3));
		Main.sendmessage("{{YELLOW}}Tu as recu une boule de neige de...",target.getName());
	}
}
