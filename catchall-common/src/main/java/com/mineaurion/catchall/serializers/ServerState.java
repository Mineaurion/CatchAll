package com.mineaurion.catchall.serializers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerState {
    @SerializedName("name")
    private String host;

    @SerializedName("info")
    private String serverName;

    @SerializedName("statut")
    private int status;

    @SerializedName("maxplayers")
    private int maxSlots;

    @SerializedName("joueurs")
    public List<String> playersName;

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

    public int getStatus() {
        return status;
    }

    public ServerState setStatus(int status) {
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

    public List<String> getPlayersName() {
        return playersName;
    }

    public ServerState setPlayersName(List<String> playersName) {
        this.playersName = playersName;
        return this;
    }
}
