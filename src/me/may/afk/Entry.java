package me.may.afk;

import com.bekvon.bukkit.residence.Residence;
import me.may.afk.command.AFKCommand;
import me.may.afk.listener.PlayerCommandPreprocessListener;
import me.may.afk.listener.PlayerDeathListener;
import me.may.afk.listener.PlayerInteractListener;
import me.may.afk.listener.PlayerQuitListener;
import me.may.afk.task.CheckRunnable;
import me.may.afk.task.TeleportRunnable;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Entry extends JavaPlugin {

    private static Entry instance;
    private ItemStack item;
    private BukkitTask rewardTask;
    private BukkitTask teleportTask;
    private Residence residenceInstance;

    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        hookResidence();
        register();
    }

    public void onDisable() {
        rewardTask.cancel();
        teleportTask.cancel();
        item = null;
    }

    public static Entry getInstance() {
        return instance;
    }

    public Residence getResidenceInstance() {
        return residenceInstance;
    }

    @SuppressWarnings("deprecation")
    public ItemStack getHologramItem() {
        if (item == null) {
            item = new ItemStack(Entry.getInstance().getConfig().getInt("Item.Material"), 1, (short) Entry.getInstance().getConfig().getInt("Item.Data"));
        }
        return item;
    }

    public void resetHoloItem() {
        item = null;
    }

    private void register() {
        // 命令
        Bukkit.getPluginCommand("gj").setExecutor(new AFKCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(), this);
        CheckRunnable checkRunnable = new CheckRunnable();
        TeleportRunnable teleportRunnable = new TeleportRunnable();

        rewardTask = Bukkit.getScheduler().runTaskTimer(this, checkRunnable, 30L, Entry.getInstance().getConfig().getLong("Task.RewardPeriod") * 20);
        teleportTask = Bukkit.getScheduler().runTaskTimer(this, teleportRunnable, 30L, Entry.getInstance().getConfig().getLong("Task.TeleportPeriod") * 20);
    }

    private void hookResidence() {
        Residence resPlugin = (Residence) getServer().getPluginManager().getPlugin("Residence");
        if (resPlugin != null) {
            residenceInstance = resPlugin;
        }
    }
}
