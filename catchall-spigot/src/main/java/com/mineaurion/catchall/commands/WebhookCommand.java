package com.mineaurion.catchall.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.mineaurion.catchall.CatchAllSpigot;
import com.mineaurion.catchall.parsers.DiscordMessageParser;
import com.mrpowergamerbr.temmiewebhook.DiscordEmbed;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import com.mrpowergamerbr.temmiewebhook.exceptions.WebhookException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

        DiscordMessageParser parser = new DiscordMessageParser(Arrays.copyOfRange(args, 1, args.length));

        if (!parser.isValid()) {
            sender.sendMessage("Invalid discord webhook format");
            return true;
        }

        if (parser.isEmbed()) {
            HttpRequest.post(channel)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
                    .contentType("application/json")
                    .send(parser.getMessage())
                    .body();
        } else {
            TemmieWebhook webhook = new TemmieWebhook(channel);
            // Set Avatar and Sender name inside the webhook service
            DiscordMessage dm = DiscordMessage.builder()
                    .content(parser.getMessage())
                    .build();
            try {
                webhook.sendMessage(dm);
            } catch (WebhookException e) {
                sender.sendMessage("Internal webhook error, contact an administrator");
                return true;
            }
        }

        sender.sendMessage("Message succesfully sent to channel [" + args[0] + "]");
        return true;
    }

}
