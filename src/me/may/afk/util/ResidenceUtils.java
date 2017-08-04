package me.may.afk.util;

import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ResidenceManager;

/**
 * 领地工具类

 * @author May_Speed
 */
public class ResidenceUtils {
	
	/**
	 * 判断一名玩家是否在领地内
	 * 
	 * @param player
	 *            玩家
	 * @return true[是]/false[不是]
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
