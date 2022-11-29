package com.mineaurion.catchall.forge.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class HandCommand {

    public HandCommand(CommandDispatcher<CommandSourceStack> dispatcher){

        dispatcher.register(Commands.literal("hand")
                .executes((cmdcntxt) -> {
                    ServerPlayer player = cmdcntxt.getSource().getPlayerOrException();
                    UUID uuid = player.getUUID();
                    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                    MinecraftServer minecraftserver = cmdcntxt.getSource().getServer();

                    MutableComponent message = new TextComponent("");


                    MutableComponent pname = new TextComponent(player.getDisplayName().getString());
                    Style styleplayer = Style.EMPTY
                            .applyFormat(ChatFormatting.DARK_AQUA)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityTooltipInfo(EntityType.PLAYER, uuid, pname)));
                    MutableComponent formpname = new TextComponent(player.getDisplayName().getString()).setStyle(styleplayer);

                    Style styleextra = Style.EMPTY
                            .applyFormat(ChatFormatting.YELLOW)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack)))
                            .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,""));
                    MutableComponent forminame = new TextComponent(stack.getDisplayName().getString()).setStyle(styleextra);

                    message.append(formpname);
                    message.append(new TextComponent(" §3shows "));
                    message.append(forminame);


                    minecraftserver.getPlayerList().broadcastMessage(message, ChatType.CHAT, Util.NIL_UUID);
                    return 1;
                }));
    }
}
