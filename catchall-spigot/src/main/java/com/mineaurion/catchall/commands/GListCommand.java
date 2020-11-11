package com.mineaurion.catchall.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GListCommand implements CommandExecutor {
    private final CatchAllSpigot main;

    public GListCommand() {
        this.main = CatchAllSpigot.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        HttpRequest request = HttpRequest.get("http://api.mineaurion.com/v1/serveurs");

        int responseCode = request.code();

        if (responseCode == 200) {
            String responseBody = request.body();
            sender.sendMessage(responseBody);
        } else {
            sender.sendMessage("Mineaurion api call failed");
        }

        return true;
    }
}
