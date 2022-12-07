package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.forge.config.ConfigPage;
import com.mineaurion.catchall.forge.utils.Page;
import com.mineaurion.catchall.forge.utils.TextParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

import java.util.Collection;

public class MenupageCommand {

    public MenupageCommand(CommandDispatcher<CommandSource> dispatcher){
        LiteralCommandNode<CommandSource> literalcommandnode = dispatcher.register(Commands.literal("menupage")
                .then(Commands.argument("page", StringArgumentType.string()).suggests((cntxt, builder) -> {
                            ConfigPage.getPage().forEach((name, page) -> builder.suggest(name));
                            return builder.buildFuture();
                        })
                        .executes((cmdcntxt) -> {
                            String s = StringArgumentType.getString(cmdcntxt, "page");
                            ServerPlayerEntity player = cmdcntxt.getSource().getPlayerOrException();
                            createPage(player, ConfigPage.getPage().get(s));
                            return 1;
                        })
                        .then(Commands.argument("player", EntityArgument.players()).executes(context -> {
                            String s = StringArgumentType.getString(context, "page");
                            Collection<ServerPlayerEntity> clctplayer = EntityArgument.getPlayers(context, "player");
                            for(ServerPlayerEntity player : clctplayer){
                                createPage(player, ConfigPage.getPage().get(s));
                            }
                            return 1;
                        }))));
        dispatcher.register(Commands.literal("mpage").redirect(literalcommandnode));
    }

    public static boolean createPage(ServerPlayerEntity player, Page page) {
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
