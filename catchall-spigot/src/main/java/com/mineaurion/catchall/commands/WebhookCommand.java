package com.mineaurion.catchall.commands;

import com.mineaurion.catchall.CatchAllSpigot;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import com.mrpowergamerbr.temmiewebhook.exceptions.WebhookException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.*;
import java.util.Arrays;


public class WebhookCommand implements CommandExecutor {
    private CatchAllSpigot main;

    public WebhookCommand() {
        this.main = CatchAllSpigot.getInstance();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        String channel = this.main.getConfig().getString("webhook.discord." + args[0]);
        if (channel == null) {
            sender.sendMessage("This channel doesn't exist!");
            return true;
        }
        String[] message = Arrays.copyOfRange(args, 1, args.length);
        TemmieWebhook webhook = new TemmieWebhook(channel);
        // Set Avatar and Sender name inside the webhook service
        DiscordMessage dm = DiscordMessage.builder()
                .content(String.join(" ", message))
                .build();
        try {
            webhook.sendMessage(dm);
        } catch (WebhookException e) {
            sender.sendMessage("Internal webhook error, contact an administrator");
            return true;
        }
        sender.sendMessage("Message succesfully sent to channel [" + args[0] + "]");
        return true;
    }

}
