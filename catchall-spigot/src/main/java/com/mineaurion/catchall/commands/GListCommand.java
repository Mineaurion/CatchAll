package com.mineaurion.catchall.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mineaurion.catchall.CatchAllSpigot;
import com.mineaurion.catchall.serializers.ServerState;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        final ServerState[] serverStates = gson.fromJson(responseBody, ServerState[].class);

        int totalOnlineCount = 0;

        sender.sendMessage("States of all servers :");

        for (ServerState serverState : serverStates) {
            totalOnlineCount += serverState.getCount();

            StringBuilder serverInfo = new StringBuilder();

            serverInfo
                    .append("- ")
                    .append(ChatColor.BOLD)
                    .append(serverState.isOn() ? ChatColor.GREEN : ChatColor.RED)
                    .append(serverState.getServerName())
                    .append(ChatColor.GOLD)
                    .append(" (")
                    .append(serverState.getCount() + "/" + serverState.getMaxSlots())
                    .append(") \n")
                    .append(ChatColor.WHITE)
            ;

            sender.sendMessage(serverInfo.toString());

            boolean loopFirst = false;

            TextComponent playersNameInteractive = new TextComponent("");

            for (String playerName : serverState.getOnlinePlayersName()) {
                if (!loopFirst) {
                    loopFirst = true;
                } else {
                    TextComponent comma = new TextComponent(", ");
                    comma.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                    playersNameInteractive.addExtra(comma);
                }

                TextComponent extra = new TextComponent(playerName);
                extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpo " + playerName));
                extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Teleport on " + playerName).create()));
                extra.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                playersNameInteractive.addExtra(extra);
            }

            if (sender instanceof Player) {
                ((Player)sender).spigot().sendMessage(playersNameInteractive);
            } else {
                sender.sendMessage(playersNameInteractive.toLegacyText());
            }

        }

        sender.sendMessage("Total online players : " + totalOnlineCount);

        return true;
    }
}
