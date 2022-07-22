package com.mineaurion.catchall.common.serializers;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ServerState {
    @SerializedName("name")
    private String host;

    @SerializedName("info")
    private String serverName;

    @SerializedName("players")
    private int count;

    @SerializedName("statut")
    private String status;

    @SerializedName("maxplayers")
    private int maxSlots;

    @SerializedName("joueurs")
    public Object onlinePlayersName;

    public ServerState() {

    }

    public String getHost() {
        return host;
    }

    public ServerState setHost(String host) {
        this.host = host;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public ServerState setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public int getCount() {
        return count;
    }

    public ServerState setCount(int count) {
        this.count = count;
        return this;
    }

    public Boolean isOn() {
        return this.status.equals("On");
    }

    public String getStatus() {
        return status;
    }

    public ServerState setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public ServerState setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
        return this;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getOnlinePlayersName() {
        if (!(this.onlinePlayersName instanceof ArrayList<?>)) {
            return new ArrayList<String>();
        }

        return (ArrayList<String>) this.onlinePlayersName;
    }

    public ServerState setOnlinePlayersName(Object onlinePlayersName) {
        this.onlinePlayersName = onlinePlayersName;
        return this;
    }
}
