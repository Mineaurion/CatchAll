package com.mineaurion.catchall.bukkit.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.mineaurion.catchall.bukkit.CatchAll;
import com.mineaurion.catchall.common.parsers.DiscordMessageParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;


public class WebhookCommand implements CommandExecutor {
    private final CatchAll main;

    public WebhookCommand() {
        this.main = CatchAll.getInstance();
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

        HttpRequest.post(channel)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
                .contentType("application/json")
                .send(parser.getMessage())
                .body();

        sender.sendMessage("Message succesfully sent to channel [" + args[0] + "]");
        return true;
    }

}
