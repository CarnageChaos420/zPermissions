package zmcpermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class GroupManager {
	
	public boolean exists(String group){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		Set<String> keys = permdata.getConfigurationSection("groups").getKeys(false);
		if(keys.contains(group)){
			return true;
		}
		return false;
	}
	
	public List<String> getGroups(){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		List<String> groups = new ArrayList<String>();
		for(String key:permdata.getConfigurationSection("groups").getKeys(false)){
			groups.add(key);
		}
		return groups;
	}
	
	public List<String> getGroupPerms(String group){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		List<String> perms = permdata.getStringList("groups."+group+".permissions");
		return perms;
	}
	
	public boolean createGroup(String name){
		if(exists(name)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		permdata.createSection("groups."+name);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		return true;
	}
	
	public boolean deleteGroup(String group){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);

		for(String groupname:getGroups()){
			List<String> parents = getParents(groupname);
			if(parents.contains(group)){
				parents.remove(group);
				permdata.set("groups."+groupname+".options.inheritence", parents);
			}
		}
		for(String user: permdata.getConfigurationSection("users").getKeys(false)){
			List<String> groups = getPlayerGroups(user);
			if(groups.contains(group)){
				groups.remove(group);
				permdata.set("users."+user+".group", groups);
			}
		}
		permdata.set("groups."+group, null);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		Main.pManager.reloadAll();
		return true;
	}
	
	public boolean setDefaultGroup(String group){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		String prevDefault = getDefaultGroup();
		
		if(prevDefault!=null) permdata.set("groups."+prevDefault+".options.default", null);
		permdata.set("groups."+group+".options.default", true);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		return true;
	}
	
	public String getDefaultGroup(){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		for(String group:getGroups()){
			boolean isDefault = permdata.getBoolean("groups."+group+".options.default");
			if(isDefault){
				return group;
			}
		}
		return null;
	}
	
	public boolean addPermission(String group, String permission){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> groupperms = permdata.getStringList("groups."+group+".permissions");
		if(groupperms.contains(permission)){
			return false;
		}
		groupperms.add(permission);
		permdata.set("groups."+group+".permissions", groupperms);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Main.pManager.reloadAll();
		return true;
	}
	
	public boolean removePermission(String group, String permission){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> groupperms = permdata.getStringList("groups."+group+".permissions");
		if(!groupperms.contains(permission)){
			return false;
		}
		groupperms.remove(permission);
		permdata.set("groups."+group+".permissions", groupperms);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Main.pManager.reloadAll();
		return true;
	}
	
	public List<String> getPlayerGroups(String uuid){
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		List<String> groups = permdata.getStringList("users."+uuid+".group");
		return groups;
	}
	
	public boolean addPlayerGroup(String uuid, String group){
		if(!exists(group)){
			return false;
		}
		
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> groups = permdata.getStringList("users."+uuid+".group");
		if(groups.contains(group)){
			return false;
		}
		groups.add(group);
		permdata.set("users."+uuid+".group", groups);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if(player != null){
			Main.pManager.reloadPermissions(player);
		}
		return true;
	}
	
	public boolean removePlayerGroup(String uuid, String group){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> groups = permdata.getStringList("users."+uuid+".group");
		if(!groups.contains(group)){
			return false;
		}
		
		groups.remove(group);
		permdata.set("users."+uuid+".group", groups);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if(player != null){
			Main.pManager.reloadPermissions(player);
		}
		return true;
	}
	
	public boolean setPlayerGroup(String uuid, String group){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> groups = new ArrayList<String>();
		groups.add(group);
		permdata.set("users."+uuid+".group", groups);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if(player != null){
			Main.pManager.reloadPermissions(player);
		}
		return true;
	}
	
	public boolean setParents(String group, String parents){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		String[] array = parents.split(",");
		List<String> parentlist = Arrays.asList(array);
		
		permdata.set("groups."+group+".options.inheritence", parentlist);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Main.pManager.reloadAll();
		return true;
	}
	public boolean addParent(String group, String parent){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> parents= permdata.getStringList("groups."+group+".options.inheritence");
		if(parents.contains(parent)){
			return false;
		}
		parents.add(parent);
		
		permdata.set("groups."+group+".options.inheritence", parents);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Main.pManager.reloadAll();
		return true;
	}
	public boolean removeParent(String group, String parent){
		if(!exists(group)){
			return false;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> parents= permdata.getStringList("groups."+group+".options.inheritence");
		if(!parents.contains(parent)){
			return false;
		}
		parents.remove(parent);
		
		permdata.set("groups."+group+".options.inheritence", parents);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
		
		Main.pManager.reloadAll();
		return true;
	}
	public List<String> getParents(String group){
		if(!exists(group)){
			return null;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		List<String> parentlist = permdata.getStringList("groups."+group+".options.inheritence");
		//if(parentlist.isEmpty()){
		//	return new ArrayList<String>();
			//return null;
		//}
		return parentlist;
	}
	public List<String> getParentChain(String group){
		if(!exists(group)){
			return null;
		}
		//if(getParents(group)==null){
		//	return new ArrayList<String>();
			//return null;
		//}
		List<String> parentchain = getParents(group);
		boolean finished = false;
		for(;!finished;){
			finished = true;
			List<String> parentlist = new ArrayList<String>();
			parentlist.addAll(parentchain);
			for(String parent:parentlist){
				List<String> parents2 = getParents(parent);
				if(parents2!=null){
					for(String parent2:parents2){
						if(!parentchain.contains(parent2)){
							parentchain.add(parent2);
							finished = false;
						}
					}
				}
			}
		}
		return parentchain;
	}
	
	public void setPrefix(String group, String prefix){
		if(!exists(group)){
			return;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		permdata.set("groups."+group+".options.prefix", prefix);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
	}
	public String getPrefix(String group){
		if(!Main.gManager.exists(group)){
			return null;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);

		String prefix = permdata.getString("groups."+group+".options.prefix");
		return prefix;
	}
	public void setSuffix(String group, String suffix){
		if(!exists(group)){
			return;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
		
		permdata.set("groups."+group+".options.suffix", suffix);
		try {
			permdata.save(file); } 
		catch (IOException e) {
			e.printStackTrace(); }
	}
	public String getSuffix(String group){
		if(!Main.gManager.exists(group)){
			return null;
		}
		File file = new File(Main.directory + "//permissions.yml");
		YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);

		String suffix = permdata.getString("groups."+group+".options.suffix");
		return suffix;
	}

}
