package com.mineaurion.catchall.sponge;

import com.mineaurion.catchall.sponge.commands.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {
    private CatchAll plugin;

    public CommandManager() {
        plugin = CatchAll.getInstance();
        registerAll();
    }

    private void register(CommandCallable callable, String... alias) {
        Sponge.getCommandManager().register(plugin, callable, alias);
        plugin.sendMessage(Text.of("Command : " + alias[0] + " loaded"));
    }

    private void registerAll() {
        // Catchall help
        CommandSpec catchall_help = CommandSpec.builder()
                .description(Text.of("Show all commands callable by CatchAll"))
                .permission("catchall.cmd.help")
                .executor((src, args) -> CommandResult.empty())
                .build();

        // Catchall reload
        CommandSpec catchall_reload = CommandSpec.builder()
                .description(Text.of("Reload the plugin"))
                .permission("catchall.cmd.reload")
                .executor(new CatchAllReloadCommand())
                .build();

        // Catchall (help)
        CommandSpec catchall = CommandSpec.builder()
                .description(Text.of("Show all commands callable by CatchAll"))
                .permission("catchall.cmd.help")
                .child(catchall_reload, "reload", "r")
                .child(catchall_help, "help", "h")
                .executor((src, args) -> CommandResult.empty())
                .build();

        // Donateur
        CommandSpec donateur = CommandSpec.builder()
                .description(Text.of("Enable/Disable the donator server status"))
                .permission("catchall.cmd.donateur")
                .executor(new DonateurCommand())
                .build();

        // Maintenance
        CommandSpec maintenance = CommandSpec.builder()
                .description(Text.of("Enable/Disable the donator server status"))
                .permission("catchall.cmd.donateur")
                .executor(new MaintenanceCommand())
                .build();

        // Webhook
        CommandSpec webhook = CommandSpec.builder()
                .description(Text.of("Send message under discord webhook"))
                .permission("catchall.cmd.webhook")
                .executor(new WebhookCommand())
                .arguments(
                    GenericArguments.string(Text.of("channel")),
                    GenericArguments.remainingRawJoinedStrings(Text.of("message"))
                )
                .build();

        // LastLogin
        CommandSpec lastlogin = CommandSpec.builder()
                .description(Text.of("Write a file with players offline between MIN and MAX months ago (default: 0 and 2 months)"))
                .permission("catchall.cmd.lastlogin")
                .executor(new LastLoginCommand())
                .arguments(
                        GenericArguments.optional(GenericArguments.integer(Text.of("minMonth"))),
                        GenericArguments.optional(GenericArguments.integer(Text.of("maxMonth")))
                )
                .build();

        // Hand
        CommandSpec hand = CommandSpec.builder()
                .description(Text.of(""))
                .permission("catchall.cmd.hand")
                .executor(new HandCommand())
                .build();

        // MChat
        CommandSpec mchat = CommandSpec.builder()
                .description(Text.of(""))
                .permission("catchall.cmd.mchat")
                .executor(new MChatCommand())
                .arguments(
                        GenericArguments.string(Text.of("target")),
                        GenericArguments.remainingJoinedStrings(Text.of("content"))
                )
                .build();

        CommandSpec glist = CommandSpec.builder()
                .description(Text.of(""))
                .permission("catchall.cmd.glist")
                .executor(new GlistCommand())
                .build();

        register(catchall, "catchall");
        register(donateur, "donateur");
        register(maintenance, "maintenance");
        register(webhook, "webhook");
        register(lastlogin, "lastlogin");
        register(hand, "hand");
        register(mchat, "mchat");
        register(glist, "glist");

    }
}
