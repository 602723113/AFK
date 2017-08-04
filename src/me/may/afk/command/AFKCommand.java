package me.may.afk.command;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ResidenceManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.may.afk.Entry;
import me.may.afk.util.ResidenceUtils;
	
public class AFKCommand implements CommandExecutor {
	
	public static List<String> afkPlayer = Lists.newArrayList();
	public static HashMap<String, Hologram> map = Maps.newHashMap();
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("gj")) {
			if(args.length == 0) {
				sender.sendMessage("��7====== ��8[��6AFK��8] ��7======");
				sender.sendMessage("��b/gj on ��7�����һ�״̬");
				sender.sendMessage("��b/gj off ��7�رչһ�״̬");
				sender.sendMessage("��b/gj reload ��7���ز��");
				return true;
			}
			
			if(!(sender instanceof Player)) { 
				sender.sendMessage("��f������Ϸ������!");
				return true;
			}
			
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("reload")) {
				player.sendMessage("��8[��6�һ���8] ��e> ��a���������");
				Entry.getInstance().reloadConfig();
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("on"))) {
				if(!player.hasPermission("gj.on")) {
					player.sendMessage("��cȨ�޲���!");
					return true;
				}
				if(afkPlayer.contains(player.getName())) { //�����Ҵ����� ���ڹһ��б���
					player.sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKIng").replaceAll("&", "��"));
					return true;
				}
				
				if(player.getLocation().add(0D, -1D, 0D).getBlock().getType() == Material.AIR) {
					player.sendMessage("��8[��6�һ���8] ��e> ��c�����ڿ��йһ�");
					return true;
				}
				
				Location loc = player.getLocation();
				// ����һ�
				if(Entry.getInstance().getConfig().getBoolean("Residence.Enable")) {
					if(!ResidenceUtils.isInResidence(player)) {
						player.sendMessage(Entry.getInstance().getConfig().getString("Residence.NoInAResidence").replaceAll("&", "��"));
						return true;
					}
					
					ResidenceManager residenceManager = Residence.getInstance().getResidenceManager();
					String residenceName = Entry.getInstance().getConfig().getString("Residence.Name");
					if (!residenceManager.getByLoc(loc).getName().equals(residenceName)) {
						player.sendMessage(Entry.getInstance().getConfig().getString("Residence.NoInRightResidence")
							.replaceAll("&", "��")
							.replaceAll("%res_name%", residenceName)
						);
						return true;
					}
				}
				
				/*hologramλ������+���ɲ���*/
				loc.setY(loc.getY() + 3.25); //λ����������3.25��
				Hologram hologram = (Hologram) HologramsAPI.createHologram(Entry.getInstance(), loc);
				hologram.appendItemLine(Entry.getInstance().getHologramItem());
				hologram.appendTextLine(Entry.getInstance().getConfig().getString("Item.Line").replaceAll("&", "��"));
				
				afkPlayer.add(player.getName()); //�������ӽ��һ����б���
				map.put(player.getName(), hologram);
				
				/*��Ϣ��ʾ*/
				player.sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKRun").replaceAll("&", "��"));
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("off"))) {
				if(!afkPlayer.contains(player.getName())) {
					player.sendMessage(Entry.getInstance().getConfig().getString("Tips.IsNotAFK").replaceAll("&", "��"));
					return true;
				}
				/*hologramɾ��+�Ƴ��һ��б��еĸ���Ҳ���*/
				for(int i = 0; i < afkPlayer.size(); i++) {
					if (afkPlayer.get(i).contains(player.getName())) {
						map.get(player.getName()).delete();
				        afkPlayer.remove(player.getName());
				        player.sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKQuit").replaceAll("&", "��"));
					}
				}
				return true;
			}
		}
		return false;
	}
}
