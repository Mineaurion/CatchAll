package com.mineaurion.Sponge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import com.google.inject.Inject;
import com.mineaurion.Sponge.command.CommandDonator;
import com.mineaurion.Sponge.command.CommandHandShow;
import com.mineaurion.Sponge.command.CommandMaintenance;
import com.mineaurion.Sponge.command.CommandPampersss;
import com.mineaurion.Sponge.command.commandStat;
import com.mineaurion.Sponge.command.commandlastlogin;
import com.mineaurion.Sponge.event.EventListener;

import lombok.Getter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "mineaurionsponge", name = "MineaurionSponge", version = "1.0", description = "An utility plugin", authors = {
		"THEJean_Kevin" })
public class Main {
	@Getter
	public static Main instance = null;

	@Inject
	@ConfigDir(sharedRoot = false)
	public Path defaultConfig;

	public static ConfigurationLoader<CommentedConfigurationNode> Loader;
	public CommentedConfigurationNode Node;

	@Listener
	public void onInitialization(GamePreInitializationEvent event) {
		instance = this;

		initConfig();
		initCommand();
		initSetup();
	}

	public void initSetup() {
		Sponge.getEventManager().registerListeners(this, new EventListener());

	}

	public void initConfig() {

		try {
			File f = new File(defaultConfig + "/Setting.conf");
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			Loader = HoconConfigurationLoader.builder().setPath(Paths.get(defaultConfig + "/Setting.conf")).build();
			Node = Loader.load();
			if (Node.getNode("Maintenance").isVirtual()) {
				setValut(false, "Maintenance");
			}
			if(Node.getNode("AccesDonateur").isVirtual()) {
				setValut(false,"AccesDonateur");
			}
			if(Node.getNode("reservedSlot").isVirtual()) {
				setValut(5,"reservedSlot");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void initCommand() {
		// statSponge
		CommandSpec cmdstat = CommandSpec.builder().permission("mineaurion.stat")
				.arguments(GenericArguments.string(Text.of("player")), GenericArguments.string(Text.of("stat")))
				.executor(new commandStat()).build();

		Sponge.getCommandManager().register(this, cmdstat, "mineaurionstat");

		// lastlogin
		CommandSpec cmdlastlogin = CommandSpec.builder().permission("mineaurion.lastlogin")
				.arguments(GenericArguments.optional(GenericArguments.integer(Text.of("mois"))))
				.executor(new commandlastlogin()).build();

		Sponge.getCommandManager().register(this, cmdlastlogin, "lastlogin");

		// hand show et mc_chat
		CommandSpec cmdhand = CommandSpec.builder().permission("mineaurion.showhand").executor(new CommandHandShow())
				.build();

		Sponge.getCommandManager().register(this, cmdhand, "hand");

		// Maintenance
		CommandSpec cmdMaintenance = CommandSpec.builder().permission("mineaurion.maintenance")
				.executor(new CommandMaintenance()).build();
		Sponge.getCommandManager().register(this, cmdMaintenance, "maintenance");
		
		// Donateur
				CommandSpec cmddonator = CommandSpec.builder().permission("mineaurion.donateurCommand")
						.executor(new CommandDonator()).build();
				Sponge.getCommandManager().register(this, cmddonator, "donateur");

		// Remove Item
		CommandSpec cmdpampersss = CommandSpec.builder().permission("mineaurion.pampersss.command")
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("type"))),GenericArguments.optional(GenericArguments.integer(Text.of("radius"))))
				.executor(new CommandPampersss()).build();
		Sponge.getCommandManager().register(this, cmdpampersss, "pampersss");

	}

	public void setValut(Object value, String node) {
		Node.getNode(node).setValue(value);
		try {
			Loader.save(Node);
			Node = Loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void sendmessage(String message, String sender) {
		if (sender.equals("console") || sender.equals("Server")) {
			Sponge.getGame().getServer().getConsole().sendMessage(addColor(message));
		} else {
			Sponge.getGame().getServer().getPlayer(sender).get().sendMessage(addColor(message));
		}
	}

	public static String prefix = "{{DARK_GREEN}}[{{WHITE}}MineaurionSponge{{DARK_GREEN}}]{{WHITE}} ";

	public static Text addColor(String message) {
		message = prefix + message;
		Text.Builder textMain = Text.builder();
		Matcher m = Pattern.compile("(\\{\\{([^\\{\\}]+)\\}\\}|[^\\{\\}]+)").matcher(message);
		TextColor color = null;
		TextStyle.Base style = null;
		while (m.find()) {

			String entry = m.group();
			if (entry.contains("{{")) {
				color = null;
				style = null;
				switch (entry) {
				case "{{BLACK}}":
					color = TextColors.BLACK;
					break;
				case "{{DARK_BLUE}}":
					color = TextColors.DARK_BLUE;
					break;
				case "{{DARK_GREEN}}":
					color = TextColors.DARK_GREEN;
					break;
				case "{{DARK_CYAN}}":
					color = TextColors.DARK_AQUA;
					break;
				case "{{DARK_RED}}":
					color = TextColors.DARK_RED;
					break;
				case "{{PURPLE}}":
					color = TextColors.DARK_PURPLE;
					break;
				case "{{GOLD}}":
					color = TextColors.GOLD;
					break;
				case "{{GRAY}}":
					color = TextColors.GRAY;
					break;
				case "{{DARK_GRAY}}":
					color = TextColors.DARK_GRAY;
					break;
				case "{{BLUE}}":
					color = TextColors.AQUA;
					break;
				case "{{BRIGHT_GREEN}}":
					color = TextColors.GREEN;
					break;
				case "{{CYAN}}":
					color = TextColors.AQUA;
					break;
				case "{{RED}}":
					color = TextColors.RED;
					break;
				case "{{LIGHT_PURPLE}}":
					color = TextColors.LIGHT_PURPLE;
					break;
				case "{{YELLOW}}":
					color = TextColors.YELLOW;
					break;
				case "{{WHITE}}":
					color = TextColors.WHITE;
					break;
				case "{{OBFUSCATED}}":
					style = TextStyles.OBFUSCATED;
					break;
				case "{{BOLD}}":
					style = TextStyles.BOLD;
					break;
				case "{{STRIKETHROUGH}}":
					style = TextStyles.STRIKETHROUGH;
					break;
				case "{{UNDERLINE}}":
					style = TextStyles.UNDERLINE;
					break;
				case "{{ITALIC}}":
					style = TextStyles.ITALIC;
					break;
				case "{{RESET}}":
					style = TextStyles.RESET;
					break;
				}
			} else {
				Text.Builder text = Text.builder(entry);

				if (color != null) {
					text.color(color);
				}
				if (style != null) {
					text.style(style);
				}
				textMain.append(text.build());
			}
		}
		return textMain.build();
	}

}
