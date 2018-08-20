package com.mineaurion.Bukkit.command;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandEntiter implements CommandExecutor {

	public Map<Integer,String> lagg = new HashMap<Integer,String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			lagg.clear();
			lagg.put(0, "Pas de tilentité");
			int entité = 0;
			if (sender.hasPermission("mineaurion.tilEntity")) {
				for(World world : Bukkit.getWorlds()) {
					for(Chunk chunk : world.getLoadedChunks()) {
						entité = 0;
						for(BlockState tileEntities : chunk.getTileEntities()) {
							entité++;
						}
						lagg.put(entité,"chunk :"+chunk.getX()+","+chunk.getZ()+" World :"+world.getName());
					}
				}
				Comparator<Integer> keyComparator = new Comparator<Integer>(){
					   @Override
					   public int compare(Integer int1, Integer int2){
					      return int2.compareTo(int1);
					   }
					};
					Map<Integer, String> sortedOnKeysMap = new TreeMap<Integer, String>(keyComparator);
					sortedOnKeysMap.putAll(lagg);
					
					int i =0;
					for(Entry<Integer, String> entry : sortedOnKeysMap.entrySet()){
						i++;
						
						if(i <10) {
							sender.sendMessage(entry.getValue() +" possède "+ entry.getKey());
						}else {
							break;
						}
					}
			}
			return true;
	}
}
