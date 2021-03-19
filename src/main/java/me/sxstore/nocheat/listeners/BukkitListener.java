package me.sxstore.nocheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import me.sxstore.nocheat.Config;
import me.sxstore.nocheat.playerdata.DataManager;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.utils.MathUtils;

public class BukkitListener implements Listener {
    @EventHandler
    public void onVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.INSTANCE.getUser(player.getUniqueId());
        data.getVelocityData().addVelocityTime(event.getVelocity());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DataManager.INSTANCE.register(new PlayerData(event.getPlayer().getUniqueId()));
        if (Config.ENABLE_ALERTS_ON_JOIN && event.getPlayer().hasPermission("nocheat.alerts"))
            DataManager.INSTANCE.getUser(event.getPlayer().getUniqueId()).setAlerts(true);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player))
            return;
        PlayerData data = DataManager.INSTANCE.getUser(e.getDamager().getUniqueId());
        data.onAttack(e);
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        PlayerData data = DataManager.INSTANCE.getUser(e.getPlayer().getUniqueId());
        data.setLastLocation(data.getLocation() != null ? data.getLocation() : e.getPlayer().getLocation());
        data.setLocation(e.getTo());

        data.setLastDeltaXZ(data.getDeltaXZ());
        data.setDeltaXZ(data.getLocation().clone().toVector().setY(0)
                .distance(data.getLastLocation().clone().toVector().setY(0)));

        data.setLastDeltaY(data.getDeltaY());
        data.setDeltaY(Math.abs(data.getLocation().getY()) - Math.abs(data.getLastLocation().getY()));
        data.setLastDeltaPitch(data.getDeltaPitch());
        data.setLastDeltaYaw(data.getDeltaYaw());
        data.setDeltaYaw(Math.abs(MathUtils.getAngleDiff(e.getTo().getYaw(), data.getYaw())));
        data.setDeltaPitch(Math.abs(e.getTo().getPitch() - data.getPitch()));

        data.setYaw(e.getTo().getYaw());
        data.setPitch(e.getTo().getPitch());

        data.setDirection(new Vector(
                -Math.sin(data.getPlayer().getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F, 0,
                Math.cos(data.getPlayer().getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F));

        data.onMove();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DataManager.INSTANCE.unregister(DataManager.INSTANCE.getUser(event.getPlayer().getUniqueId()));
    }
}
