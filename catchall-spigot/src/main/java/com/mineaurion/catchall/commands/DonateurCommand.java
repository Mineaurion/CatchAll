package com.mineaurion.catchall.commands;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DonateurCommand implements CommandExecutor {
    private final CatchAllSpigot main;

    public DonateurCommand() {
        this.main = CatchAllSpigot.getInstance();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0)
            return false;

        int countPlayersKicked = 0;
        boolean currentState;
        currentState = this.main.getConfig().getBoolean("states.donateur");
        this.main.getConfig().set("states.donateur", !currentState);
        this.main.saveConfig();
        Bukkit.broadcastMessage("Server state update : donator mode is now " + ((currentState) ? "disable" : "enable"));
        if (!currentState) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission("mineaurion.donateur")) {
                    p.kickPlayer("Server is now in donator only mode. Try later please");
                    countPlayersKicked++;
                }
            }
            Bukkit.broadcastMessage(countPlayersKicked + " player(s) has been kicked from the server");
        }
        return true;
    }
}
