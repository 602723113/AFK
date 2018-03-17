package me.may.afk.command;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ResidenceManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.may.afk.Entry;
import me.may.afk.util.ResidenceUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class AFKCommand implements CommandExecutor {

    public static List<String> afkPlayer = Lists.newArrayList();
    public static HashMap<String, Hologram> map = Maps.newHashMap();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gj")) {
            if (args.length == 0) {
                sender.sendMessage("§7====== §8[§6AFK§8] §7======");
                sender.sendMessage("§b/gj on §7开启挂机状态");
                sender.sendMessage("§b/gj off §7关闭挂机状态");
                sender.sendMessage("§b/gj reload §7重载插件");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("§f请在游戏中输入!");
                return true;
            }

            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("reload")) {
                player.sendMessage("§8[§6挂机§8] §e> §a插件已重载");
                Entry.getInstance().reloadConfig();
                Entry.getInstance().resetHoloItem();
                return true;
            }

            if ((args[0].equalsIgnoreCase("on"))) {
                if (!player.hasPermission("gj.on")) {
                    player.sendMessage("§c权限不足!");
                    return true;
                }
                if (afkPlayer.contains(player.getName())) { //如果玩家存在于 正在挂机列表中
                    player.sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKIng").replaceAll("&", "§"));
                    return true;
                }
                // 1.4 Fix: 修复玩家在空中时做挂机的操作
                if (player.getLocation().add(0D, -1D, 0D).getBlock().getType() == Material.AIR) {
                    player.sendMessage(Entry.getInstance().getConfig().getString("Tips.WhenInAirAFK").replaceAll("&", "§"));
                    return true;
                }

                Location loc = player.getLocation();
                // 1.4 Feature: 区域挂机
                if (Entry.getInstance().getConfig().getBoolean("Residence.Enable")) {
                    if (!ResidenceUtils.isInResidence(player)) {
                        player.sendMessage(Entry.getInstance().getConfig().getString("Residence.NoInAResidence").replaceAll("&", "§"));
                        return true;
                    }

                    ResidenceManager residenceManager = Residence.getInstance().getResidenceManager();
                    String residenceName = Entry.getInstance().getConfig().getString("Residence.Name");
                    if (!residenceManager.getByLoc(loc).getName().equals(residenceName)) {
                        player.sendMessage(Entry.getInstance().getConfig().getString("Residence.NoInRightResidence")
                                .replaceAll("&", "§")
                                .replaceAll("%res_name%", residenceName)
                        );
                        return true;
                    }
                }

                /*hologram位置设置+生成部分*/
                loc.setY(loc.getY() + 3.25); //位置向上提升3.25格
                Hologram hologram = HologramsAPI.createHologram(Entry.getInstance(), loc);
                hologram.appendItemLine(Entry.getInstance().getHologramItem());
                hologram.appendTextLine(Entry.getInstance().getConfig().getString("Item.Line").replaceAll("&", "§"));

                afkPlayer.add(player.getName()); //将玩家添加进挂机者列表中
                map.put(player.getName(), hologram);

                /*信息提示*/
                player.sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKRun").replaceAll("&", "§"));
                return true;
            }

            if ((args[0].equalsIgnoreCase("off"))) {
                if (!afkPlayer.contains(player.getName())) {
                    player.sendMessage(Entry.getInstance().getConfig().getString("Tips.IsNotAFK").replaceAll("&", "§"));
                    return true;
                }
                /*hologram删除+移除挂机列表中的该玩家部分*/
                for (int i = 0; i < afkPlayer.size(); i++) {
                    if (afkPlayer.get(i).contains(player.getName())) {
                        map.get(player.getName()).delete();
                        afkPlayer.remove(player.getName());
                        player.sendMessage(Entry.getInstance().getConfig().getString("Tips.AFKQuit").replaceAll("&", "§"));
                    }
                }
                return true;
            }
        }
        return false;
    }
}
