package com.mineaurion.catchall.forge.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.UUID;

public class HandCommand {
    public HandCommand(CommandDispatcher<CommandSource> dispatcher){

        dispatcher.register(Commands.literal("hand")
                .executes((cmdcntxt) -> {
                    ServerPlayerEntity player = cmdcntxt.getSource().getPlayerOrException();
                    UUID uuid = player.getUUID();
                    ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);
                    player.broadcastCarriedItem();
                    MinecraftServer minecraftserver = cmdcntxt.getSource().getServer();

                    IFormattableTextComponent message = new StringTextComponent("");



                    ITextComponent pname = new StringTextComponent(player.getDisplayName().getString());
                    Style styleplayer = Style.EMPTY
                            .applyFormat(TextFormatting.DARK_AQUA)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityHover(EntityType.PLAYER, uuid, pname)));
                    IFormattableTextComponent formpname = new StringTextComponent(player.getDisplayName().getString()).setStyle(styleplayer);

                    Style styleextra = Style.EMPTY
                            .applyFormat(TextFormatting.YELLOW)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemHover(stack)));
                    IFormattableTextComponent forminame = new StringTextComponent(stack.getDisplayName().getString()).setStyle(styleextra);

                    message.append(formpname);
                    message.append(new StringTextComponent(" §3shows "));
                    message.append(forminame);


                    minecraftserver.getPlayerList().broadcastMessage(message, ChatType.CHAT, Util.NIL_UUID);
                    return 1;
                }));
    }
}
