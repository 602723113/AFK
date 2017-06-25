package com.mayspeed.afk;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFKListener implements Listener {
	
	FileConfiguration config;
	
    @EventHandler
	  public void move(PlayerMoveEvent e) {
	    Location after = e.getTo();
	    Location before = e.getFrom();
	    if ((after.getBlockX() == before.getBlockX()) && 
	      (after.getBlockY() == before.getBlockY()) && 
	      (after.getBlockZ() == before.getBlockZ())) {
	      return;
	    }
	    Player player = e.getPlayer();
	    
	    if(AFKCommand.afkPlayer.indexOf(player) != -1) {
	    	if(before.getY() != after.getY()) {
		    	e.setCancelled(true);
		    	e.getPlayer().teleport(before);
		    }
	    	e.setCancelled(true);
	    	e.getPlayer().teleport(before);
	    }
	  }
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onMove(PlayerMoveEvent e) {
		if(AFKCommand.afkPlayer.indexOf(e.getPlayer()) != -1) {
			Player player = (Player) e.getPlayer();
			if (player.getLocation().distance(e.getTo()) <= 1.0D) {
				e.setCancelled(true);
				e.getPlayer().teleport(e.getFrom());
			}
		}
	}

	/*����һ��ڹһ�ʱ ͻȻ���� �� ͻȻ�Ͽ� ����֮������*/
	@EventHandler
	public void Quit2(PlayerQuitEvent e) {
		if (AFKCommand.afkPlayer.indexOf(e.getPlayer()) != -1) {
			for(int i = 0; i < AFKCommand.afkPlayer.size(); i++) {
				if ((AFKCommand.afkPlayer.get(i)).getName().contains(e.getPlayer().getName())) {
					AFKCommand.map.get(e.getPlayer()).delete();
			        AFKCommand.afkPlayer.remove(i);
			        break;
			    }
			}
		}
	}
	
	/*����һ��ڹһ�ʱͻȻ���������*/
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) e.getEntity();
		if (AFKCommand.afkPlayer.indexOf(player) != -1) {
			for(int i = 0; i < AFKCommand.afkPlayer.size(); i++) {
				if ((AFKCommand.afkPlayer.get(i)).getName().contains(player.getName())) {
					AFKCommand.map.get(player).delete();
			        AFKCommand.afkPlayer.remove(i);
			        break;
			    }
			}
		}
	}
	
	/*��ֹ���ʹ��ָ��*/
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		if(AFKCommand.afkPlayer.indexOf(e.getPlayer()) != -1) {
			config = AFK.getInstance().getConfig();
			String msg = e.getMessage().toLowerCase();
			if((!msg.equals("/gj")) && (!msg.startsWith("/gj "))) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(config.getString("usecmd").replaceAll("&", "��"));
			}
		}
    }
	
	/*��ֹ���ʹ��ָ��*/
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (AFKCommand.afkPlayer.indexOf(e.getPlayer()) != -1) { //�ж��Ƿ����ڹһ���
			config = AFK.getInstance().getConfig();
			e.setCancelled(true);
			e.getPlayer().sendMessage(config.getString("usecmd").replaceAll("&", "��"));
		}
	}
}
