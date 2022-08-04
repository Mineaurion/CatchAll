package com.mineaurion.catchall.bukkit.commands;

import com.mineaurion.catchall.bukkit.CatchAll;
import com.mineaurion.catchall.common.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DonateurCommand implements CommandExecutor {
    private final CatchAll main;

    public DonateurCommand() {
        this.main = CatchAll.getInstance();
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
                if (!p.hasPermission(Config.Donateur.permission)) {
                    p.kickPlayer("Server is now in donator only mode. Try later please");
                    countPlayersKicked++;
                }
            }
            Bukkit.broadcastMessage(countPlayersKicked + " player(s) has been kicked from the server");
        }
        return true;
    }
}
