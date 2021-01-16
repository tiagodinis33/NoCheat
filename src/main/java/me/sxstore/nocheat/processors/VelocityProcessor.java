package me.sxstore.nocheat.processors;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.out.transaction.WrappedPacketOutTransaction;
import me.sxstore.nocheat.playerdata.DataManager;
import me.sxstore.nocheat.playerdata.PlayerData;

import org.bukkit.util.Vector;

import java.util.Random;

public class VelocityProcessor {
    public static void process(PacketSendEvent e){
        if (e.getPacketId() == PacketType.Server.ENTITY_VELOCITY){
            PlayerData data = DataManager.INSTANCE.getUser(e.getPlayer().getUniqueId());
            WrappedPacketOutEntityVelocity event = new WrappedPacketOutEntityVelocity(e.getNMSPacket());
            data.setLastVel(new Vector(event.getVelocityX(), event.getVelocityY(), event.getVelocityZ()));

            data.setVerifyingVelocity(true);

            data.setVelocityID((short) new Random().nextInt(32000));
            PacketEvents.getAPI().getPlayerUtils().sendPacket(e.getPlayer(), new WrappedPacketOutTransaction(0, data.getVelocityID(), false));
        }
    }

    public static void processTransaction(PacketReceiveEvent e){
        if (e.getPacketId() == PacketType.Client.TRANSACTION){
            WrappedPacketInTransaction wrappedPacketInTransaction = new WrappedPacketInTransaction(e.getNMSPacket());

            PlayerData data = DataManager.INSTANCE.getUser(e.getPlayer().getUniqueId());
            if (data.isVerifyingVelocity() && wrappedPacketInTransaction.getActionNumber() == data.getVelocityID()){
                data.setVelTick(data.getTicks());
                data.setVerifyingVelocity(false);
                data.setMaxVelTicks((int) (((data.getLastVel().getX() + data.getLastVel().getZ()) / 2D + 2D) * 15D));
            }
        }
    }
}
