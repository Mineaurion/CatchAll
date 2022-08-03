package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.common.QueryServer;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class GlistCommand {

    public GlistCommand(CommandDispatcher<CommandSource> dispatcher){
        this.register(dispatcher);
    }

    public void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                Commands.literal("glist")
                        .executes(context -> {
                            try {
                                context.getSource().sendSuccess(new StringTextComponent(QueryServer.getServers().replace("&", "§")), false);
                            } catch (Exception e){
                                context.getSource().sendFailure(new StringTextComponent(e.getMessage()));
                            }
                            return 1;
                        })
        );
    }
}
