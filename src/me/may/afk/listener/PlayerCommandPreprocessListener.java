package me.may.afk.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.may.afk.Entry;
import me.may.afk.command.AFKCommand;

/**
 * ���ʹ���������
 * 
 * ��������: ����һ��ڹһ�ʱʹ����������
 * @author May_Speed
 */
public class PlayerCommandPreprocessListener implements Listener {
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		if(AFKCommand.afkPlayer.contains(player.getName())) {
			String msg = e.getMessage().toLowerCase();
			if((!msg.equals("/gj")) && (!msg.startsWith("/gj "))) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKUseCommand").replaceAll("&", "��"));
			}
		}
    }
	
}
