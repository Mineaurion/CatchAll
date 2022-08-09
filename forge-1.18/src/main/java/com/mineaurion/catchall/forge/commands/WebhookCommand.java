package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.common.parsers.DiscordMessageParser;
import com.mineaurion.catchall.forge.CatchAll;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.TextComponent;

public class WebhookCommand {

    public WebhookCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        this.register(dispatcher);
    }

    private void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
            Commands.literal("webhook")
                .requires(commandSource -> commandSource.hasPermission(4))
                .then(
                    Commands.argument("channel", IntegerArgumentType.integer())
                        .suggests((context, builder) -> {
                            for (int i = 0; i < CatchAll.config.webhook.discord.get().size(); i ++) {
                                int channel = i + 1;
                                builder.suggest(channel);
                            }
                            return builder.buildFuture();
                        })
                        .then(
                            Commands.argument("message", MessageArgument.message())
                                    .executes(ctx -> sendWebhook(
                                            ctx.getSource(),
                                            IntegerArgumentType.getInteger(ctx, "channel"),
                                            MessageArgument.getMessage(ctx, "message").getString()
                                    ))
                        )
                )
        );
    }

    private int sendWebhook(CommandSourceStack source, Integer channel, String message){
        DiscordMessageParser parser = new DiscordMessageParser(message.split(" "));
        try {
            int realChannel = channel - 1;
            parser.send(CatchAll.config.webhook.discord.get().get(realChannel));
            source.sendSuccess(new TextComponent("Message succesfully sent to channel [" + channel + "]"), true);
        } catch (Exception e){
            source.sendFailure(new TextComponent(e.getMessage()));
        }
        return 1;
    }
}
