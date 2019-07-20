package com.mineaurion.catchall;

import com.mineaurion.catchall.commands.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {
    private CatchAllSponge plugin;

    public CommandManager() {
        plugin = CatchAllSponge.getInstance();
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
                .executor(new CatchAllCommand())
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
                .executor(new CatchAllCommand())
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
                    GenericArguments.remainingJoinedStrings(Text.of("message"))
                )
                .build();

        // LastLogin
        CommandSpec lastlogin = CommandSpec.builder()
                .description(Text.of(""))
                .permission("catchall.cmd.lastlogin")
                .executor(new LastLoginCommand())
                .arguments(
                        GenericArguments.optional(GenericArguments.integer(Text.of("minMonth"))),
                        GenericArguments.optional(GenericArguments.integer(Text.of("maxMonth")))
                )
                .build();

        register(catchall, "catchall");
        register(donateur, "donateur");
        register(maintenance, "maintenance");
        register(webhook, "webhook");
        register(lastlogin, "lastlogin");
    }
}
