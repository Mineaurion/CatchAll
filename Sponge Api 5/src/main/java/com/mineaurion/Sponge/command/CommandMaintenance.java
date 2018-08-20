package com.mineaurion.Sponge.command;

import java.util.Iterator;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import com.mineaurion.Sponge.Main;

public class CommandMaintenance implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!Main.getInstance().Node.getNode("Maintenance").getBoolean()) {
			Main.getInstance().setValut(true, "Maintenance");
			MessageChannel messageChannel = MessageChannel.TO_PLAYERS;
			messageChannel.send(Main.addColor("{{RED}}Attention le serveur est maintenant en maintenance"));
			messageChannel.send(Main.addColor("{{RED}}WArning, the server is now in maintenance"));
			Main.sendmessage("{{RED}}Attention le serveur est maintenant en maintenance", "console");
			
			for(Iterator<Player> iter = Sponge.getServer().getOnlinePlayers().iterator(); iter.hasNext();) {
				Player p = iter.next();
				if(!p.hasPermission("Maintenance.bypass")) {
					p.kick(Text.of("Maintenance"));
				}
			}
		}else {
			Main.getInstance().setValut(false, "Maintenance");
			MessageChannel messageChannel = MessageChannel.TO_PLAYERS;
			messageChannel.send(Main.addColor("{{RED}}Le serveur n'est plus en maintenance"));
			messageChannel.send(Main.addColor("{{RED}}Maintenance finish"));
			Main.sendmessage("{{RED}}Le serveur n'est plus en maintenance", "console");
		}
		return CommandResult.success();
	}

}
