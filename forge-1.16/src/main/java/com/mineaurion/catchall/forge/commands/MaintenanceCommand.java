package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.forge.CatchAll;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;

public class MaintenanceCommand {
    public MaintenanceCommand(CommandDispatcher<CommandSource> dispatcher){
        this.register(dispatcher);
    }

    public void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(
                Commands.literal("maintenance")
                        .requires(commandSource -> commandSource.hasPermission(3))
                        .executes(context -> {
                            int count = 0;
                            boolean currentState = CatchAll.config.states.maintenance.get();
                            CatchAll.config.states.maintenance.set(!currentState);
                            String message = "Server state update : maintenance mode is now " + ((currentState) ? "disable" : "enable");
                            context.getSource().getServer().getPlayerList().broadcastMessage(new StringTextComponent(message), ChatType.SYSTEM, Util.NIL_UUID);
                            if(!currentState){
                                for(ServerPlayerEntity serverPlayer: context.getSource().getServer().getPlayerList().getPlayers()){
                                    if(!CatchAll.hasPermission(serverPlayer, "maintenance.bypass")){
                                        serverPlayer.connection.disconnect(new StringTextComponent("Server is now in maintenance only mode. Try later please"));
                                        count++;
                                    }
                                }
                            }
                            context.getSource().getServer().getPlayerList().broadcastMessage(new StringTextComponent(count + " player(s) has been kicked from the server"), ChatType.SYSTEM, Util.NIL_UUID);
                            return 1;
                        })
        );
    }
}
