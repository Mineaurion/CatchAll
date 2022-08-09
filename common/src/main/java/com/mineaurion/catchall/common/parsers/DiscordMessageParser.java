package com.mineaurion.catchall.common.parsers;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.Arrays;

public class DiscordMessageParser {
    private final String[] message;

    public DiscordMessageParser(String[] message) {
        this.message = message;
    }

    public DiscordMessageParser(String message) {
        this.message = message.split("\\s+");
    }

    public boolean isValid() {
        return (this.message.length >= 1)  && !isEmbed() || (isEmbed() && this.message.length > 2);
    }

    public boolean isEmbed() {
        if (this.message.length == 0) {
            return false;
        }

        return this.message[0].equalsIgnoreCase("embed");
    }

    public String getMessage() {
        if (isEmbed()) {
            String[] message = Arrays.copyOfRange(this.message, 1, this.message.length);
            return String.join(" ", message);
        }

        return "{\"content\": \"" + String.join(" ", this.message) + "\"}";
    }

    public String send(String channel) throws Exception {
        if(!isValid()){
            throw new Exception("Invalid discord webhook format");
        }

        return HttpRequest.post(channel)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
                .contentType("application/json")
                .send(getMessage())
                .body();

    }

}
