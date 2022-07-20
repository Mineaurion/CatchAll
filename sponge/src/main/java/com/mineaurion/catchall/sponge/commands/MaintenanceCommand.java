package com.mineaurion.catchall.sponge.commands;

import com.mineaurion.catchall.sponge.CatchAll;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class MaintenanceCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        CatchAll plugin = CatchAll.getInstance();

        int countPlayersKicked = 0;
        boolean currentState = plugin.getConf().get("states", "maintenance").getBoolean();
        plugin.getConf().get("states", "maintenance").setValue(!currentState);
        plugin.getConf().save();

        Sponge.getServer().getBroadcastChannel().send(Text.of("Server state update : maintenance mode is now " + ((currentState) ? "disable" : "enable")));
        plugin.getLogger().info("Server state update : maintenance mode is now " + ((currentState) ? "disable" : "enable"));

        if (!currentState) {
            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                if (!p.hasPermission("Maintenance.bypass")) {
                    p.kick(Text.of("Server is now in maintenance only mode. Try later please"));
                    countPlayersKicked++;
                }
            }
            Sponge.getServer().getBroadcastChannel().send(Text.of(countPlayersKicked + " player(s) has been kicked from the server"));
        }
        return CommandResult.success();
    }
}
