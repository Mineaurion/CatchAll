package com.mineaurion.catchall.sponge.commands;

import com.mineaurion.catchall.sponge.CatchAll;
import me.rojo8399.placeholderapi.PlaceholderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MChatCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        CatchAll plugin = CatchAll.getInstance();

        String target = args.<String>getOne("target").get();
        String content = args.<String>getOne("content").get();

        boolean uniquePlayer = true;


        Optional<Player> receiver = Sponge.getServer().getPlayer(target);

        if (target.equals("@@")){
            uniquePlayer = false;
        } else if (receiver.isPresent()) {
            if (!receiver.get().isOnline()) {
                src.sendMessage(Text.of(target + " is an invalid player name (maybe offline or not valid)"));
                return CommandResult.empty();
            }
        } else {
            src.sendMessage(Text.of("Invalid target argument"));
            return CommandResult.empty();
        }

        String name = (src instanceof Player) ? src.getName() : "Console";
        String raw = content;
        //String msg = msgBuilder.toString().replace("&", "ยง");
        if (Sponge.getPluginManager().getPlugin("placeholderapi").isPresent()) {
            Optional<PlaceholderService> service = Sponge.getGame().getServiceManager()
                    .provide(PlaceholderService.class);
            if (service.isPresent()) {
                PlaceholderService placeholderService = service.get();
                raw = placeholderService.replacePlaceholders(Text.of(raw), src, src).toPlain();
                name = placeholderService.replacePlaceholders(Text.of(name), src, src).toPlain();
            }
        }


        String[] splitRaw = splitWithDelimiters(raw, "(<.*?>)");
        Text.Builder message = Text.builder("");
        for (String sub : splitRaw) {
            Text.Builder extra = Text.builder("");
            if (sub.matches(("<.*?>"))) {
                message.append(extraHref(sub));
            } else {
                extra.append(Text.of(sub));
            }
            message.append(extra.build());
        }
        if (uniquePlayer){
            receiver.get().sendMessage(message.build());
        } else {
            for (final Player p : Sponge.getServer().getOnlinePlayers()){
                p.sendMessage(message.build());
            }
        }

        plugin.sendMessage(Text.of("Message sent by " + name + " to " + ((uniquePlayer) ? receiver.get().getName() : "all players connected")));
        plugin.sendMessage(Text.of("Content : " + raw));

        return CommandResult.success();
    }

    private Text extraHref(String sub) {
        Text.Builder extra = Text.builder("");

        String[] split = sub.split("\\|");
        if (split.length != 3) {
            extra.append(Text.builder(sub).style(TextStyles.ITALIC).build());
            return extra.build();
        }

        split[0] = split[0].substring(1);
        split[2] = split[2].substring(0, split[2].length() - 1);


        try {
            extra.append(
                    Text.builder(split[0])
                            .onHover(TextActions.showText(Text.of(split[1])))
                            .onClick(split[2].startsWith("http") ? TextActions.openUrl(new URL(split[2])) : TextActions.runCommand(split[2]))
                            .style(TextStyles.ITALIC)
                            .color(TextColors.GOLD)
                            .build());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return extra.build();
    }

    private String[] splitWithDelimiters(String str, String regex) {
        List<String> parts = new ArrayList<>();

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        int lastEnd = 0;
        while (m.find()) {
            int start = m.start();
            if (lastEnd != start) {
                String nonDelim = str.substring(lastEnd, start);
                parts.add(nonDelim);
            }
            String delim = m.group();
            parts.add(delim);

            lastEnd = m.end();
        }

        if (lastEnd != str.length()) {
            String nonDelim = str.substring(lastEnd);
            parts.add(nonDelim);
        }

        return parts.toArray(new String[]{});
    }
}
