package com.mineaurion.catchall.forge.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mineaurion.catchall.forge.utils.Page;
import net.minecraftforge.fml.loading.FMLPaths;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class ConfigPage {

    public static Path getPath(){
        return FMLPaths.CONFIGDIR.get().resolve("MenuPage.json");
    }

    public static Map<String, Page> getPage() {
        Map<String, Page> page = new HashMap<>();
        try {
            Reader channelsJson = Files.newBufferedReader(FMLPaths.CONFIGDIR.get().resolve("menupage.json"));
            Gson gson = new Gson();

            page = gson.fromJson(channelsJson, new TypeToken<Map<String,Page>>() {}.getType());
            channelsJson.close();
        } catch (IOException exception) {
            System.out.println("Error file menupage.json not found");
        }
        return page;
    }

    public static void setPage(HashMap<String, Page> MapPage) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File ConfigFile = new File(ConfigPage.getPath().toString());
        try (FileWriter writer = new FileWriter(ConfigFile)) {
            writer.write(gson.toJson(MapPage));
        } catch (FileNotFoundException e) {
            throw e; // catch and re-throw
        } catch (IOException e) {
            System.err.print("Something went wrong" + e);
        }
    }

    public static void createFile(Path path) {
        if (!new File(path.toString()).exists()) {
            try {
                Files.createFile(Paths.get(path.toString()));
            } catch (IOException ex){
                System.err.println("Error create file: $ex");
            }
            HashMap<String, Page> example = new HashMap<>();
            example.put("example", Page.example());
            try {
                ConfigPage.setPage(example);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("[AurionChat] Successfully created MenuPage.json");
        }
    }
}
