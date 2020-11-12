package com.mineaurion.catchall.parsers;

import java.util.Arrays;

public class DiscordMessageParser {
    private String[] message;

    public DiscordMessageParser(String[] message) {
        this.message = message;
    }

    public boolean isValid() {
        return (this.message.length >= 1)  && !isEmbed() || (isEmbed() && this.message.length > 2);
    }

    public boolean isEmbed() {
        if (this.message.length == 0) {
            return false;
        }

        return this.message[0].toLowerCase().equals("embed");
    }

    public String getMessage() {
        if (isEmbed()) {
            String[] message = Arrays.copyOfRange(this.message, 1, this.message.length);
            return String.join(" ", message);
        }

        return String.join(" ", this.message);
    }

}
