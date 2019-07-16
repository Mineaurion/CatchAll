package com.mineaurion.catchall.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import me.clip.placeholderapi.PlaceholderAPI;

public class MChatCommand implements CommandExecutor {
    private CatchAllSpigot main;

    public MChatCommand() {
        this.main = CatchAllSpigot.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
        if (args.length < 2)
            return false;

        boolean uniquePlayer = true;
        Player receiver = Bukkit.getPlayer(args[0]);

        if (args[0].equals("@@"))
            uniquePlayer = false;
        else if (receiver != null) {
            if (!receiver.isValid() || !receiver.isOnline()) {
                sender.sendMessage(args[0] + " is an invalid player name (maybe offline or not valid)");
                return true;
            }
        } else {
            sender.sendMessage("Invalid target argument");
            return true;
        }

        String name = (sender instanceof Player) ? sender.getName() : "Console";
        String raw = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        //String msg = msgBuilder.toString().replace("&", "ยง");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && ((sender instanceof Player)))
        {
            raw = PlaceholderAPI.setPlaceholders((Player)sender, raw);
            name = PlaceholderAPI.setPlaceholders((Player)sender, name);
        }

        String[] splitRaw = splitWithDelimiters(raw, "(<.*?>)");
        TextComponent message = new TextComponent("");
        for (String sub : splitRaw) {
            TextComponent extra = new TextComponent("");
            if (sub.matches(("<.*?>"))) {
                message.addExtra(extraHref(sub));

            } else
                extra.setText(sub);
            message.addExtra(extra);
        }
        if (uniquePlayer)
            receiver.spigot().sendMessage(message);
        else
            for (final Player p : Bukkit.getOnlinePlayers())
                p.spigot().sendMessage(message);

        this.main.sendMessage("Message sent by " + name + " to " + ((uniquePlayer) ? receiver.getName() : "all players connected"));
        this.main.sendMessage("Content : " + raw);

        return true;
    }

    private TextComponent extraHref(String sub) {
        TextComponent extra = new TextComponent("");
        //extra.setUnderlined(true);
        extra.setItalic(true);
        String[] split = sub.split("\\|");
        if (split.length != 3) {
            extra.setText(sub);
            return extra;
        }

        split[0] = split[0].substring(1);
        split[2] = split[2].substring(0, split[2].length() - 1);

        extra.setColor(ChatColor.valueOf(this.main.getConfig().getString("mchat.href_color")));
        extra.setText(split[0]);
        extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(split[1]).create()));
        extra.setClickEvent(new ClickEvent(split[2].startsWith("http") ? ClickEvent.Action.OPEN_URL : ClickEvent.Action.RUN_COMMAND, split[2]));

        return extra;
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