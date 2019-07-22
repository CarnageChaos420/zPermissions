package zmcpermissions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import zmcpermissions.command.Permissions;
import zmcpermissions.event.PlayerJoin;
import zmcpermissions.event.PlayerQuit;



public class Main extends JavaPlugin{
	
	public static String directory ;
	//public static HashMap<UUID,PermissionAttachment> perms = new HashMap<UUID,PermissionAttachment>();
	public static PermissionsManager pManager;
	public static GroupManager gManager;
	
	public static void main(String[] args) {

	}
	
	public void onEnable() {
		directory = getDataFolder().getAbsolutePath();
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();
		
		registerCommands();
		registerEvents();
		registerConfig();
		
		//File userData = new File(directory + "//userdata");
		//userData.mkdirs();
		
		logger.info(pdfFile.getName() + " has been enabled (V." + pdfFile.getVersion() + ")");
		
		
		File file = new File(directory + "//permissions.yml");
		if(!file.exists()){
			try { 
				file.createNewFile(); 
				YamlConfiguration permdata = YamlConfiguration.loadConfiguration(file);
				permdata.createSection("groups");
				permdata.save(file);
			} 
			catch (IOException e1) { 
				e1.printStackTrace();  }
		}
		
		gManager = new GroupManager();
		pManager = new PermissionsManager(this);
	}
	
	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();
		
		pManager.close();

		logger.info(pdfFile.getName() + " has been disabled (V." + pdfFile.getVersion() + ")");
	}
	
	public void registerCommands() {
		getCommand("permissions").setExecutor(new Permissions(this));
	}
	
	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
	}
	
	private void registerConfig() {
		saveDefaultConfig();
	}
	

}
