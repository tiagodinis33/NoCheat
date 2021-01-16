package me.sxstore.nocheat;

import io.github.retrooper.packetevents.PacketEvents;
import me.sxstore.nocheat.listeners.BukkitListener;
import me.sxstore.nocheat.listeners.NetworkListener;
import me.sxstore.nocheat.playerdata.DataManager;
import me.sxstore.nocheat.playerdata.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoCheatMain extends JavaPlugin {

    private static NoCheatMain instance;

    @Override
    public void onLoad() { PacketEvents.load(); }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        Config.updateConfig();

        //PacketEvents
        PacketEvents.getSettings();
        PacketEvents.start(this);

        // Register listeners
        PacketEvents.getAPI().getEventManager().registerListener(new NetworkListener());
        getServer().getPluginManager().registerEvents(new BukkitListener(), this);

        for (Player player : getServer().getOnlinePlayers()){ DataManager.INSTANCE.register(new PlayerData(player.getUniqueId())); }
    }

    @Override
    public void onDisable() {
       PacketEvents.stop();
    }

    public static NoCheatMain getInstance() {
        return instance;
    }
}
