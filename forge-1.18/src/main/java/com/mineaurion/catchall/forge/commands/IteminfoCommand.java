package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.forge.utils.TextParser;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class IteminfoCommand {
    public IteminfoCommand(CommandDispatcher<CommandSourceStack> dispatcher){

        dispatcher.register(Commands.literal("infoitem")
                .executes((cmdcntxt) -> {
                    ServerPlayer player = cmdcntxt.getSource().getPlayerOrException();
                    UUID uuid = player.getUUID();
                    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                    MinecraftServer minecraftserver = cmdcntxt.getSource().getServer();
                    String message = "§6[<§aInfoItem|§dProvided by Gohu|cmd|Pouet>§6] "
                            + "§2Item: <§a[item]|item:" + stack.getItem().getRegistryName().toString() + stack.getTag() + "| | >" + "\n"
                            + "§6RegistryName: §e" + stack.getItem().getRegistryName().toString() + "\n"
                            + "§2RawNBT: " + "<§a§oClick to copy to clipboard|" + stack.getTag() + "|copy|" + stack.getTag() + ">" + "\n"
                            + "§6GiveCommand: <§e§nCommand|Click to get the Give Command| |/give " + player.getDisplayName().getString() + " " + stack.getItem().getRegistryName().toString() + stack.getTag() + " 1>";
                    player.sendMessage(TextParser.buildtext(message), Util.NIL_UUID);
                    return 1;
                }));
    }
}
