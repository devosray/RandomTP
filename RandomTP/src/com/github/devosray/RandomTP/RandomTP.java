package com.github.devosray.RandomTP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class RandomTP extends JavaPlugin{

	private WorldEditPlugin worldEdit; 
	
	enum messageType { GOOD, BAD, INFO }
	String prefix = ChatColor.GOLD + "[RandomTP]";
	
	final String path = "plugins"+File.separator+"RandomTP"+File.separator;
	final String filename = "portals.yml";
	File dir;
	
	private FileConfiguration portalConfig;
	
	@Override
	public void onEnable() {
		
		if (getServer().getPluginManager().getPlugin("WorldEdit") == null){
			Bukkit.broadcastMessage(prefix + ": " + ChatColor.RED + "World edit needs to be installed!");
		}
		
		getLogger().info("RandomTP has been enabled");
		worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		portalConfig = loadPortalConfg();

	}
	
	@Override
	public void onDisable() {
		getLogger().info("RandomTP has been disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("rtp")){

			//If sender is a player
			if (sender instanceof Player) {

				Player p = (Player) sender;

				//Checks if it is the create command	
				if (args[0].equalsIgnoreCase("create")){
					if (args.length == 2){

						createPortal(p , args[1]);
						return true;
					} else {
						SendMessage(messageType.BAD, prefix, p, "Please provide a name for your portal!");
					}
				}
			}
		}

		return false;
		
	}
	
	private void createPortal(Player p, String name){
		/*
		 * Create a portal and convert 
		 * air spaces to portal
		 */		
		
		Selection selection = worldEdit.getSelection(p);
		//Make sure the selection is not empty
		
		if (selection != null){	
			double x1, x2, y1, y2, z1, z2;			
			Location min = selection.getMinimumPoint();
			Location max = selection.getMaximumPoint();
			
			HashMap<String, Double> portal = new HashMap<String, Double>();
			
			portal.put("x1", min.getX());
			portal.put("y1", min.getY());
			portal.put("z1", min.getZ());
			
			portal.put("x2", max.getX());
			portal.put("y2", max.getY());
			portal.put("z2", max.getZ());
			
			//Save to config
			savePortal(portal, name, p);
			
			
		} else {
			SendMessage(messageType.BAD, prefix, p, "You have not selected anything");
		}
		
	}
	
	
	private void SendMessage(messageType type, String prefix, Player p, String message){
		
		/*
		 * Simple player-chat message controller
		 */
		
		ChatColor color = ChatColor.GRAY;
		
		if (type == messageType.BAD) color = ChatColor.RED;
		if (type == messageType.GOOD) color = ChatColor.GREEN;
		if (type == messageType.INFO) color = ChatColor.BLUE;
		
		p.sendMessage(prefix + ": " +color + message);
		
	}
	
	private FileConfiguration loadPortalConfg(){
		
		/*
		 * Load the portal config file.
		 * If it does not exist, create it
		 */
		
		dir = new File(path);
		dir.mkdirs();
		
		File file = new File(path + filename);
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if (!(config.contains("portals"))){
			
			HashMap empty = new HashMap<>();
			
			config.createSection("portals", empty);
			try {
				config.save(path+filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		YamlConfiguration config2 = YamlConfiguration.loadConfiguration(new File(path + filename));
		
		return config2;
	}
	
	private void savePortal(HashMap data, String name, Player p){
		
		if (portalConfig.contains("portals."+name)){
			//The name is already used
			SendMessage(messageType.BAD, prefix, p, "The name is already in use");
			return;
			
		}
		
		portalConfig.createSection("portals." + name, data);
		try {
			portalConfig.save(new File(path+filename));
			//Success!
			SendMessage(messageType.GOOD, prefix, p, "Portal " + ChatColor.BLUE + name + ChatColor.GREEN + " has been created!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

