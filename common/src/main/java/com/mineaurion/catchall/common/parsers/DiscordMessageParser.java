package com.mineaurion.catchall.common.parsers;

import java.util.Arrays;

public class DiscordMessageParser {
    private String[] message;

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

}
