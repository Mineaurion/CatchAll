package com.mineaurion.Sponge.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.animal.Animal;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.mineaurion.Sponge.Main;

public class CommandPampersss implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player player = (Player) src;
		Location<World> loc = player.getLocation();
		String type = args.<String>getOne("type").orElse("non");
		int radius = args.<Integer>getOne("radius").orElse(20);
		int removeditem = 0;
		int removedmob = 0;
		int removedanimal = 0;
		
		if (!player.hasPermission("mineaurion.pampersss.radius")) {
			radius = 20;
		}
		
		if (type.equalsIgnoreCase("all") && player.hasPermission("mineaurion.pampersss.all")) {
			for (Entity entity : loc.getExtent().getEntities()) {
				if(entity instanceof Player) { continue;}
				if (entity.getLocation().getBlockPosition().distance(loc.getBlockPosition()) < radius) {
					entity.remove();
					removeditem++;
				}
			}
			player.sendMessage(Main.addColor("{{GRAY}}" + removeditem + " entités supprimés"));
		} else {
			for (Entity entity : loc.getExtent().getEntities()) {
				if (entity instanceof Item) {
					if (entity.getLocation().getBlockPosition().distance(loc.getBlockPosition()) < radius) {
						entity.remove();
						removeditem++;
					}
				}
				if (entity instanceof Monster) {
					if (entity.getLocation().getBlockPosition().distance(loc.getBlockPosition()) < radius) {
						entity.remove();
						removedmob++;
					}
				}
				if(type.equalsIgnoreCase("passif")) {
					if(entity instanceof Animal) {
						if (entity.getLocation().getBlockPosition().distance(loc.getBlockPosition()) < radius) {
							entity.remove();
							removedanimal++;
						}
					}
				}
				
				
			}
			player.sendMessage(Main.addColor("{{GRAY}}" + removeditem + " items supprimés et " + removedmob + " mobs supprimés et " + removedanimal+ " animaux supprimés"));
		}
		return CommandResult.success();
	}

}
