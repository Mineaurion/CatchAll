package com.mineaurion.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mineaurion.Bukkit.command.CommandAddons;
import com.mineaurion.Bukkit.command.CommandDonateur;
import com.mineaurion.Bukkit.command.CommandEntiter;
import com.mineaurion.Bukkit.command.CommandLanceTomate;
import com.mineaurion.Bukkit.command.CommandLastLogin;
import com.mineaurion.Bukkit.command.CommandMaintenance;
import com.mineaurion.Bukkit.command.CommandReload;
import com.mineaurion.Bukkit.command.CommandWebhook;
import com.mineaurion.Bukkit.event.EventManager;
import com.mineaurion.Bukkit.event.MyListener;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;

import lombok.Getter;

public class Main extends JavaPlugin {
	public static FileConfiguration config;

	@Getter
	public static Main instance = null;

	public static String prefix = "";

	public TemmieWebhook temmie;
	public boolean usePlaceholderApi;

	@Override
	public void onEnable() {
		sendmessage("{{GOLD}}Les plugins Bukkit demarrent", "console");
		instance = this;

		initConfig();
		initPlugin();
		initCommand();
		getServer().getPluginManager().registerEvents(new EventManager(), this);
		getServer().getPluginManager().registerEvents(new MyListener(this), this);
	}

	public void initPlugin() {

		if (getConfig().getString("MinecraftToDiscord_link").equals("NONE")) {
			getLogger().info("Hey, is this your first time using MinecraftToDiscord?");
			getLogger().info("If yes, then you need to add your Webhook URL to the");
			getLogger().info("config.yml!");
			getLogger().info("");
			getLogger().info("...so go there and do that.");
			getLogger().info("After doing that, use /MineaurionReload");
			return;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
				&& Main.getInstance().getConfig().getBoolean("MinecraftToDiscord_UsePlaceholderAPI")) {
			usePlaceholderApi = true;
		}
		temmie = new TemmieWebhook(Main.getInstance().getConfig().getString("MinecraftToDiscord_link"));
	}

	public void initConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		config = Main.getInstance().getConfig();

		if (!config.contains("MinecraftToDiscord_link")) {
			config.set("MinecraftToDiscord_UsePlaceholderAPI", true);
			config.set("MinecraftToDiscord_link", "NONE");
		}

		if (!config.contains("Maintenance"))
			config.set("Maintenance", false);
		if (!config.contains("AccesDonateur"))
			config.set("AccesDonateur", false);
		if(!config.contains("reservedSlot")) 
			config.set("reservedSlot", 5);
		
		saveConfig();
	}
	

	public void initCommand() {
		instance.getCommand("MineaurionReload").setExecutor(new CommandReload());
		// LastLoginBukkit
		instance.getCommand("lastlogin").setExecutor(new CommandLastLogin());
		// lanceTomate
		instance.getCommand("lancetomate").setExecutor(new CommandLanceTomate());
		instance.getCommand("foundentiter").setExecutor(new CommandEntiter());
		// MinecraftToDiscord
		instance.getCommand("webhook").setExecutor(new CommandWebhook());
		//Maintenance
		instance.getCommand("maintenance").setExecutor(new CommandMaintenance());
		//Donateur
		instance.getCommand("donateur").setExecutor(new CommandDonateur());
		//Addons
		instance.getCommand("m_chat").setExecutor(new CommandAddons());
		instance.getCommand("spider").setExecutor(new CommandAddons());
		instance.getCommand("row").setExecutor(new CommandAddons());
		instance.getCommand("smoke").setExecutor(new CommandAddons());
		instance.getCommand("thunder").setExecutor(new CommandAddons());
		instance.getCommand("lavapop").setExecutor(new CommandAddons());
		instance.getCommand("spider").setExecutor(new CommandAddons());
	}

	// Utilities
	@SuppressWarnings("deprecation")
	public static void sendmessage(String message, String sender) {
		if (sender.equalsIgnoreCase("console") || sender.equalsIgnoreCase("Server")) {
			Bukkit.getConsoleSender().sendMessage(addColor(message));
		} else {
			Bukkit.getPlayer(sender).sendMessage(addColor(message));
		}
	}

	private static String addColor(String message) {
		message = prefix + message;
		StringBuilder textmain = new StringBuilder();
		Matcher m = Pattern.compile("(\\{\\{([^\\{\\}]+)\\}\\}|[^\\{\\}]+)").matcher(message);
		ChatColor color = null;
		ChatColor style = null;
		while (m.find()) {

			String entry = m.group();
			if (entry.contains("{{")) {
				color = null;
				style = null;
				switch (entry) {
				case "{{BLACK}}":
					color = ChatColor.BLACK;
					break;
				case "{{DARK_BLUE}}":
					color = ChatColor.DARK_BLUE;
					break;
				case "{{DARK_GREEN}}":
					color = ChatColor.DARK_GREEN;
					break;
				case "{{DARK_CYAN}}":
					color = ChatColor.DARK_AQUA;
					break;
				case "{{DARK_RED}}":
					color = ChatColor.DARK_RED;
					break;
				case "{{PURPLE}}":
					color = ChatColor.DARK_PURPLE;
					break;
				case "{{GOLD}}":
					color = ChatColor.GOLD;
					break;
				case "{{GRAY}}":
					color = ChatColor.GRAY;
					break;
				case "{{DARK_GRAY}}":
					color = ChatColor.DARK_GRAY;
					break;
				case "{{BLUE}}":
					color = ChatColor.AQUA;
					break;
				case "{{BRIGHT_GREEN}}":
					color = ChatColor.GREEN;
					break;
				case "{{CYAN}}":
					color = ChatColor.AQUA;
					break;
				case "{{RED}}":
					color = ChatColor.RED;
					break;
				case "{{LIGHT_PURPLE}}":
					color = ChatColor.LIGHT_PURPLE;
					break;
				case "{{YELLOW}}":
					color = ChatColor.YELLOW;
					break;
				case "{{WHITE}}":
					color = ChatColor.WHITE;
					break;
				case "{{OBFUSCATED}}":
					style = ChatColor.MAGIC;
					break;
				case "{{BOLD}}":
					style = ChatColor.BOLD;
					break;
				case "{{STRIKETHROUGH}}":
					style = ChatColor.STRIKETHROUGH;
					break;
				case "{{UNDERLINE}}":
					style = ChatColor.UNDERLINE;
					break;
				case "{{ITALIC}}":
					style = ChatColor.ITALIC;
					break;
				case "{{RESET}}":
					style = ChatColor.RESET;
					break;
				}
			} else {
				StringBuffer buff = new StringBuffer(entry);

				if (color != null) {
					buff.insert(0, color);

				}
				if (style != null) {
					buff.insert(0, style);
				}
				textmain.append(buff);
			}
		}
		return textmain.toString();
	}

}
