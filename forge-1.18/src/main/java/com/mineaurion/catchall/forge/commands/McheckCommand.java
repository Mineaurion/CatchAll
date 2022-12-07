package com.mineaurion.catchall.forge.commands;

import com.mineaurion.catchall.forge.utils.TextParser;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;


public class McheckCommand {

    private static final DecimalFormat TIME_FORMATTER = new DecimalFormat("########0.000");
    public McheckCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("mcheck")
            .requires((p_138116_) -> p_138116_.hasPermission(3))
            .executes((cmdcntxt) -> {
                Player player = cmdcntxt.getSource().getPlayerOrException();
                MinecraftServer server = player.getServer();
                Collection<ServerLevel> serverlevel = (Collection<ServerLevel>) server.getAllLevels();
                DateFormat simple = new SimpleDateFormat("dd MMM HH:mm:ss");
                int allentitiescounter = 0;
                int allchunkscounter = 0;
                double meanTickTime = mean(server.tickTimes) * 1.0E-6D;
                double meanTPS = Math.min(1000.0/meanTickTime, 20);

                String finalstring = "&6[<&aMCheck|&dProvided by Gohu|cmd|Pouet>&6] <&6&o&lServer Started|&e" + simple.format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime())) + "| | >&e, <&6&o&lServer Memory|&eMax: &6" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "\\n&eTotal: &6" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "\\n&eFree: &6" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "\\n&eUse: &6" + ((Runtime.getRuntime().totalMemory() / 1024 / 1024) - (Runtime.getRuntime().freeMemory() / 1024 / 1024)) + "| | >" + "\n";
                finalstring = finalstring + "&aWorldName  &0|&d  Ms  &0|&e   TPS   &0|&b Entities &0|&c Chunks" + "\n";

                for(ServerLevel level : serverlevel){
                    long[] times = server.getTickTime(level.dimension());
                    double worldTickTime = mean(times) * 1.0E-6D;
                    double worldTPS = Math.min(1000.0 / worldTickTime, 20);
                    Iterable<Entity> allentities = level.getAllEntities();
                    int counter = 0;
                    for (Object i : allentities) {
                        counter++;
                    }
                    allentitiescounter = allentitiescounter + counter;
                    allchunkscounter = allchunkscounter + level.getChunkSource().getLoadedChunksCount();
                    String[] levelname = level.dimension().location().toString().split(":");
                    finalstring = finalstring + "<&a" + levelname[1] + "|" + level.dimension().location().toString() + "| | > &0|&d " + TIME_FORMATTER.format(worldTickTime) + " &0|&e " + TIME_FORMATTER.format(worldTPS) + " &0|&b " + counter + " &0|&c " + level.getChunkSource().getLoadedChunksCount() + "\n";
                }

                finalstring = finalstring + "&aOverall" + " &0|&d " + TIME_FORMATTER.format(meanTickTime) + " &0|&e " + TIME_FORMATTER.format(meanTPS) + " &0|&b " + allentitiescounter + " &0|&c " + allchunkscounter;
                player.sendMessage(TextParser.buildtext(finalstring), Util.NIL_UUID);
            return 1;
        }));
    }

    private static long mean(long[] values)
    {
        long sum = 0L;
        for (long v : values)
            sum += v;
        return sum / values.length;
    }
}
