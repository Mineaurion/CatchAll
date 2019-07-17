package com.mineaurion.catchall.commands;

import com.mineaurion.catchall.CatchAllSponge;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import com.mrpowergamerbr.temmiewebhook.exceptions.WebhookException;
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
            plugin.getLogger().info("This channel doesn't exist!");
            return CommandResult.empty();
        }

        String message = args.<String>getOne("message").get();
        if (message.isEmpty()) {
            src.sendMessage(Text.of("Invalide message ?!"));
            plugin.getLogger().info("Invalide message ?!");
            return CommandResult.empty();
        }

        TemmieWebhook webhook = new TemmieWebhook(channels.get(channel).getString());
        DiscordMessage dm = DiscordMessage.builder()
                .content(message)
                .build();
        try {
            webhook.sendMessage(dm);
        } catch (WebhookException e) {
            src.sendMessage(Text.of("Internal webhook error, contact an administrator"));
            plugin.getLogger().info("Internal webhook error, contact an administrator");
            return CommandResult.empty();
        }
        plugin.getLogger().info("Message succesfully sent to channel [" + channel + "]");
        src.sendMessage(Text.of("Message succesfully sent to channel [" + channel + "]"));
        return CommandResult.success();
    }
}
