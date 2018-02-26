package com.mineaurion.Sponge.command;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import com.mineaurion.Sponge.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class commandlastlogin implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		int mois = args.<Integer>getOne("mois").orElse(1);
		List<String> Player = new ArrayList<String>();
		File folder = new File("world/stats/");
		
		for (final File file : folder.listFiles()) {
			if (!file.isDirectory()) {
				UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
				
				Optional<User> user = service.get(file.getName().replace(".json", ""));
				if (user.isPresent()) {
					
					Instant lastlog = user.get().get(Keys.LAST_DATE_PLAYED).get();
					Calendar moisdernier = Calendar.getInstance();
					moisdernier.add(Calendar.MONTH, -1 * mois);

					Calendar DateLog = Calendar.getInstance();
					DateLog.setTime(Date.from(lastlog));
					
					if (DateLog.before(moisdernier)) {
						Player.add(user.get().getName() + " : " + file.getName());
					}
				}
			}
		}
		File dataFile = new File(Main.getInstance().defaultConfig+"/PlayerAFK.conf");
		HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(dataFile.toPath()).build();
		try {
			CommentedConfigurationNode node = loader.load();
			node.getNode("Player").setValue(Player);
			loader.save(node);
		} catch (IOException e) {
			Main.sendmessage("{{RED}}Une erreur est survenu lors de la création de fichier", src.getName());
			e.printStackTrace();
		}
		
		
		return CommandResult.success();
	}

}
