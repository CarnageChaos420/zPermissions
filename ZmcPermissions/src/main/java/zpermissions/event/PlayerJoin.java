package zpermissions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import zpermissions.Main;

public class PlayerJoin implements Listener{
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		
		if(!player.hasPlayedBefore()){
			String uuid = player.getUniqueId().toString();
			String defaultGroup = Main.gManager.getDefaultGroup();
			if(defaultGroup!=null){
				Main.gManager.setPlayerGroup(uuid, defaultGroup);
			}
		}
		
		Main.pManager.reloadPermissions(player);
	}
	

}
