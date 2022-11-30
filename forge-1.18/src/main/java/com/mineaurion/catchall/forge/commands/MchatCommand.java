package com.mineaurion.catchall.forge.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MchatCommand {

    public MchatCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("mchat")
                .requires((p_138116_) -> p_138116_.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                .then(Commands.argument("action", StringArgumentType.greedyString())
                .executes((cmdcntxt) -> {
                    String s = StringArgumentType.getString(cmdcntxt, "action");
                    Collection<ServerPlayer> clctplayer = EntityArgument.getPlayers(cmdcntxt, "player");
                    for(ServerPlayer player : clctplayer){
                        player.sendMessage(buildtext(s), Util.NIL_UUID);
                    }

                    return 1;
                }))));
    }

    public static TextComponent buildtext(String string){
        String[] splitRaw = splitWithDelimiters(string, "(<.*?>)");
        TextComponent message = new TextComponent("");
        for(String sub : splitRaw){
            if (sub.matches("<.*?>")) {
                try{
                    message.append(textparser(sub));
                } catch (MalformedURLException e){
                    e.printStackTrace();
                }
            } else {
                message.append(new TextComponent(sub.replace("&", "§")));
            }
        }

        return message;
    }

    public static String[] splitWithDelimiters(String str, String regex)
    {
        List<String> parts = new ArrayList();

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);

        int lastEnd = 0;
        while (m.find())
        {
            int start = m.start();
            if (lastEnd != start)
            {
                String nonDelim = str.substring(lastEnd, start);
                parts.add(nonDelim);
            }
            String delim = m.group();
            parts.add(delim);

            lastEnd = m.end();
        }
        if (lastEnd != str.length())
        {
            String nonDelim = str.substring(lastEnd);
            parts.add(nonDelim);
        }
        return (String[])parts.toArray(new String[0]);
    }

    public static MutableComponent textparser(String message) throws MalformedURLException {
        message = message.replace("<", "").replace(">", "");
        String[] split = message.split("\\|");
        MutableComponent finlmsg = new TextComponent("");
        if(split.length != 4){
            MutableComponent component = new TextComponent(message.replace("&", "§")).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new TextComponent("§4Malformed"))));
            finlmsg.append(component);
            return finlmsg;
        }
        String hoverstring = "";
        if(split[1].contains("\\n")){
            String[] rtline = split[1].split("\\\\n");
            for(String sub : rtline){
                hoverstring = hoverstring + sub + "\n";
            }
        }else {
            hoverstring = split[1];
        }
        hoverstring = hoverstring.strip();
        finlmsg.append(new TextComponent(split[0].replace("&", "§")));
        Style style = Style.EMPTY
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new TextComponent(hoverstring.replace("&", "§"))))
                .withClickEvent((split[2]).equals("cmd") ? new ClickEvent(ClickEvent.Action.RUN_COMMAND, split[3]) : (split[2]).equals("url") ? new ClickEvent(ClickEvent.Action.OPEN_URL, split[3]) : new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, split[3]));
        finlmsg = finlmsg.setStyle(style);
        return finlmsg;
    }
}
