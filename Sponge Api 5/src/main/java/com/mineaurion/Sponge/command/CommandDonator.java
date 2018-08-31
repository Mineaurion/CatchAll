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

public class CommandDonator implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!Main.getInstance().Node.getNode("AccesDonateur").getBoolean()) {
			Main.getInstance().setValut(true, "AccesDonateur");
			MessageChannel messageChannel = MessageChannel.TO_PLAYERS;
			messageChannel.send(Main.addColor("{{RED}}Attention le serveur est maintenant réservé au donateur"));
			messageChannel.send(Main.addColor("{{RED}}Warning, the server is now reserved to donator"));
			Main.sendmessage("{{RED}}Attention le serveur est maintenant est réservé au donateur", "console");
			
			for(Iterator<Player> iter = Sponge.getServer().getOnlinePlayers().iterator(); iter.hasNext();) {
				Player p = iter.next();
				if(!p.hasPermission("mineaurion.donateur")) {
					p.kick(Text.of("Reserved to donator"));
				}
			}
		}else {
			Main.getInstance().setValut(false, "AccesDonateur");
			MessageChannel messageChannel = MessageChannel.TO_PLAYERS;
			messageChannel.send(Main.addColor("{{RED}}Le serveur n'est plus reservé au donateur"));
			messageChannel.send(Main.addColor("{{RED}}The server is open for all"));
			Main.sendmessage("{{RED}}Le serveur n'est plus reservé au donateur", "console");
		}
		return CommandResult.success();
	}

}
