package com.mineaurion.catchall.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.ArrayList;

public class Config {
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        builder.comment("States").push("states");
        builder.define("maintenance", false);
        builder.define("donateur", false);
        builder.pop();

        builder.comment("Webhook").push("webhook").defineList("discord", new ArrayList<>(), o -> true);
        builder.pop();

        SPEC = builder.build();
    }
}
