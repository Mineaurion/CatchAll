

package com.mineaurion.Bukkit.event;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import com.mineaurion.Bukkit.Main;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.entity.Entity;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.event.Listener;

public class MyListener implements Listener
{
    protected Main plugin;
    protected HashMap<Player, String> flood;
    protected ArrayList<Entity> godmod;
    ArrayList<Player> not;
    
    public MyListener(final Main main) {
        this.flood = new HashMap<Player, String>();
        this.godmod = new ArrayList<Entity>();
        this.not = new ArrayList<Player>();
        this.plugin = main;
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
    public boolean onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        final Player p = event.getPlayer();
        final Entity t = event.getRightClicked();
        if (p.getItemInHand().getType().getId() == 290 && (p.isOp() || p.hasPermission("addons.goldshovel"))) {
            if (this.godmod.contains(t)) {
                this.godmod.remove(t);
                p.sendMessage(String.valueOf(t.getType().getName()) + " n'est plus invinsible !");
            }
            else {
                this.godmod.add(t);
                p.sendMessage(String.valueOf(t.getType().getName()) + " est invinsible !");
            }
        }
        else if (p.getItemInHand().getType().getId() == 269 && (p.isOp() || p.hasPermission("addons.goldshovel"))) {
            final Vector vec = p.getLocation().getDirection();
            t.setVelocity(new Vector(vec.getX() * 3.0, 1.0, vec.getZ() * 3.0));
        }
        else if (p.getItemInHand().getType().getId() == 273 && (p.isOp() || p.hasPermission("addons.goldshovel"))) {
            t.setVelocity(new Vector(0, 10, 0));
        }
        else if (p.getItemInHand().getType().equals((Object)Material.RED_ROSE) && (p.isOp() || p.hasPermission("addons.onMe"))) {
            if (p.getPassenger() == null) {
                if (t instanceof Player) {
                    p.sendMessage(ChatColor.BLUE + ((Player)t).getName() + ChatColor.GOLD + " est maintenant votre chapeau humain !");
                }
                else {
                    p.sendMessage(ChatColor.BLUE + t.getType().getName() + ChatColor.GOLD + " est maintenant votre chapeau humain !");
                }
                p.setPassenger(event.getRightClicked());
            }
            else if (p.getPassenger().equals(t)) {
                if (event.getRightClicked() instanceof Player) {
                    p.sendMessage(ChatColor.BLUE + ((Player)t).getName() + ChatColor.GOLD + " n'est plus votre chapeau humain !");
                }
                else {
                    p.sendMessage(ChatColor.BLUE + t.getType().getName() + ChatColor.GOLD + " n'est plus votre chapeau humain !");
                }
                p.eject();
            }
        }
        else if (p.getItemInHand().getType().equals((Object)Material.YELLOW_FLOWER) && (p.isOp() || p.hasPermission("addons.onHim"))) {
            if (t.getPassenger() == null) {
                if (t instanceof Player) {
                    p.sendMessage(ChatColor.GOLD + "Vous est maintenant le chapeau humain de " + ChatColor.BLUE + ((Player)t).getName() + ChatColor.GOLD + " !");
                }
                else {
                    p.sendMessage(ChatColor.GOLD + "Vous est maintenant le chapeau humain d'un " + ChatColor.BLUE + t.getType().getName() + ChatColor.GOLD + " !");
                }
                t.setPassenger((Entity)p);
            }
            else if (t.getPassenger().equals(p)) {
                if (t instanceof Player) {
                    p.sendMessage(ChatColor.GOLD + "Vous n'\ufffdtes plus le chapeau humain de " + ChatColor.BLUE + ((Player)t).getName() + ChatColor.GOLD + " !");
                }
                else {
                    p.sendMessage(ChatColor.GOLD + "Vous n'\ufffdtes plus le chapeau humain d'un " + ChatColor.BLUE + t.getType().getName() + ChatColor.GOLD + " !");
                }
                t.eject();
            }
        }
        return true;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(this.colorTexts("&5+" + event.getPlayer().getDisplayName()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(this.colorTexts("&9-" + event.getPlayer().getDisplayName()));
    }
    
    public String colorTexts(String message) {
        final String[] allowed = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "4" };
        String[] array;
        for (int length = (array = allowed).length, i = 0; i < length; ++i) {
            final String temp = array[i];
            if (message.contains("&" + temp)) {
                message = message.replaceAll("&" + temp, "�" + temp);
            }
        }
        return message;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDestroy(final VehicleDestroyEvent event) {
        if (event.getVehicle().getType().equals((Object)EntityType.BOAT) && !(event.getAttacker() instanceof Player)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMessage(final AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("addons.canflood") && !event.getPlayer().isOp()) {
            if (this.flood.containsKey(event.getPlayer()) && this.flood.get(event.getPlayer()).equals(event.getMessage())) {
                event.getPlayer().sendMessage(ChatColor.RED + "Pas de flood !");
                event.setCancelled(true);
                return;
            }
            this.flood.put(event.getPlayer(), event.getMessage());
        }
    }
    
    @EventHandler
    public void EntityDamageEvent(final EntityDamageEvent event) {
        if (this.godmod.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }
}
