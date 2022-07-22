package com.mineaurion.catchall.sponge.commands;

import com.mineaurion.catchall.common.QueryServer;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class GlistCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        try {
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(QueryServer.getServers()));
            return CommandResult.success();
        } catch (Exception e){
            src.sendMessage(Text.of(e.getMessage()));
            return CommandResult.empty();
        }
    }
}
