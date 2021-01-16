package me.sxstore.nocheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import me.sxstore.nocheat.Config;
import me.sxstore.nocheat.playerdata.DataManager;
import me.sxstore.nocheat.playerdata.PlayerData;

public class BukkitListener implements Listener {
@EventHandler
    public void onVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.INSTANCE.getUser(player.getUniqueId());
        data.getVelocityData().addVelocityTime(event.getVelocity());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        DataManager.INSTANCE.register(new PlayerData(event.getPlayer().getUniqueId()));
        if (Config.ENABLE_ALERTS_ON_JOIN && event.getPlayer().hasPermission("nocheat.alerts"))DataManager.INSTANCE.getUser(event.getPlayer().getUniqueId()).setAlerts(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        DataManager.INSTANCE.unregister(DataManager.INSTANCE.getUser(event.getPlayer().getUniqueId()));
    }
}
