package com.mineaurion.catchall.forge.utils;




import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Page {
    private final String title;
    private final String header;
    private final List<String> content;
    private String footer = "";

    public Page(String title, String header, List<String> content, String footer) {
        this.title = title;
        this.header = header;
        this.content = content;
        this.footer = footer;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public List<String> getContent() {
        return content;
    }

    public String getFooter() {
        return footer;
    }

    public static Page example(){
        String title = "---Example Title---";
        String header = "Example Header";
        List<String> content = Stream.of("line1","line2","line3")
                                     .collect(Collectors.toList());
        String footer = "Example Footer";

        return new Page(title, header, content, footer);
    }
}
