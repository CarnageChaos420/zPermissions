package zmcpermissions.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import zmcpermissions.Main;

public class Permissions implements CommandExecutor{
	
	Main plugin;
	
	public Permissions(Main pl){
		this.plugin = pl;
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(label.equalsIgnoreCase("permissions") || label.equalsIgnoreCase("perm")){
				if(!sender.hasPermission("zpermissions")){
					sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
					return false; 
				}
			if(args.length==0){
				sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
				sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.YELLOW+"User Commands:");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> add <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> remove <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group set <group>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group add <group>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group remove <group>");
				sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.YELLOW+"Group Commands:");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm default group");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm set default group <group>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group [group]");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> create");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> delete");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> add <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> remove <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents set <parent(s)>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents add <parent>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents remove <parent>");
				
				return true;
			}
			
			if(args[0].equalsIgnoreCase("set")){
				if(!sender.hasPermission("zpermissions.group.default.set")){
					sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
					return false; 
				}
				if(args.length>1 && args[1].equalsIgnoreCase("default")){
					if(args.length>2 && args[2].equalsIgnoreCase("group")){
						if(args.length==4){
							boolean success = Main.gManager.setDefaultGroup(args[3]);
							if(!success){
								sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[3]);
								return false;
							}
							sender.sendMessage(ChatColor.GOLD+"Set default group to "+ChatColor.YELLOW+args[3]);
							return true;
						}
					}
				}
				sender.sendMessage("Correct Usage: /perm set default group <group>");
				return false;
			}
			if(args[0].equalsIgnoreCase("default")){
				if(!sender.hasPermission("zpermissions.group.default")){
					sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
					return false; 
				}
				if(args.length>1 && args[1].equalsIgnoreCase("group")){
					if(args.length==2){
						String group = Main.gManager.getDefaultGroup();
						if(group==null){
							sender.sendMessage(ChatColor.RED+"A default group has not been set yet!");
							return false;
						}
						sender.sendMessage(ChatColor.GOLD+"Default group is "+ChatColor.YELLOW+group);
						return true;
					}
				}
				sender.sendMessage("Correct Usage: /perm default group");
				return false;
			}
			
			if(args[0].equalsIgnoreCase("user")){
				if(!sender.hasPermission("zpermissions.user")){
					sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
					return false;
				}
				if(args.length==1){
					sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
					sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player>");
					sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> add <permission>");
					sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> remove <permission>");
					return false;
				}
				if(args.length==2){
					//if(!sender.hasPermission("zpermissions.user")){
					//	sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
					//	return false;
					//}
					@SuppressWarnings("deprecation")
					OfflinePlayer user = Bukkit.getServer().getOfflinePlayer(args[1]);
					String uuid = user.getUniqueId().toString();
					
					sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
					sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"User: "+ChatColor.AQUA+user.getName());
					sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"Groups:");
					List<String> usergroups = Main.gManager.getPlayerGroups(uuid);
					for(String group:usergroups){
						sender.sendMessage(ChatColor.BLUE+"| "+ ChatColor.WHITE+"- "+group);
					}
					
					sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"Permissions:");
					List<String> userperms = Main.pManager.getUserPerms(uuid);
					for(String perm:userperms){
						sender.sendMessage(ChatColor.BLUE+"| "+ ChatColor.WHITE+"- "+perm);
					}
					return true;
				}
				
				if(args[2].equalsIgnoreCase("add")){
					if(!sender.hasPermission("zpermissions.user.permissions.add")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(args.length==3){
						sender.sendMessage("Correct Usage: /perm user <player> add <permission>");
						return false;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer user = Bukkit.getServer().getOfflinePlayer(args[1]);
					String uuid = user.getUniqueId().toString();
					
					Main.pManager.addPermission(uuid, args[3]);
					sender.sendMessage(ChatColor.GOLD+"Added permission "+ChatColor.WHITE+args[3]+ChatColor.GOLD+" to "+ChatColor.RED+args[1]);
					return true;
				}
				
				if(args[2].equalsIgnoreCase("remove")){
					if(!sender.hasPermission("zpermissions.user.permissions.remove")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(args.length==3){
						sender.sendMessage("Correct Usage: /perm user <player> remove <permission>");
						return false;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer user = Bukkit.getServer().getOfflinePlayer(args[1]);
					String uuid = user.getUniqueId().toString();

					Main.pManager.removePermission(uuid, args[3]);
					sender.sendMessage(ChatColor.GOLD+"Removed permission "+ChatColor.WHITE+args[3]+ChatColor.GOLD+" from "+ChatColor.RED+args[1]);
					return true;
				}
				
				if(args[2].equalsIgnoreCase("group")){
					@SuppressWarnings("deprecation")
					OfflinePlayer user = Bukkit.getServer().getOfflinePlayer(args[1]);
					String uuid = user.getUniqueId().toString();
					
					if(args.length==3){
						sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
						sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group set <group>");
						sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group add <group>");
						sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group remove <group>");
						return true;
					}
					
					if(args[3].equalsIgnoreCase("set")){
						if(!sender.hasPermission("zpermissions.user.group.set")){
							sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
							return false; 
						}
						if(args.length!=5){
							sender.sendMessage("Correct usage: /perm user <user> group set <group>");
							return false;
							}
						if(!Main.gManager.exists(args[4])){
							sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[4]);
							return false;
							}
						Main.gManager.setPlayerGroup(uuid, args[4]);
						sender.sendMessage(ChatColor.GOLD+"Set group of "+ChatColor.RED+args[1]+ChatColor.GOLD+" to "+ChatColor.YELLOW+args[4]);
						return true;
						
					}
					if(args[3].equalsIgnoreCase("add")){
						if(!sender.hasPermission("zpermissions.user.group.add")){
							sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
							return false; 
						}
						if(args.length!=5){
							sender.sendMessage("Correct usage: /perm user <user> group add <group>");
							return false;
							}
						if(!Main.gManager.exists(args[4])){
							sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[4]);
							return false;
							}
						if(!Main.gManager.addPlayerGroup(uuid, args[4])){
							sender.sendMessage(ChatColor.RED+args[1]+ChatColor.DARK_RED+" is already a member of group "+ChatColor.YELLOW+args[4]);
							return false; 
							}
						sender.sendMessage(ChatColor.GOLD+"Added group "+ChatColor.YELLOW+args[4]+ChatColor.GOLD+" to "+ChatColor.RED+args[1]);
						return true;
						
					}
					if(args[3].equalsIgnoreCase("remove")){
						if(!sender.hasPermission("zpermissions.user.group.remove")){
							sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
							return false; 
						}
						if(args.length!=5){
							sender.sendMessage("Correct usage: /perm user <user> group remove <group>");
							return false;
							}
						if(!Main.gManager.exists(args[4])){
							sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[4]);
							return false; 
							}
						if(!Main.gManager.removePlayerGroup(uuid, args[4])){
							sender.sendMessage(ChatColor.RED+args[1]+ChatColor.DARK_RED+" is not a member of group "+ChatColor.YELLOW+args[4]);
							return false; 
							}
						sender.sendMessage(ChatColor.GOLD+"Removed group "+ChatColor.YELLOW+args[4]+ChatColor.GOLD+" to "+ChatColor.RED+args[1]);
						return true;
						
					}
					sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
					sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group set <group>");
					sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group add <group>");
					sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group remove <group>");
					return false;
				}
				sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
				sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.YELLOW+"User Commands:");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> add <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> remove <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group set <group>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group add <group>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group remove <group>");
				return false;
			}
			if(args[0].equalsIgnoreCase("group")){
				if(!sender.hasPermission("zpermissions.group")){
					sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
					return false; 
				}
				if(args.length==1){
					sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
					sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"List of Groups: ");
					List<String> groups = Main.gManager.getGroups();
					for(String group:groups){
						sender.sendMessage(ChatColor.BLUE+"|"+ChatColor.WHITE+" - "+group);
					}
					
					return false;
				}
				if(args.length==2){
					if(!Main.gManager.exists(args[1])){
						sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[1]);
						return false;
					}
					sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
					sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"Group: "+ChatColor.AQUA+args[1]);
					String prefix = Main.gManager.getPrefix(args[1]);
					if(prefix!=null) 
						sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"Prefix: "+ChatColor.WHITE+colorize(prefix)+ChatColor.RESET+"  <-  "+prefix);
					String suffix = Main.gManager.getSuffix(args[1]);
					if(suffix!=null) 
						sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"Suffix: "+colorize(suffix)+ChatColor.RESET+"  <-  "+suffix);
					List<String> parents = Main.gManager.getParents(args[1]);
					if(parents!=null){
						sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.GOLD+"Parents:");
						for(String parent:parents){
							sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.WHITE+"- "+parent);
						}
					}
					sender.sendMessage(ChatColor.BLUE+"|"+ChatColor.GOLD+" Permissions:");
					List<String> perms = Main.gManager.getGroupPerms(args[1]);
					for(String perm:perms){
						sender.sendMessage(ChatColor.BLUE+"|"+ChatColor.WHITE+" - "+perm);
					}
					String defaultGroup = Main.gManager.getDefaultGroup();
					if(defaultGroup!=null && defaultGroup.equals(args[1])){
						sender.sendMessage(ChatColor.BLUE+"|"+ChatColor.GOLD+" Default Group: "+ChatColor.WHITE+"true");
					}
					
					return false;
				}
				
				if(args[2].equalsIgnoreCase("create")){
					if(!sender.hasPermission("zpermissions.group.create")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(Main.gManager.exists(args[1])){
						sender.sendMessage(ChatColor.RED+"Group with name "+ChatColor.YELLOW+args[1]+ChatColor.RED+" already exists!");
						return false;
					}
					sender.sendMessage(ChatColor.GOLD+"Created group "+ChatColor.YELLOW+args[1]);
					Main.gManager.createGroup(args[1]);
					return true;
				}
				if(args[2].equalsIgnoreCase("delete")){
					if(!sender.hasPermission("zpermissions.group.delete")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(!Main.gManager.exists(args[1])){
						sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[1]);
						return false;
					}
					sender.sendMessage(ChatColor.GOLD+"Group "+ChatColor.YELLOW+args[1]+ChatColor.GOLD+" has been removed");
					Main.gManager.deleteGroup(args[1]);
					return true;
				}
				if(args[2].equalsIgnoreCase("add")){
					if(!sender.hasPermission("zpermissions.group.permissions.add")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(args.length==3){
						sender.sendMessage("Correct Usage: /perm group <group> add <permission>");
						return false;
					}
					if(!Main.gManager.exists(args[1])){
						sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[1]);
						return false;
					}
					sender.sendMessage(ChatColor.GOLD+"Added permission "+ChatColor.WHITE+args[3]+ChatColor.GOLD+" to group "+ChatColor.YELLOW+args[1]);
					Main.gManager.addPermission(args[1], args[3]);
					return true;
				}
				if(args[2].equalsIgnoreCase("remove")){
					if(!sender.hasPermission("zpermissions.group.permissions.remove")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(args.length==3){
						sender.sendMessage("Correct Usage: /perm group <group> remove <permission>");
						return false;
					}
					if(!Main.gManager.exists(args[1])){
						sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[1]);
						return false;
					}
					sender.sendMessage(ChatColor.GOLD+"Removed permission "+ChatColor.WHITE+args[3]+ChatColor.GOLD+" from group "+ChatColor.YELLOW+args[1]);
					Main.gManager.removePermission(args[1], args[3]);
					return true;
				}
				if(args[2].equalsIgnoreCase("parents")){
						if(args.length==3){
							sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
							sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents set <parent(s)>");
							sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents add <parent>");
							sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents remove <parent>");
							return true;
						}
						if(!Main.gManager.exists(args[1])){
							sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[1]);
							return false;
							}
						if(args[3].equalsIgnoreCase("set")){
							if(!sender.hasPermission("zpermissions.group.parents.set")){
								sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
								return false; 
							}
							if(args.length!=5){
								sender.sendMessage("Correct Usage: /perm group <group> parents set <parent(s)>");
								sender.sendMessage(ChatColor.DARK_AQUA+"(For multiple parents, seperate each parent with a comma)");
								return false;
							}
							String[] array = args[4].split(",");
							List<String> parentlist = Arrays.asList(array);
							for(String parent:parentlist){
								if(!Main.gManager.exists(parent)){
									sender.sendMessage(ChatColor.RED+"Error setting parents; Group "+ChatColor.YELLOW+parent+ChatColor.RED+" does not exist!");
									return false;
								}
								if(args[1].equals(parent)){
									sender.sendMessage(ChatColor.RED+"Cannot set parent of a group to itself!");
									return false;
									}
							}
							
							Main.gManager.setParents(args[1], args[4]);
							sender.sendMessage(ChatColor.GOLD+"Successfully set inheritance for group "+ChatColor.YELLOW+args[1]);
							return true;
						}
						if(args[3].equalsIgnoreCase("add")){
							if(!sender.hasPermission("zpermissions.group.parents.add")){
								sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
								return false; 
							}
							if(args.length!=5){
								sender.sendMessage("Correct Usage: /perm group <group> parents add <parent>");
								return false;
								}
							if(!Main.gManager.exists(args[4])){
								sender.sendMessage(ChatColor.RED+"Error setting parents; Group "+ChatColor.YELLOW+args[4]+ChatColor.RED+" does not exist!");
								return false;
								}
							if(args[1].equals(args[4])){
								sender.sendMessage(ChatColor.RED+"Cannot set parent of a group to itself!");
								return false;
								}
							if(!Main.gManager.addParent(args[1], args[4])){
								sender.sendMessage(ChatColor.YELLOW+args[4]+ChatColor.RED+" is already a parent of "+ChatColor.YELLOW+args[1]+ChatColor.RED+"!");
								return false;
								}
							sender.sendMessage(ChatColor.GOLD+"Successfully added parent to "+ChatColor.YELLOW+args[1]);
							return true;
						}
						if(args[3].equalsIgnoreCase("remove")){
							if(!sender.hasPermission("zpermissions.group.parents.remove")){
								sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
								return false; 
							}
							if(args.length!=5){
								sender.sendMessage("Correct Usage: /perm group <group> parents remove <parent>");
								return false;
								}
							if(!Main.gManager.exists(args[4])){
								sender.sendMessage(ChatColor.RED+"Error setting parents; Group "+ChatColor.YELLOW+args[4]+ChatColor.RED+" does not exist!");
								return false;
								}
							if(!Main.gManager.removeParent(args[1], args[4])){
								sender.sendMessage(ChatColor.YELLOW+args[4]+ChatColor.RED+" is not a parent of "+ChatColor.YELLOW+args[1]+ChatColor.RED+"!");
								return false;
								}
							sender.sendMessage(ChatColor.GOLD+"Successfully removed parent from "+ChatColor.YELLOW+args[1]);
							return true;
						}
						
						sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
						sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents set <parent(s)>");
						sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents add <parent>");
						sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents remove <parent>");
						return false;
				}
				if(args[2].equalsIgnoreCase("prefix")){
					if(!sender.hasPermission("zpermissions.group.prefix.set")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(args.length!=4){
						sender.sendMessage("Correct Usage: /perm group <group> prefix <prefix>");
						return false;
						}
					if(!Main.gManager.exists(args[1])){
						sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[1]);
						return false;
					}
					
					Main.gManager.setPrefix(args[1], args[3]);
					
					sender.sendMessage(ChatColor.GOLD+"Changed prefix of "+ChatColor.YELLOW+args[1]+ChatColor.GOLD+" to "+colorize(args[3]));
					return true;
				}
				if(args[2].equalsIgnoreCase("suffix")){
					if(!sender.hasPermission("zpermissions.group.suffix.set")){
						sender.sendMessage(ChatColor.DARK_RED+"You do not have permission to use this command!");
						return false; 
					}
					if(args.length!=4){
						sender.sendMessage("Correct Usage: /perm group <group> suffix <suffix>");
						return false;
						}
					if(!Main.gManager.exists(args[1])){
						sender.sendMessage(ChatColor.RED+"No Group found by the name of "+ChatColor.YELLOW+args[1]);
						return false;
					}
					
					Main.gManager.setSuffix(args[1], args[3]);
					
					sender.sendMessage(ChatColor.GOLD+"Changed suffix of "+ChatColor.YELLOW+args[1]+ChatColor.GOLD+" to "+colorize(args[3]));
					return true;
				}
				sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
				sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.YELLOW+"Group Commands:");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm default group");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm set default group <group>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group [group]");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> create");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> delete");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> add <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> remove <permission>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents set <parent(s)>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents add <parent>");
				sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents remove <parent>");
				return false;
			}
			sender.sendMessage(ChatColor.BLUE+"|====[  "+ChatColor.GREEN+"Z Permissions"+ChatColor.BLUE+"  ]====|");
			sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.YELLOW+"User Commands:");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> add <permission>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> remove <permission>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group set <group>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group add <group>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm user <player> group remove <group>");
			//sender.sendMessage(ChatColor.BLUE+"|   ");
			sender.sendMessage(ChatColor.BLUE+"| "+ChatColor.YELLOW+"Group Commands:");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm default group");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm set default group <group>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group [group]");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> create");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> delete");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> add <permission>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> remove <permission>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents set <parent(s)>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents add <parent>");
			sender.sendMessage(ChatColor.BLUE+"| - "+ChatColor.GREEN+"/perm group <group> parents remove <parent>");
			return false;
		}
		
		return false;
	}
	
	
	public static String colorize(String s){
        if(s == null) return null;
        return s.replaceAll("&([0-9a-f])", "\u00A7$1");
	}

}
