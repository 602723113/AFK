package me.may.afk.util;

import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ResidenceManager;

/**
 * ��ع�����

 * @author May_Speed
 */
public class ResidenceUtils {
	
	/**
	 * �ж�һ������Ƿ��������
	 * 
	 * @param player
	 *            ���
	 * @return true[��]/false[����]
	 */
	public static boolean isInResidence(Player player) {
		ResidenceManager resmanage = Residence.getInstance().getResidenceManager();
		if(resmanage.getByLoc(player.getLocation()) != null) {
			return true;
		}else {
			return false;
		}
	}
}
