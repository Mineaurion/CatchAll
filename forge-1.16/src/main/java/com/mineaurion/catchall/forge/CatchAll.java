package com.mineaurion.catchall.forge;


import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.mineaurion.catchall.forge.commands.DonateurCommand;
import com.mineaurion.catchall.forge.commands.GlistCommand;
import com.mineaurion.catchall.forge.commands.MaintenanceCommand;
import com.mineaurion.catchall.forge.config.Config;
import com.mineaurion.catchall.forge.config.ConfigData;
import com.mineaurion.catchall.forge.listeners.LoginLogoutListener;
import com.mojang.brigadier.CommandDispatcher;
import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.fml.ExtensionPoint.DISPLAYTEST;

@Mod("catchall")
public class CatchAll {
    public static ConfigData config;
    public static final Logger logger = LogManager.getLogger();

    public CatchAll() {
        logger.info("CatchAll Initializing");
        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(DISPLAYTEST, () -> Pair.of(
                () -> FMLNetworkConstants.IGNORESERVERONLY, // ignore me, I'm a server only mod
                (s,b) -> true // I accept anything from the server or the save, if I'm asked
        ));
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC, "catchall.toml");
        config = (new ObjectConverter().toObject(Config.SPEC.getValues(), ConfigData::new));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event){
        MinecraftForge.EVENT_BUS.register(new LoginLogoutListener());
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event){
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        new MaintenanceCommand(dispatcher);
        new DonateurCommand(dispatcher);
        new GlistCommand(dispatcher);
    }

    public static boolean hasPermission(ServerPlayerEntity player, String permission){
        return FTBRanksAPI.getPermissionValue(player, permission).asBooleanOrFalse();
    }

}