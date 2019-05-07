package com.mineaurion.catchall.commands;

import com.github.kevinsawicki.http.HttpRequest;
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
            return false;
        }

        String[] message = Arrays.copyOfRange(args, 1, args.length);
        String author = this.main.getServer().getServerName() + " - " + sender.getName();
        TemmieWebhook webhook = new TemmieWebhook(channel);
        DiscordMessage dm = DiscordMessage.builder().username(author)
                .content(String.join(" ", message))
                .avatarUrl(this.main.getConfig().getString("webhook.avatar"))
                .build();
        try {
            webhook.sendMessage(dm);
        } catch (WebhookException e) {
            sender.sendMessage("Internal webhook error, contact an administrator");
            return false;
        }
        sender.sendMessage("Message succesfully sent to channel [" + args[0] + "]");
        return true;
    }
}
