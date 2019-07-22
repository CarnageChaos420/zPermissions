package zmcpermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionsManager {
	
	Main plugin;
	
	private HashMap<String,PermissionAttachment> perms = new HashMap<String,PermissionAttachment>();

	
	public PermissionsManager(Main pl){
		this.plugin = pl;
		reloadAll();
	}
	
	public void close(){
		for(Player player:Bukkit.getServer().getOnlinePlayers()){
			unloadPermissions(player);
		}
	}
	
	public void reloadAll(){
		for(Player player:Bukkit.getServer().getOnlinePlayers()){
			reloadPermissions(player);
		}
	}
	
	public void reloadPermissions(Player player){
		unloadPermissions(player);
		String uuid = player.getUniqueId().toString();
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		PermissionAttachment attachment = player.addAttachment(plugin);
		perms.put(uuid, attachment);
		
		ConfigurationSection user = permdata.getConfigurationSection("users."+uuid);
		if(user==null){//If player hasn't been recorded under users
			permdata.set("users."+uuid+".group", new ArrayList<String>());
			permdata.set("users."+uuid+".options.name", player.getName());
			try {
				permdata.save(file); } 
			catch (IOException e) {
				e.printStackTrace(); }
			return;
		}
		List<String> disabledperms = new ArrayList<String>();
		List<String> userperms = getUserPerms(uuid);
		for(String p:userperms){ //Load user perms to player
			if(p.startsWith("-")){
				disabledperms.add(p);
				continue;
			}
			attachment.setPermission(p, true);
		}
		
		//Get all the groups of the player including all group parents, and parents of the parents etc.
		List<String> groups = Main.gManager.getPlayerGroups(uuid);
		for(String group:Main.gManager.getPlayerGroups(uuid)){
			for(String parent:Main.gManager.getParentChain(group)){
				if(!groups.contains(parent)){
					groups.add(parent);
				}
			}
		}
		//=====
		for(String group:groups){
			List<String> gperms = Main.gManager.getGroupPerms(group);
			for(String p:gperms){ //Load group perms to player
				if(p.startsWith("-")){
					disabledperms.add(p);
					continue;
				}
				attachment.setPermission(p, true);
			}
		}
		for(String p:disabledperms){
			attachment.unsetPermission(p.replaceFirst("-", ""));
		}
		
	}
	public void unloadPermissions(Player player){
		String uuid = player.getUniqueId().toString();
		if(perms.containsKey(uuid)){
			player.removeAttachment(perms.get(uuid));
			perms.remove(uuid);
		}
	}
	
	public void addPermission(String uuid, String permission){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> userperms = permdata.getStringList("users."+uuid+".permissions");
		if(userperms.contains(permission)){
			return;
		}
		userperms.add(permission);
		permdata.set("users."+uuid+".permissions", userperms);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if(player != null){
			reloadPermissions(player);
		}
	}
	
	public void removePermission(String uuid, String permission){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> userperms = permdata.getStringList("users."+uuid+".permissions");
		if(!userperms.contains(permission)){
			return;
		}
		userperms.remove(permission);
		permdata.set("users."+uuid+".permissions", userperms);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if(player != null){
			reloadPermissions(player);
		}
	}
	
	public List<String> getUserPerms(String uuid){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		List<String> perms = permdata.getStringList("users."+uuid+".permissions");
		return perms;
	}

}
