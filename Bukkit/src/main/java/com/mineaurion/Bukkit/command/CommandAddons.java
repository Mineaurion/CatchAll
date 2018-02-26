package com.mineaurion.Bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.List;
import org.bukkit.util.Vector;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.World;
import java.util.Set;
import java.util.HashSet;
import org.bukkit.Sound;
import org.bukkit.Effect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class CommandAddons implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (commandLabel.equalsIgnoreCase("m_chat") && sender.hasPermission("addons.chat")) {
            String msg = "";
            for (int i = 1; i < args.length; ++i) {
                msg = String.valueOf(msg) + args[i] + " ";
            }
            
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && sender instanceof Player) {
                msg = PlaceholderAPI.setPlaceholders((Player)sender, msg);
                args[0] = PlaceholderAPI.setPlaceholders((Player)sender, args[0]);
            }
            
            if (args[0].equalsIgnoreCase("@@")) {
                for (final Player temp : Bukkit.getServer().getOnlinePlayers()) {
                    temp.sendMessage(colorTexts(msg));
                }
            }
            else {
                final Player t = Bukkit.getServer().getPlayer(args[0]);
                if (t != null && t.isValid() && t.isOnline()) {
                    t.sendMessage(colorTexts(msg));
                }
            }
        }
        if (sender instanceof Player) {
            final Player p = (Player)sender;
            if (commandLabel.equalsIgnoreCase("spider") && p.hasPermission("addons.spider")) {
                this.spider(p);
            }
            else if (commandLabel.equalsIgnoreCase("row") && p.hasPermission("addons.row")) {
                final World w = p.getWorld();
                final Location l = p.getLocation();
                final ItemStack is = new ItemStack(Material.APPLE);
                final ItemMeta im = is.getItemMeta();
                for (int j = l.getBlockX() - 2; j < l.getBlockX() + 2; ++j) {
                    for (int k = l.getBlockZ() - 2; k < l.getBlockZ() + 2; ++k) {
                        im.setDisplayName(String.valueOf(this.random()) + "Rowena");
                        is.setItemMeta(im);
                        w.dropItemNaturally(new Location(w, (double)j, (double)(l.getBlockY() + 30), (double)k), is);
                    }
                }
                p.sendMessage(ChatColor.RED + "Pluie de pomme !");
            }
            else if (commandLabel.equalsIgnoreCase("smoke") && sender.hasPermission("addons.effect.smoke")) {
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 0);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 2);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 3);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 4);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 5);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 6);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 7);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 8);
            }
            else if (commandLabel.equalsIgnoreCase("thunder") && sender.hasPermission("addons.effect.thunder")) {
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1.0f, 1.0f);
            }
            else if (commandLabel.equalsIgnoreCase("lavapop") && sender.hasPermission("addons.effect.lavapop")) {
                p.getWorld().playSound(p.getTargetBlock((Set)new HashSet(), 1000).getLocation(), Sound.BLOCK_LAVA_POP, 1.0f, 1.0f);
                for (int i = 0; i < 100; ++i) {
                    p.getWorld().playEffect(p.getTargetBlock((Set)new HashSet(), 1000).getLocation(), Effect.ANVIL_BREAK, 0);
                }
            }
        }
        return true;
    }
    
    protected void spider(final Player p) {
        final Vector vec = p.getLocation().getDirection();
        p.setVelocity(new Vector(vec.getX() * 3.0, vec.getY() * 5.0, vec.getZ() * 3.0));
    }
    
    protected String random() {
        final String[] color = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o", "r" };
        final int r = (int)(Math.random() * (color.length - 1 - 0)) + 0;
        return "\ufffd" + color[r];
    }
    
    public static String colorTexts(String message) {
        final String[] allowed = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o", "r" };
        String[] arrayOfString1;
        for (int j = (arrayOfString1 = allowed).length, i = 0; i < j; ++i) {
            final String toggle = arrayOfString1[i];
            if (message.contains("&" + toggle)) {
                message = message.replaceAll("&" + toggle, "�" + toggle);
            }
        }
        return message;
    }
    
    public static boolean scrollDown(final Player p) {
        final ItemStack is = p.getItemInHand();
        final ItemMeta im = is.getItemMeta();
        if (is.getTypeId() == 0 || !im.hasLore() || im.getLore().size() == 0) {
            return false;
        }
        final List<String> lore = (List<String>)im.getLore();
        int i = 0;
        while (i < lore.size()) {
            if (lore.get(i).contains("-")) {
                if (i + 1 >= lore.size()) {
                    return true;
                }
                lore.set(i, lore.get(i).replaceAll("-", ""));
                final ItemStack _r = getItemStack(lore.get(i + 1));
                lore.set(i + 1, "-" + lore.get(i + 1));
                im.setLore((List)lore);
                _r.setItemMeta(im);
                p.setItemInHand(_r);
                return true;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    public static boolean scrollUp(final Player p) {
        final ItemStack is;
        ItemStack _r = is = p.getItemInHand();
        final ItemMeta im = is.getItemMeta();
        if (is.getTypeId() == 0 || !im.hasLore() || im.getLore().size() == 0) {
            return false;
        }
        final List<String> lore = (List<String>)im.getLore();
        int k = 0;
        int i = 0;
        while (i < lore.size()) {
            if (lore.get(i).contains("-")) {
                k = i;
                if (i - 1 < 0) {
                    return true;
                }
                lore.set(i, lore.get(i).replaceAll("-", ""));
                _r = getItemStack(lore.get(i - 1));
                lore.set(i - 1, "-" + lore.get(i - 1));
                im.setLore((List)lore);
                _r.setItemMeta(im);
                p.setItemInHand(_r);
                return true;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    protected static ItemStack getItemStack(final String s) {
        int id = 0;
        int data = 0;
        if (s.contains(":")) {
            id = Integer.valueOf(s.split(":")[0]);
            data = Integer.valueOf(s.split(":")[1]);
        }
        else {
            id = Integer.valueOf(s);
        }
        return new ItemStack(id, 1, (short)0, (byte)data);
    }
}
