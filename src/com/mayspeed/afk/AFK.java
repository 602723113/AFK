package com.mayspeed.afk;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ResidenceManager;

import net.milkbowl.vault.economy.Economy;

public class AFK extends JavaPlugin {
	
	public static AFK instance;
	public static List<Player> players;
	public static Economy economy = null;
	public static ResidenceApi resAPI;

	private boolean initVault(){
        boolean hasNull = false;
        //初始化经济系统
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            if ((economy = economyProvider.getProvider()) == null) hasNull = true;
        }
        return !hasNull;
    }
	
	public void hookRes() {
		Plugin resPlug = getServer().getPluginManager().getPlugin("Residence");
		if (resPlug != null) {
			resAPI = Residence.getAPI();
		}
	}
	
	public void onEnable() {
		if (!initVault()){
			getLogger().info("[挂机插件]初始化Vault支持失败,请检测是否已经安装Vault插件");
		}
		getLogger().info("挂机插件 已加载 Power By May_Speed");
		Bukkit.getPluginManager().registerEvents(new AFKListener(), this);
		getCommand("gj").setExecutor(new AFKCommand(this));
		
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		File file = new File(getDataFolder(),"config.yml");
		if (!(file.exists())) {
			saveDefaultConfig();
		}
		reloadConfig();
		instance = this;
	}
	
	public static AFK getInstance() {
		return instance;
	}
	
	public static ResidenceApi getResApi() {
		return resAPI;
	}
	
	public boolean playerIsInResidence(Player player) {
		ResidenceManager resmanage = Residence.getResidenceManager();
		if(resmanage.getByLoc(player.getLocation()) != null) {
			return true;
		}else {
			return false;			
		}
	}
	
}
