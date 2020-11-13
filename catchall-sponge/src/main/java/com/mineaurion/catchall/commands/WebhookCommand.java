package com.mineaurion.catchall.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.mineaurion.catchall.CatchAllSponge;
import com.mineaurion.catchall.parsers.DiscordMessageParser;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Map;

public class WebhookCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        CatchAllSponge plugin = CatchAllSponge.getInstance();
        String channel = args.<String>getOne("channel").get();

        Map<Object, ? extends CommentedConfigurationNode> channels = plugin.getConf().get("webhook", "discord").getChildrenMap();

        if (channel.isEmpty() || !channels.containsKey(channel)) {
            src.sendMessage(Text.of("This channel doesn't exist!"));
            return CommandResult.empty();
        }

        String message = args.<String>getOne("message").get();
        if (message.isEmpty()) {
            src.sendMessage(Text.of("Invalid discord webhook format"));
            return CommandResult.empty();
        }

        DiscordMessageParser parser = new DiscordMessageParser(message);

        HttpRequest.post(channel)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
                .contentType("application/json")
                .send(parser.getMessage())
                .body();

        src.sendMessage(Text.of("Message succesfully sent to channel [" + channel + "]"));
        return CommandResult.success();
    }
}
