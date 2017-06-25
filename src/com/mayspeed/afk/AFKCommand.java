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
	public static List<Player> afkPlayer = new ArrayList<Player>(); //��ҹһ��б�
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
			/*�ж��Ƿ������*/
			if(!(sender instanceof Player)) { 
				sender.sendMessage("������Ϸ������!");
				return true;
			}
			
			config = plugin.getConfig();
			Player player = (Player) sender;
			Location loc = player.getLocation();
			
			if(args.length == 0) {
				player.sendMessage("��6��l[��a��lAFK ��e��l> ��6��l�һ������6��l]");
				player.sendMessage("��b/gj on ��7�����һ�״̬");
				player.sendMessage("��b/gj off ��7�رչһ�״̬");
				player.sendMessage("��b/gj reload ��7���ز��");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reload")) {
				if(args.length != 1) {
					player.sendMessage("��6��l[�һ����] ��e��l-> ��c��������ȷ");
					return true;
				}
				player.sendMessage("��6��l[�һ����] ��e��l-> ��a���������");
				plugin.reloadConfig();
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("on"))) {
				if(args.length != 1) {
					player.sendMessage("��6��l[�һ����] ��e��l-> ��c��������ȷ");
					return true;
				}
				if(!player.hasPermission("gj.on")) {
					player.sendMessage("��cȨ�޲���");
					return true;
				}
				if(afkPlayer.indexOf(player) != -1) { //�����Ҵ����� ���ڹһ��б���
					player.sendMessage(config.getString("gjing").replaceAll("&", "��"));
					return true;
				}
				
				if(player.getLocation().add(0D, -1D, 0D).getBlock().getType() == Material.AIR) {
					player.sendMessage("��6��l[�һ����] ��e��l-> ��c�����ڿ���ʹ�ùһ�");
					return true;
				}
				
				//�ж��Ƿ�������һ�
				if(AFK.getInstance().getConfig().getBoolean("Residence.Enable")) {
					if(!AFK.getInstance().playerIsInResidence(player)) {
						player.sendMessage(AFK.getInstance().getConfig().getString("Residence.NoInAResidence").replaceAll("&", "��"));
						return true;
					}
					ResidenceManager resmanage = Residence.getResidenceManager();
					System.out.println(AFK.getInstance().getConfig().getString("Residence.Name") == resmanage.getByLoc(player.getLocation()).getResidenceName());
					if(!resmanage.getByLoc(player.getLocation()).getResidenceName().equals(AFK.getInstance().getConfig().getString("Residence.Name"))) {
						player.sendMessage(AFK.getInstance().getConfig().getString("Residence.NoInRightResidence").replaceAll("&", "��").replaceAll("%res_name%", AFK.getInstance().getConfig().getString("Residence.Name")));
						return true;
					}
				}
				
				/*hologramλ������+���ɲ���*/
				loc.setY(player.getLocation().getY() + 3.25);
				Hologram hologram = (Hologram) HologramsAPI.createHologram(plugin, loc);
				hologram.appendItemLine(holoItem());
				hologram.appendTextLine(config.getString("Line").replaceAll("&", "��"));
				
				afkPlayer.add(player); //�������ӽ��һ����б���
				map.put(player, hologram);
				
				/*��Ϣ��ʾ*/
				player.sendMessage(config.getString("gjrun").replaceAll("&", "��"));
				/*ѭ������*/
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
					player.sendMessage("��6��l[�һ����] ��e��l-> ��c��������ȷ");
					return true;
				}
				if(!(afkPlayer.indexOf(player) != -1)) {
					//�ж�����Ƿ������ڹһ�
					player.sendMessage(config.getString("nogj").replaceAll("&", "��"));
					return true;
				}
				/*hologramɾ��+�Ƴ��һ��б��еĸ���Ҳ���*/
				for(int i = 0; i < afkPlayer.size(); i++) {
					if ((afkPlayer.get(i)).getName().contains(player.getName())) {
						map.get(player).delete();
				        afkPlayer.remove(i);
				        break;
				    }
				}
				
				player.sendMessage(config.getString("gjdone").replaceAll("&", "��"));
				return true;
			}
		}
		return false;
	}
}
