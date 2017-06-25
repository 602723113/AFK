package com.mayspeed.afk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ResidenceManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class AFKCommand implements CommandExecutor {
	
	private final AFK plugin;
	FileConfiguration config;
	public static List<Player> afkPlayer = new ArrayList<Player>(); //玩家挂机列表
	public static HashMap<Player, Hologram> map = new HashMap<Player, Hologram>();

	public AFKCommand(AFK plugin) {
		this.plugin = plugin;
	}
	
    @SuppressWarnings("deprecation")
	public static ItemStack holoItem() {
      ItemStack i = new ItemStack(Material.getMaterial(AFK.getInstance().getConfig().getInt("Item.id")) ,1 ,(short)AFK.getInstance().getConfig().getInt("Item.data"));
      return i;
    }
    
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("gj")) {
			/*判断是否是玩家*/
			if(!(sender instanceof Player)) { 
				sender.sendMessage("请在游戏中输入!");
				return true;
			}
			
			config = plugin.getConfig();
			Player player = (Player) sender;
			Location loc = player.getLocation();
			
			if(args.length == 0) {
				player.sendMessage("§6§l[§a§lAFK §e§l> §6§l挂机插件§6§l]");
				player.sendMessage("§b/gj on §7开启挂机状态");
				player.sendMessage("§b/gj off §7关闭挂机状态");
				player.sendMessage("§b/gj reload §7重载插件");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reload")) {
				if(args.length != 1) {
					player.sendMessage("§6§l[挂机插件] §e§l-> §c参数不正确");
					return true;
				}
				player.sendMessage("§6§l[挂机插件] §e§l-> §a插件已重载");
				plugin.reloadConfig();
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("on"))) {
				if(args.length != 1) {
					player.sendMessage("§6§l[挂机插件] §e§l-> §c参数不正确");
					return true;
				}
				if(!player.hasPermission("gj.on")) {
					player.sendMessage("§c权限不足");
					return true;
				}
				if(afkPlayer.indexOf(player) != -1) { //如果玩家存在于 正在挂机列表中
					player.sendMessage(config.getString("gjing").replaceAll("&", "§"));
					return true;
				}
				
				if(player.getLocation().add(0D, -1D, 0D).getBlock().getType() == Material.AIR) {
					player.sendMessage("§6§l[挂机插件] §e§l-> §c请勿在空中使用挂机");
					return true;
				}
				
				//判断是否是区域挂机
				if(AFK.getInstance().getConfig().getBoolean("Residence.Enable")) {
					if(!AFK.getInstance().playerIsInResidence(player)) {
						player.sendMessage(AFK.getInstance().getConfig().getString("Residence.NoInAResidence").replaceAll("&", "§"));
						return true;
					}
					ResidenceManager resmanage = Residence.getResidenceManager();
					System.out.println(AFK.getInstance().getConfig().getString("Residence.Name") == resmanage.getByLoc(player.getLocation()).getResidenceName());
					if(!resmanage.getByLoc(player.getLocation()).getResidenceName().equals(AFK.getInstance().getConfig().getString("Residence.Name"))) {
						player.sendMessage(AFK.getInstance().getConfig().getString("Residence.NoInRightResidence").replaceAll("&", "§").replaceAll("%res_name%", AFK.getInstance().getConfig().getString("Residence.Name")));
						return true;
					}
				}
				
				/*hologram位置设置+生成部分*/
				loc.setY(player.getLocation().getY() + 3.25);
				Hologram hologram = (Hologram) HologramsAPI.createHologram(plugin, loc);
				hologram.appendItemLine(holoItem());
				hologram.appendTextLine(config.getString("Line").replaceAll("&", "§"));
				
				afkPlayer.add(player); //将玩家添加进挂机者列表中
				map.put(player, hologram);
				
				/*信息提示*/
				player.sendMessage(config.getString("gjrun").replaceAll("&", "§"));
				/*循环部分*/
				new BukkitRunnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						if(!(afkPlayer.indexOf(player) != -1)) {
							cancel();
							return;
						}
						player.giveExp(config.getInt("exp"));
						AFK.economy.depositPlayer(player.getName(), config.getDouble("money"));
					}
				}.runTaskTimerAsynchronously(plugin, 100L, config.getInt("period") * 20);
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("off"))) {
				if(args.length != 1) {
					player.sendMessage("§6§l[挂机插件] §e§l-> §c参数不正确");
					return true;
				}
				if(!(afkPlayer.indexOf(player) != -1)) {
					//判断玩家是否是正在挂机
					player.sendMessage(config.getString("nogj").replaceAll("&", "§"));
					return true;
				}
				/*hologram删除+移除挂机列表中的该玩家部分*/
				for(int i = 0; i < afkPlayer.size(); i++) {
					if ((afkPlayer.get(i)).getName().contains(player.getName())) {
						map.get(player).delete();
				        afkPlayer.remove(i);
				        break;
				    }
				}
				
				player.sendMessage(config.getString("gjdone").replaceAll("&", "§"));
				return true;
			}
		}
		return false;
	}
}
