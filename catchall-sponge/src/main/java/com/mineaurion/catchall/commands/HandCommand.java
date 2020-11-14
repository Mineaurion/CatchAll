package com.mineaurion.catchall.commands;

import com.mineaurion.catchall.CatchAllSponge;
import me.rojo8399.placeholderapi.PlaceholderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import java.util.Collection;
import java.util.Optional;


public class HandCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("This command is reserved for player... Sorry!"));
            return CommandResult.empty();
        }

        CatchAllSponge plugin = CatchAllSponge.getInstance();
        String rawText_FR = "&6* %player_name% &5vous montre&c %player_item_in_main_hand%";
        String rawText_EN = "&6* %player_name% &5show to you&c %player_item_in_main_hand%";

        Text text_FR = TextSerializers.FORMATTING_CODE.deserialize(rawText_FR);
        Text text_EN = TextSerializers.FORMATTING_CODE.deserialize(rawText_EN);

        if (Sponge.getPluginManager().getPlugin("placeholderapi").isPresent()) {
            Optional<PlaceholderService> service = Sponge.getGame().getServiceManager()
                    .provide(PlaceholderService.class);
            if (service.isPresent()) {
                PlaceholderService placeholderService = service.get();
                text_FR = placeholderService.replacePlaceholders(text_FR, src, src);
                text_EN = placeholderService.replacePlaceholders(text_EN, src, src);
            }
        }

        Collection<Player> players = Sponge.getServer().getOnlinePlayers();

        for (Player player : players) {
            if (player.hasPermission("mineaurion.announce.fr"))
                player.sendMessage(text_FR);
            else
                player.sendMessage(text_EN);
        }

        return CommandResult.success();
    }
}
