package com.mineaurion.catchall.forge;


import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.mineaurion.catchall.common.LuckPermsUtils;
import com.mineaurion.catchall.forge.commands.DonateurCommand;
import com.mineaurion.catchall.forge.commands.GlistCommand;
import com.mineaurion.catchall.forge.commands.HandCommand;
import com.mineaurion.catchall.forge.commands.MaintenanceCommand;
import com.mineaurion.catchall.forge.commands.MchatCommand;
import com.mineaurion.catchall.forge.commands.McheckCommand;
import com.mineaurion.catchall.forge.commands.MenupageCommand;
import com.mineaurion.catchall.forge.commands.WebhookCommand;
import com.mineaurion.catchall.forge.config.Config;
import com.mineaurion.catchall.forge.config.ConfigData;
import com.mineaurion.catchall.forge.config.ConfigPage;
import com.mineaurion.catchall.forge.listeners.LoginLogoutListener;
import com.mojang.brigadier.CommandDispatcher;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Mod("catchall")
public class CatchAll {
    public static ConfigData config;
    public static final Logger logger = LogManager.getLogger();

    public static LuckPerms luckPerms = null;

    public CatchAll() {
        logger.info("CatchAll Initializing");
        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        try {
            ModLoadingContext.class.getDeclaredMethod("registerExtensionPoint", Class.class, Supplier.class)
                    .invoke(
                            ModLoadingContext.get(),
                            IExtensionPoint.DisplayTest.class,
                            (Supplier<?>) () -> new IExtensionPoint.DisplayTest(
                                    () -> NetworkConstants.IGNORESERVERONLY,
                                    (a, b) -> true
                            )
                    );
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC, "catchall.toml");
        config = (new ObjectConverter().toObject(Config.SPEC.getValues(), ConfigData::new));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartedEvent event){
        ConfigPage.createFile(ConfigPage.getPath());
        luckPerms = LuckPermsProvider.get();
        MinecraftForge.EVENT_BUS.register(new LoginLogoutListener(event.getServer().getScoreboard()));
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        new MaintenanceCommand(dispatcher);
        new DonateurCommand(dispatcher);
        new GlistCommand(dispatcher);
        new WebhookCommand(dispatcher);
        new HandCommand(dispatcher);
        new MchatCommand(dispatcher);
        new McheckCommand(dispatcher);
        new MenupageCommand(dispatcher);
    }

    public static boolean hasPermission(ServerPlayer player, String permission){
        boolean has = false;
        User user = luckPerms.getUserManager().getUser(player.getUUID());
        if(user != null){
            has = user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
        }
        return has;
    }

    public static Optional<String> getMetaData(UUID uuid, String meta){
        return LuckPermsUtils.getMetaData(luckPerms, uuid, meta);
    }

}
