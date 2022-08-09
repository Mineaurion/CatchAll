package com.mineaurion.catchall.sponge.commands;

import com.mineaurion.catchall.common.parsers.DiscordMessageParser;
import com.mineaurion.catchall.sponge.CatchAll;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Map;

public class WebhookCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        CatchAll plugin = CatchAll.getInstance();
        String channel = args.<String>getOne("channel").get();
        String message = args.<String>getOne("message").get();

        Map<Object, ? extends CommentedConfigurationNode> channels = plugin.getConf().get("webhook", "discord").getChildrenMap();

        if (channel.isEmpty() || !channels.containsKey(channel)) {
            src.sendMessage(Text.of("This channel doesn't exist!"));
            return CommandResult.empty();
        }

        DiscordMessageParser parser = new DiscordMessageParser(message);

        try {
            parser.send(channel);
            src.sendMessage(Text.of("Message succesfully sent to channel [" + channel + "]"));
            return CommandResult.success();
        } catch (Exception e){
            src.sendMessage(Text.of("Invalid discord webhook format"));
        }

        return CommandResult.empty();
    }
}
