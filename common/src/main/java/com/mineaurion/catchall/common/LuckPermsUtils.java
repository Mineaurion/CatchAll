package com.mineaurion.catchall.common;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

import java.util.Optional;
import java.util.UUID;

public class LuckPermsUtils {

    public static boolean hasPermission(LuckPerms luckPerms, UUID uuid, String permission){
        boolean has = false;
        User user = luckPerms.getUserManager().getUser(uuid);
        if(user != null){
            has = user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
        }
        return has;
    }

    public static Optional<String> getMetaData(LuckPerms luckPerms, UUID uuid, String meta){
        Optional<String> metaValue = Optional.empty();
        User user = luckPerms.getUserManager().getUser(uuid);
        if(user != null){
            Optional<QueryOptions> context = luckPerms.getContextManager().getQueryOptions(user);
            metaValue = context.map(queryOptions -> Optional.ofNullable(user.getCachedData().getMetaData(queryOptions).getMetaValue(meta))).orElseGet(() -> Optional.ofNullable(user.getCachedData().getMetaData().getMetaValue(meta)));
        }
        return metaValue;
    }
}
