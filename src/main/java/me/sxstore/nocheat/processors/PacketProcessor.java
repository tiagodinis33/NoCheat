package me.sxstore.nocheat.processors;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.sxstore.nocheat.playerdata.DataManager;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.utils.MathUtils;
import me.sxstore.nocheat.utils.PlayerUtils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PacketProcessor {

    @SuppressWarnings("Deprecated")
    public static void process(PacketReceiveEvent event) {
        PlayerData data = DataManager.INSTANCE.getUser(event.getPlayer().getUniqueId());
        if (data != null) {
            if (PacketType.Client.Util.isInstanceOfFlying(event.getPacketId())) {
                WrappedPacketInFlying wrappedPacketInFlying = new WrappedPacketInFlying(event.getNMSPacket());

                data.setTicks(data.getTicks() + 1);

                data.setServerOnGround(PlayerUtils.onGround(data));
                data.setOnGround(wrappedPacketInFlying.isOnGround());
                if(!PlayerUtils.onGround(data)){

                    data.setAirTicks(data.getAirTicks() + 1);
                }else
                    data.setAirTicks(0);
                if (!data.getPlayer().isOnGround()) {
                    data.setGroundTicks(0);
                } else
                data.setGroundTicks(data.getGroundTicks() + 1);
                if (PlayerUtils.onGround(data))
                    data.setLastOnGroundLocation(data.getLocation());

                if (PlayerUtils.isOnIce(data))
                    data.setIceTicks(data.getTicks());
                if (PlayerUtils.isOnSlime(data))
                    data.setSlimeTicks(data.getTicks());
                if (PlayerUtils.blockNearHead(data))
                    data.setUnderBlockTicks(data.getTicks());
                if (PlayerUtils.inLiquid(data))
                    data.setLiquidTicks(data.getTicks());
                if (data.isSprinting())
                    data.setSprintingTicks(data.getTicks());

                if (data.getTicks() - data.getLegitTick() == 0)
                    data.setLastLegitLocation(data.getLocation());
                double velocity = data.getPlayer().getVelocity().length();
                data.getVelocityData().setLastVelocity(data.getVelocityData().getCurrentVelocity());
                data.getVelocityData().setCurrentVelocity(velocity);
                data.setLastIsFlying(data.isFlying());
                data.setFlying(data.getPlayer().isFlying());
            } else if (event.getPacketId() == PacketType.Client.ENTITY_ACTION) {
                WrappedPacketInEntityAction packet = new WrappedPacketInEntityAction(event.getNMSPacket());
                switch (packet.getAction()) {
                    case START_SPRINTING:
                        data.setSprinting(true);
                        break;
                    case STOP_SPRINTING:
                        data.setSprinting(false);
                        break;
                    case START_SNEAKING:
                        data.setSneaking(true);
                        break;
                    case STOP_SNEAKING:
                        data.setSneaking(false);
                        break;
                    default:
                        break;
                }
            } else if (event.getPacketId() == PacketType.Client.BLOCK_DIG) {
                WrappedPacketInBlockDig wrappedPacketInBlockDig = new WrappedPacketInBlockDig(event.getNMSPacket());
                if (wrappedPacketInBlockDig.getDigType() == WrappedPacketInBlockDig.PlayerDigType.START_DESTROY_BLOCK)
                    data.setDigging(true);
                else if (wrappedPacketInBlockDig
                        .getDigType() == WrappedPacketInBlockDig.PlayerDigType.ABORT_DESTROY_BLOCK
                        || wrappedPacketInBlockDig
                                .getDigType() == WrappedPacketInBlockDig.PlayerDigType.STOP_DESTROY_BLOCK) {
                    data.setDigging(false);
                }
            }
        }
    }
}
