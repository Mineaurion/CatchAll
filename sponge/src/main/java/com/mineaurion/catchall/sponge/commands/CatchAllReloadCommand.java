package com.mineaurion.catchall.sponge.commands;

import com.mineaurion.catchall.sponge.CatchAll;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class CatchAllReloadCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        CatchAll plugin = CatchAll.getInstance();
        plugin.reload();

        Text result = Text.of("Plugin reloaded");

        if (src instanceof Player) {
            Player player = (Player) src;
            player.sendMessage(result);
        }

        plugin.sendMessage(Text.of(result));
        return CommandResult.success();
    }
}
