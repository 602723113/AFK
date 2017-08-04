package me.may.afk.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.may.afk.Entry;
import me.may.afk.command.AFKCommand;

/**
 * �����������
 * 
 * ��������: ����һ��ڹһ�ʱʹ��һЩ��Ʒ�����
 * @author May_Speed
 */
public class PlayerInteractListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if(AFKCommand.afkPlayer.contains(player.getName())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKUseCommand").replaceAll("&", "��"));
		}
	}
	
}
