package me.sxstore.nocheat.checks;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.sxstore.nocheat.Config;
import me.sxstore.nocheat.NoCheatMain;
import me.sxstore.nocheat.playerdata.DataManager;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.utils.LogUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

public abstract class Check implements Listener {

    public double vl, maxVL;
    public boolean enabled, punishable;
    public String checkName, checkType, punishCommand;

    protected int preVL;

    public Check(){
        checkName = getCheckName();
        checkType = getCheckType();
        enabled = NoCheatMain.getInstance().getConfig().getBoolean(checkName.toLowerCase() + ".enabled");
        punishable = NoCheatMain.getInstance().getConfig().getBoolean(checkName.toLowerCase() + ".punishable");
        punishCommand = NoCheatMain.getInstance().getConfig().getString(checkName.toLowerCase() + ".punish-command");
        maxVL = NoCheatMain.getInstance().getConfig().getInt(checkName.toLowerCase() + ".max-vl");
        Bukkit.getPluginManager().registerEvents(this, NoCheatMain.getInstance());
    }

    private String getCheckName() { return this.getClass().getAnnotation(CheckInfo.class).name(); }

    private String getCheckType() { return this.getClass().getAnnotation(CheckInfo.class).type(); }

    public void onPacketReceive(final PacketReceiveEvent e, final PlayerData data){}
    public void onPacketSend(final PacketSendEvent e, final PlayerData data){}
    public void onMove(final PlayerData data){}
    public void onAttack(final WrappedPacketInUseEntity packet, final PlayerData data){}

    protected void flag(PlayerData data, String information, boolean setback){

        assert data != null;

        if (enabled){
            vl++;
            data.setTotalFlags(data.getTotalFlags() + 1);

            data.setLegitTick(data.getTicks());

            TextComponent alertMessage = new TextComponent(ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "NoCheat" + ChatColor.GRAY + "] " + ChatColor.DARK_GREEN + data.getPlayer().getName() + ChatColor.WHITE + " failed " + getCheckName() + " (" + getCheckType() + ") VL: " + vl);
            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + data.getPlayer().getName()));
            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7(Click to teleport)\n Info: " + information).create()));

            if (Config.ENABLE_LOGGING)LogUtils.logToFile(data.getLogFile(), "[NoCheat] " + data.getPlayer().getName() + " failed " + getCheckName() + " (" + getCheckType() + ") [Info: " + information + "]");
            if (Config.LOG_TO_CONSOLE)Bukkit.getLogger().info("[NoCheat] " + data.getPlayer().getName() + " failed " + getCheckName() + " (" + getCheckType() + ") [Info: " + information + "]");

            for (PlayerData staff : DataManager.INSTANCE.getUsers()) {
                if (staff.isAlerts()){
                    if (staff.getPlayer().isOp() || staff.getPlayer().hasPermission("anticheat.alerts")){
                        staff.getPlayer().spigot().sendMessage(alertMessage);
                    }
                }
            }

            if (punishable && vl >= maxVL && !data.getPlayer().hasPermission("nocheat.bypass")){
                Bukkit.getServer().getScheduler().runTask(NoCheatMain.getInstance(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), punishCommand.replace("%player%", data.getPlayer().getName()).replace("%check%", checkName)));
            }
            if(setback && !data.getPlayer().hasPermission("nocheat.bypass")){
                if(data.getLastLegitLocation() != null)
                    data.getPlayer().teleport(data.getLastLegitLocation());
                else
                    data.getPlayer().teleport(data.getLastLocation());
            }
        }
    }

    protected long time(){ return System.nanoTime() / 1000000; }
    protected long elapsed(long num1, long num2){ return num1 - num2; }
    protected double elapsed(double num1, double num2){ return num1 - num2; }
    protected void Debug(Object object) { Bukkit.broadcastMessage(String.valueOf(object)); }
    protected void debug(Object object) { Bukkit.broadcastMessage(String.valueOf(object)); }
    protected boolean isFlyingPacket(PacketReceiveEvent event) { return PacketType.Client.Util.isInstanceOfFlying(event.getPacketId()); }
}
