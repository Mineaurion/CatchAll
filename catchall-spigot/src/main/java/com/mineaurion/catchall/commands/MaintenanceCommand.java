package com.mineaurion.catchall.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.mineaurion.catchall.CatchAllSpigot;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import com.mrpowergamerbr.temmiewebhook.exceptions.WebhookException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.*;
import java.util.Arrays;


public class MaintenanceCommand implements CommandExecutor {
    private CatchAllSpigot main;

    public MaintenanceCommand() {
        this.main = CatchAllSpigot.getInstance();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0)
            return false;

        int countPlayersKicked = 0;
        boolean currentState;
        currentState = this.main.getConfig().getBoolean("states.maintenance");
        this.main.getConfig().set("states.maintenance", !currentState);
        this.main.saveConfig();
        Bukkit.broadcastMessage("Server state update : maintenance is now " + ((currentState) ? "disable" : "enable"));
        if (!currentState) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission("Maintenance.bypass")) {
                    p.kickPlayer("Server is now in maintenance. Try later please");
                    countPlayersKicked++;
                }
            }
            Bukkit.broadcastMessage(countPlayersKicked + " player(s) has been kicked from the server");
        }
        return true;
    }
}
