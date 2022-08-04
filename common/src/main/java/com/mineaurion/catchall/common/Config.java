package com.mineaurion.catchall.common;

public class Config {

    public static class Tablist {
        public static String metaName = "namecolor";
        public static String metaDefaultName = "f";
    }

    public static class Maintenance {
        public static String permission = "mineaurion.bypass";
        public static String message = "Server is now in maintenance. Try again later please";
    }

    public static class Donateur {
        public static String permission = "mineaurion.donateur";
        public static String message = "Server is now in donator only mode. Try later please";
        public static String messageReservedSlot = "Last slots are reserved to donators (and more) players";
    }


}
