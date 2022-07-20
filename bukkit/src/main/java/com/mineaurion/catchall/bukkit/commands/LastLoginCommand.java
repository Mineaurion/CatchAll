package com.mineaurion.catchall.bukkit.commands;

import com.mineaurion.catchall.bukkit.CatchAll;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LastLoginCommand implements CommandExecutor {
    private final CatchAll main;


    public LastLoginCommand() {
        this.main = CatchAll.getInstance();
    }

    private int parseMonthArg(String arg) {
        int month = 0;

        try {
            month = Math.abs(Integer.parseInt(arg));
            if (month == Integer.MIN_VALUE)
                throw new NumberFormatException();
        } catch (NumberFormatException | ArithmeticException e) {
            return 0;
        }
        return month;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 2){
            return false;
        }

        String filename = "lastlogin";
        int minMonth = 0;
        int maxMonth = 2;
        long minTs;
        long maxTs;

        if (args.length >= 1) {
            minMonth = parseMonthArg(args[0]);
            if (minMonth == 0) {
                sender.sendMessage("Invalid arguments passed");
                return true;
            }
        }
        if (args.length == 2) {
            maxMonth = parseMonthArg(args[1]);
            if (maxMonth < minMonth + 1) {
                sender.sendMessage("Invalid arguments passed");
                return true;
            }
        }

        List<String> players = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -minMonth);
        minTs = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, -maxMonth + minMonth);
        maxTs = calendar.getTimeInMillis();

        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            long lastPlayed = p.getLastPlayed();
            if (lastPlayed <= minTs && lastPlayed >= maxTs) {
                players.add(p.getName());
            }
        }

        if (players.isEmpty()){
            sender.sendMessage("No player matched");
        }

        // Write players list to file
        DateFormat outputDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String pathname = main.getDataFolder() + File.separator + filename + "_" + outputDateFormat.format(new Date()) + "_" + minMonth + "_" + maxMonth + ".yml";

        File file = new File(pathname);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                sender.sendMessage("Can't create file, contact an admin");
                e.printStackTrace();
                return true;
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.set("players", players);
        try {
            yml.save(file);
        } catch (IOException e) {
            sender.sendMessage("Can't save output file, contact an admin");
            e.printStackTrace();
            return true;
        }

        sender.sendMessage("Result of offline players between " + minMonth + " and " + maxMonth + " months ago  : " + players.size() + " player(s) find");
        sender.sendMessage("File has been written in " + pathname);
        return true;
    }
}