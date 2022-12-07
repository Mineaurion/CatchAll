package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.forge.utils.TextParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;



    public class MchatCommand {

        public MchatCommand(CommandDispatcher<CommandSourceStack> dispatcher){
            dispatcher.register(Commands.literal("mchat")
                    .requires((p_138116_) -> p_138116_.hasPermission(2))
                    .then(Commands.argument("player", EntityArgument.players())
                            .then(Commands.argument("action", StringArgumentType.greedyString())
                                    .executes((cmdcntxt) -> {
                                        String s = StringArgumentType.getString(cmdcntxt, "action");
                                        Collection<ServerPlayer> clctplayer = EntityArgument.getPlayers(cmdcntxt, "player");
                                        for(ServerPlayer player : clctplayer){
                                            player.sendMessage(TextParser.buildtext(s), Util.NIL_UUID);
                                        }

                                        return 1;
                                    }))));
        }

    }