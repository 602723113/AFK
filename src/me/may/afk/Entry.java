package me.may.afk;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;

import me.may.afk.command.AFKCommand;
import me.may.afk.dto.Vault;
import me.may.afk.listener.PlayerCommandPreprocessListener;
import me.may.afk.listener.PlayerDeathListener;
import me.may.afk.listener.PlayerInteractListener;
import me.may.afk.listener.PlayerQuitListener;
import me.may.afk.task.CheckRunnable;

public class Entry extends JavaPlugin {
	
	private static Entry instance;
	private Vault vault;
	private ItemStack item;
	private BukkitTask task;
	private ResidenceApi residenceAPI;
	private CheckRunnable checkRunnable;
	
	
	public void onEnable() {
		instance = this;
		register();
		saveDefaultConfig();
		hookResidence();
	}
	
	public void onDisable() {
		task.cancel();
		item = null;
	}
	
	public static Entry getInstance() {
		return instance;
	}
	
	public ResidenceApi getResidenceAPI() {
		return residenceAPI;
	}
	
	public Vault getEconomy() {
		if (vault == null) {
			vault = new Vault();
		}
		return vault;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getHologramItem() {
		if (item == null) {
			item = new ItemStack(Entry.getInstance().getConfig().getInt("Item.Material"), 1, (short) Entry.getInstance().getConfig().getInt("Item.Data"));
		}
		return item;
	}
	
	private void register() {
		Bukkit.getPluginCommand("gj").setExecutor(new AFKCommand());
		Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(), this);
		checkRunnable = new CheckRunnable();
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(this, checkRunnable, 30L, Entry.getInstance().getConfig().getLong("Task.Period") * 20);
	}
	
	private void hookResidence() {
		Plugin resPlugin = getServer().getPluginManager().getPlugin("Residence");
		if (resPlugin != null) {
			residenceAPI = Residence.getInstance().getAPI();
		}
	}
}
