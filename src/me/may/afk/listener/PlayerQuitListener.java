package me.may.afk.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.may.afk.command.AFKCommand;

/**
 * ����˳�����������
 * 
 * ��������: ����һ��ڹһ�ʱ ͻȻ���� �� ͻȻ�Ͽ� ����֮������
 * @author May_Speed
 */
public class PlayerQuitListener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if(AFKCommand.afkPlayer.contains(player.getName())) {
			AFKCommand.map.get(player.getName()).delete();
	        AFKCommand.afkPlayer.remove(player.getName());
		}
	}
}
