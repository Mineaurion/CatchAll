package com.mineaurion.catchall.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        builder.comment("States").push("states");
        builder.define("maintenance", false);
        builder.define("donateur", false);
        builder.pop();

        SPEC = builder.build();
    }
}
