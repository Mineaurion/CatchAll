package com.mineaurion.catchall.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigData {
    public States states;

    public static class States {
        public ForgeConfigSpec.ConfigValue<Boolean> maintenance;
        public ForgeConfigSpec.ConfigValue<Boolean> donateur;
    }
}
