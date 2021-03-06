package me.may.afk.task;

import me.may.afk.Entry;
import me.may.afk.command.AFKCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class CheckRunnable implements Runnable {

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (int i = 0; i < AFKCommand.afkPlayer.size(); i++) {
            String name = AFKCommand.afkPlayer.get(i);
            if (AFKCommand.map.get(name) == null) {
                AFKCommand.afkPlayer.remove(name);
                continue;
            }
            /* 离线检查开始 */
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            if (offlinePlayer == null) {
                AFKCommand.afkPlayer.remove(name);
                AFKCommand.map.get(name).delete();
                continue;
            }
            if (!offlinePlayer.isOnline()) {
                AFKCommand.afkPlayer.remove(name);
                AFKCommand.map.get(name).delete();
                continue;
            }
            /* 离线检查结束 */
            Player player = offlinePlayer.getPlayer();

            List<String> commands = Entry.getInstance().getConfig().getStringList("Task.Commands");
            commands.forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("&", "§").replaceAll("%player%", player.getName())));

//            player.giveExp(Entry.getInstance().getConfig().getInt("Task.Exp"));
//            Entry.getInstance().getEconomy().addMoney(name, Entry.getInstance().getConfig().getDouble("Task.Money"));
        }
    }
}
