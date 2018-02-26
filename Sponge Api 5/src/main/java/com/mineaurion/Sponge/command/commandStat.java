package com.mineaurion.Sponge.command;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import com.mineaurion.Sponge.Main;



public class commandStat implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		String player = args.<String>getOne("player").get();
		String statcmd = args.<String>getOne("stat").orElse("");

		if (statcmd == "") {
			Main.sendmessage("{{RED}}Choose a good stat", src.getName());
		} else {
			UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
			Optional<User> user = service.get(player);
			
			String uuid = user.get().getPlayer().get().getUniqueId().toString();
			long stat = 0;
			try {
				JSONParser parser = new JSONParser();
				Reader reader = new FileReader("world/stats/" + uuid + ".json");
				Object jsonObj = parser.parse(reader);
				JSONObject jsonObject = (JSONObject) jsonObj;
				stat = (Long) jsonObject.get(statcmd);
				reader.close();
				Main.sendmessage(String.valueOf(stat), src.getName());
			} catch (IOException | ParseException e) {
				Main.sendmessage("{{RED}}Choose a good stat", src.getName());
				e.printStackTrace();
			}

		}
		return CommandResult.success();
	}
}
