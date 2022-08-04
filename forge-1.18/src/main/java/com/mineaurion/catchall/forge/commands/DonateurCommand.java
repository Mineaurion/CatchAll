package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.common.Config;
import com.mineaurion.catchall.forge.CatchAll;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class DonateurCommand {
    public DonateurCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        this.register(dispatcher);
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("donateur")
                        .requires(commandSource -> commandSource.hasPermission(3))
                        .executes(context -> {
                            int count = 0;
                            boolean currentState = CatchAll.config.states.donateur.get();
                            CatchAll.config.states.donateur.set(!currentState);
                            String message = "Server state update : donator mode is now " + ((currentState) ? "disable" : "enable");
                            context.getSource().getServer().getPlayerList().broadcastMessage(new TextComponent(message), ChatType.SYSTEM, Util.NIL_UUID);
                            if(!currentState){
                                for(ServerPlayer serverPlayer: context.getSource().getServer().getPlayerList().getPlayers()){
                                    if(!CatchAll.hasPermission(serverPlayer, Config.Donateur.permission)){
                                        serverPlayer.connection.disconnect(new TextComponent("Server is now in donator only mode. Try later please"));
                                        count++;
                                    }
                                }
                            }
                            context.getSource().getServer().getPlayerList().broadcastMessage(new TextComponent(count + " player(s) has been kicked from the server"), ChatType.SYSTEM, Util.NIL_UUID);
                            return 1;
                        })
        );
    }
}
