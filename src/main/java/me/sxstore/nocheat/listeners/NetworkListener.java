package me.sxstore.nocheat.listeners;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import me.sxstore.nocheat.playerdata.DataManager;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.processors.PacketProcessor;
import me.sxstore.nocheat.processors.VelocityProcessor;

public class NetworkListener implements PacketListener {

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        PlayerData data = DataManager.INSTANCE.getUser(e.getPlayer().getUniqueId());
        if (data != null) {
            PacketProcessor.process(e);
            data.inbound(e);
            VelocityProcessor.processTransaction(e);
        }
    }

    @PacketHandler
    public void onSend(PacketSendEvent e) {
        PlayerData data = DataManager.INSTANCE.getUser(e.getPlayer().getUniqueId());
        if (data != null) {
            data.outgoing(e);
            VelocityProcessor.process(e);
            if (e.getPacketId() == PacketType.Server.POSITION)data.setTeleportTicks(data.getTicks());
        }
    }
}
