package com.mineaurion.catchall.bukkit.commands;

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

        try {
            parser.send(channel);
            sender.sendMessage("Message successfully sent to channel [" + args[0] + "]");
        } catch (Exception e) {
            sender.sendMessage(e.getMessage());
        }

        return true;
    }

}
