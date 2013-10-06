package com.github.devosray.RandomTP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.*;

public class RandomTP extends JavaPlugin implements CommandExecutor {

	private WorldEditPlugin worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("worldedit");
	
	@Override
	public void onEnable() {
		getLogger().info("RandomTP has been enabled");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("RandomTP has been disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		// TODO Auto-generated method stub
		return super.onCommand(sender, command, label, args);
	}

}
