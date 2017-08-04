package me.may.afk.dto;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class Vault {
	
	public Vault() {
		setupMoney();
	}

	private Economy economy;

	@SuppressWarnings("deprecation")
	public boolean addMoney(String name, double money) {
		this.economy.depositPlayer(name, money);
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean delMoney(String name, double money) {
		if (!this.economy.hasAccount(name)) {
			return false;
		}
		if (this.economy.has(name, money)) {
			this.economy.withdrawPlayer(name, money);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public int getMoney(String name) {
		return (int) economy.getBalance(name);
	}

	@SuppressWarnings("deprecation")
	public boolean hasMoney(String name, double money) {
		if ((name == null) || (name.length() <= 0)) {
			return false;
		}
		if (money <= 0.0D) {
			return true;
		}
		return this.economy.has(name, money);
	}

	private boolean setupMoney() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			this.economy = ((Economy) economyProvider.getProvider());
		}
		return true;
	}

}
