package com.mineaurion.catchall.common;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.GsonBuilder;
import com.mineaurion.api.library.model.query.Server;

public class QueryServer {

    public QueryServer(){}

    public static String getServers() throws Exception {
        HttpRequest request = HttpRequest.get("https://api.mineaurion.com/query");
        if (request.code() != 200){
           throw new Exception("Mineaurion api call failed");
        }
        Server[] servers = new GsonBuilder().create().fromJson(request.body(), Server[].class);

        StringBuilder serversInfo = new StringBuilder();
        int totalPlayersOnline = 0;

        serversInfo.append("States of all servers :\n");
        for(Server server: servers){
            totalPlayersOnline += server.getOnlinePlayers();

            serversInfo
                    .append("- ")
                    .append("&l")
                    .append(server.isStatus() ? "&2" : "&4")
                    .append(server.getName())
                    .append("&6")
                    .append(" (")
                    .append(server.getOnlinePlayers()).append("/").append(server.getMaxPlayers())
                    .append(") \n")
                    .append("&f\n");
        }
        serversInfo.append("Total online players : ").append(totalPlayersOnline);

        return serversInfo.toString();
    }
}
