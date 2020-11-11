package com.mineaurion.catchall.commands;

import com.mineaurion.catchall.CatchAllSpigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GListCommand implements CommandExecutor {
    private CatchAllSpigot main;

    public GListCommand() {
        this.main = CatchAllSpigot.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            URL url = new URL("http://api.mineaurion.com/v1/serveurs");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            String response = con.getResponseMessage();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            sender.sendMessage(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }
}
