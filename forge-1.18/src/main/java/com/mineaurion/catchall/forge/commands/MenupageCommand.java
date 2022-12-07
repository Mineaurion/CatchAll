package com.mineaurion.catchall.forge.commands;


import com.mineaurion.catchall.forge.utils.TextParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mineaurion.catchall.forge.utils.Page;
import com.mineaurion.catchall.forge.config.ConfigPage;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;


public class MenupageCommand {

    public MenupageCommand(CommandDispatcher<CommandSourceStack> dispatcher){
            LiteralCommandNode<CommandSourceStack> literalcommandnode = dispatcher.register(Commands.literal("menupage")
                    .then(Commands.argument("page", StringArgumentType.string()).suggests((cntxt, builder) -> {
                                ConfigPage.getPage().forEach((name, page) -> builder.suggest(name));
                                return builder.buildFuture();
                            })
                            .executes((cmdcntxt) -> {
                                String s = StringArgumentType.getString(cmdcntxt, "page");
                                ServerPlayer player = cmdcntxt.getSource().getPlayerOrException();
                                createPage(player, ConfigPage.getPage().get(s));
                                return 1;
                            })
                            .then(Commands.argument("player", EntityArgument.players()).executes(context -> {
                                String s = StringArgumentType.getString(context, "page");
                                Collection<ServerPlayer> clctplayer = EntityArgument.getPlayers(context, "player");
                                for(ServerPlayer player : clctplayer){
                                    createPage(player, ConfigPage.getPage().get(s));
                                }
                                return 1;
                            }))));
            dispatcher.register(Commands.literal("mpage").redirect(literalcommandnode));
        }

        public static boolean createPage(ServerPlayer player, Page page) {
            String message = (page.getTitle() + "\n")
                    + (page.getHeader() + "\n");
            for (String s : page.getContent()){
                message = message + (s + "\n");
            }
            message = message + page.getFooter();
            player.sendMessage(TextParser.buildtext(message), Util.NIL_UUID);
            return true;
        }

    }

