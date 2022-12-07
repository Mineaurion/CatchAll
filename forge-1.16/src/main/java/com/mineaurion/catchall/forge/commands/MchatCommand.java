package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.forge.utils.TextParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

import java.util.Collection;

public class MchatCommand {

    public MchatCommand(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("mchat")
                .requires((p_138116_) -> p_138116_.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.argument("action", StringArgumentType.greedyString())
                                .executes((cmdcntxt) -> {
                                    String s = StringArgumentType.getString(cmdcntxt, "action");
                                    Collection<ServerPlayerEntity> clctplayer = EntityArgument.getPlayers(cmdcntxt, "player");
                                    for(ServerPlayerEntity player : clctplayer){
                                        player.sendMessage(TextParser.buildtext(s), Util.NIL_UUID);
                                    }

                                    return 1;
                                }))));
    }

}
