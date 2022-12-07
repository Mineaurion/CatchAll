package com.mineaurion.catchall.forge.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.command.arguments.ItemParser;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextParser {


    public static IFormattableTextComponent buildtext(String string)
    {
        String[] splitRaw = splitWithDelimiters(string, "(<.*?>)");
        IFormattableTextComponent message = new StringTextComponent("");
        for (String sub : splitRaw)
        {
            if (sub.matches("<.*?>")) {
                try{
                    message.append(textparser(sub));
                } catch (MalformedURLException | CommandSyntaxException e){
                    e.printStackTrace();
                }
            } else {
                message.append(new StringTextComponent(sub.replace("&", "§")));
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

    public static IFormattableTextComponent textparser(String message) throws MalformedURLException, CommandSyntaxException {
        message = message.replace("<", "").replace(">", "");
        String[] split = message.split("\\|");
        if(split.length != 4){
            return new StringTextComponent(message.replace("&", "§")).setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new StringTextComponent("§4Malformed"))));
        }
        return getFinalMsg(split[0], split[1], split[2], split[3]);
    }
    public static IFormattableTextComponent getFinalMsg(String split0, String split1, String split2, String split3) throws CommandSyntaxException {
        IFormattableTextComponent finlmsg;
        if(split0.contains("[item]")){
            finlmsg = new StringTextComponent(split0.replace("[item]", itemToStack(split1.replace("item:", "")).getDisplayName().getString()).replace("&", "§"));
        }else{
            finlmsg = new StringTextComponent(split0.replace("&", "§"));
        }
        return finlmsg.setStyle(getStyle(split0, split1, split2, split3));
    }
    public static Style getStyle(String split0, String split1, String split2, String split3) throws CommandSyntaxException {
        Style style = Style.EMPTY
                .withHoverEvent(getHover(split0, split1))
                .withClickEvent(getClick(split2, split3));
        return style;
    }


    public static ClickEvent getClick(String split2, String split3){
        if((split2).equals("cmd")){
            return new ClickEvent(ClickEvent.Action.RUN_COMMAND, split3);
        }else if((split2).equals("url")){
            return new ClickEvent(ClickEvent.Action.OPEN_URL, split3);
        }else if((split2).equals("copy")){
            return new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, split3);
        }else{
            return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, split3);
        }
    }

    public static HoverEvent getHover(String split0,String split1) throws CommandSyntaxException {
        String hoverstring = "";
        if(split1.contains("\\n")){
            String[] rtline = split1.split("\\\\n");
            for(String sub : rtline){
                hoverstring = hoverstring + sub + "\n";
            }
        }else {
            hoverstring = split1;
        }
        hoverstring = hoverstring.trim();
        if(split1.startsWith("item:")){
            return new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemHover(itemToStack(split1.replace("item:", ""))));
        }else{
            return new HoverEvent(HoverEvent.Action.SHOW_TEXT,new StringTextComponent(hoverstring.replace("&", "§")));
        }
    }
    private static ItemStack itemToStack(String itemname) throws CommandSyntaxException {
        ItemParser itmprs = (new ItemParser(new StringReader(itemname), false)).parse();
        ItemInput itmnpt = new ItemInput(itmprs.getItem(), itmprs.getNbt());
        return itmnpt.createItemStack(1, true);
    }

    public static Style getCustomStyle(HoverEvent customhoverevent, ClickEvent customclickevent) throws CommandSyntaxException {
        Style style = Style.EMPTY
                .withHoverEvent(customhoverevent)
                .withClickEvent(customclickevent);
        return style;
    }

}
