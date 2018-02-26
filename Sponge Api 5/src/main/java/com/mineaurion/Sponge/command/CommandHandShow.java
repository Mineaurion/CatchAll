package com.mineaurion.Sponge.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import me.rojo8399.placeholderapi.PlaceholderService;

public class CommandHandShow implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String commandfr = "&6* %player_name% &5vous montre&c %player_item_in_main_hand%";
		String commanden = "&6* %player_name% &5show to you&c %player_item_in_main_hand%";
		
		Text Tcommandfr = TextSerializers.FORMATTING_CODE.deserialize(commandfr);
		Text Tcommanden = TextSerializers.FORMATTING_CODE.deserialize(commanden);
		
		if (Sponge.getPluginManager().getPlugin("placeholderapi").isPresent()) {
			Optional<PlaceholderService> service = Sponge.getGame().getServiceManager()
					.provide(PlaceholderService.class);
			if (service.isPresent()) {
				PlaceholderService placeholderService = service.get();
				Tcommandfr = placeholderService.replacePlaceholders(Tcommandfr, src, src);
				Tcommanden = placeholderService.replacePlaceholders(Tcommanden, src, src);
			}
		}
		Collection<Player> PlayerC = Sponge.getServer().getOnlinePlayers();
		Iterator<Player> iterator = PlayerC.iterator();
		while (iterator.hasNext()){
			Player player = iterator.next();
			if(player.hasPermission("mineaurion.announce.fr")){
				player.sendMessage(Tcommandfr);
			}else{
				player.sendMessage(Tcommanden);
			}
		}
		return CommandResult.success();
	}

}
