package me.sxstore.nocheat.checks.impl.movement;

import org.bukkit.util.Vector;

import me.sxstore.nocheat.checks.Check;
import me.sxstore.nocheat.checks.CheckInfo;
import me.sxstore.nocheat.playerdata.PlayerData;

@CheckInfo(name = "Fly", type = "A")
public class FlyA extends Check {

    @Override
    public void onMove(PlayerData data) {
        if (!data.isOnClimbableBlock() && !data.isInLiquid() && !data.isInWeb() && !data.getPlayer().getAllowFlight()) {

            double deltaY = data.getDeltaY();
            double lastDeltaY = data.getLastDeltaY();
            if (data.getAirTicks() > 2 && data.velocityTicks() > 4 && !data.isServerOnGround() && Math.abs(deltaY - lastDeltaY) < 0.01) {
                if (data.flyThreshold++ < 2) {
                    return;
                }
                flag(data, "from=" + lastDeltaY + " to=" + deltaY, false);
                if (!data.canBypassFlags() && !data.isServerOnGround()) {
                    double accel = deltaY - lastDeltaY;
                    Vector vel = data.getPlayer().getVelocity().clone();
                    data.getPlayer().teleport(data.getLocation().clone().add(0, -accel, 0));
                    data.getPlayer().setVelocity(vel.setX(0).setZ(0));
                }
            }else data.flyThreshold -= data.flyThreshold > 0? 0.1f:0;

        }
        //TODO Do a better solution to eliminate random flags
    }

}