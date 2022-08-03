package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.common.QueryServer;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class GlistCommand {

    public GlistCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        this.register(dispatcher);
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("glist")
                        .executes(context -> {
                            try {
                                context.getSource().sendSuccess(new TextComponent(QueryServer.getServers().replace("&", "§")), false);
                            } catch (Exception e){
                                context.getSource().sendFailure(new TextComponent(e.getMessage()));
                            }
                            return 1;
                        })
        );
    }
}
