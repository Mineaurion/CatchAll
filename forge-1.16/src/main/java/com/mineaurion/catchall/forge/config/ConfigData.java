package com.mineaurion.catchall.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ConfigData {
    public States states;

    public static class States {
        public ForgeConfigSpec.ConfigValue<Boolean> maintenance;
        public ForgeConfigSpec.ConfigValue<Boolean> donateur;

    }

    public Webhook webhook;

    public static class Webhook {

        public ForgeConfigSpec.ConfigValue<List<? extends String>> discord;

    }

}
