package com.mineaurion.catchall.commands;

import com.mineaurion.catchall.CatchAllSponge;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


public class LastLoginCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        // Somethings usefull huhu
        CatchAllSponge plugin = CatchAllSponge.getInstance();
        UserStorageService userStorage = Sponge.getServiceManager().provide(UserStorageService.class).get();
        String filename = "lastlogin";
        // Args
        Integer minMonth = args.<Integer>getOne("minMonth").orElse(0);
        Integer maxMonth = args.<Integer>getOne("maxMonth").orElse(2);
        // Im kind hihi
        if (minMonth > maxMonth) {
            Integer tmp = minMonth;
            minMonth = maxMonth;
            maxMonth = tmp;
        }
        // Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -minMonth);
        long minTs = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, -maxMonth + minMonth);
        long maxTs = calendar.getTimeInMillis();
        // Loop all offline players! shiiiiiit
        List<String> players = new ArrayList<String>();
        Collection<GameProfile> profiles = userStorage.getAll();
        for (GameProfile profile : profiles) {
            Optional<User> optionalUser = userStorage.get(profile);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Instant lastPlayed = user.get(Keys.LAST_DATE_PLAYED).get();
                if (lastPlayed.toEpochMilli() <= minTs && lastPlayed.toEpochMilli() >= maxTs)
                    players.add(user.getName());
            }
        }

        if (players.isEmpty())
            src.sendMessage(Text.of("No player matched"));
        // Write players list to file
        DateFormat outputDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String directory = plugin.configDir + File.separator + filename + "s";
        String pathname = directory + File.separator + filename + "_" + outputDateFormat.format(new Date()) + "_" + minMonth + "_" + maxMonth + ".yml";
        // Create dir if doesn't exist
        boolean created = new File(directory).mkdirs();
        if (created)
            plugin.sendMessage(Text.of("Lastlogin directory has been created!"));
        // Build result file (even if empty)
        File file = new File(pathname);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                src.sendMessage(Text.of("Can't create file, contact an admin"));
                e.printStackTrace();
                return CommandResult.empty();
            }
        }

        YAMLConfigurationLoader yml = YAMLConfigurationLoader.builder().setPath(file.toPath()).build();
        // Try to write inside the lastlogin file built
        try {
            ConfigurationNode node = yml.load();
            node.getNode("player").setValue(players);
            yml.save(node);
        } catch (IOException e) {
            src.sendMessage(Text.of("Can't save output file, contact an admin"));
            e.printStackTrace();
            return CommandResult.empty();
        }
        src.sendMessage(Text.of("Result of offline players between " + minMonth + " and " + maxMonth + " months ago  : " + players.size() + " player(s) find"));
        src.sendMessage(Text.of("File has been written in " + pathname));

        return CommandResult.success();
    }
}
