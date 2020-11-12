package com.mineaurion.catchall.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mineaurion.catchall.CatchAllSpigot;
import com.mineaurion.catchall.serializers.ServerState;
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

        if (responseCode != 200) {
            sender.sendMessage("Mineaurion api call failed");
            return true;

        }

        final String responseBody = request.body();

        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        ServerState[] serverStates = gson.fromJson(responseBody, ServerState[].class);

        for (ServerState serverState : serverStates) {
            sender.sendMessage(serverState.getServerName());
        }

        return true;
    }
}
