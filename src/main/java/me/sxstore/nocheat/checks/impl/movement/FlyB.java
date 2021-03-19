package me.sxstore.nocheat.checks.impl.movement;

import org.bukkit.GameMode;
import org.bukkit.util.Vector;

import me.sxstore.nocheat.checks.Check;
import me.sxstore.nocheat.checks.CheckInfo;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.utils.MathUtils;

@CheckInfo(name = "Fly", type = "B")
public class FlyB extends Check {

    @Override
    public void onMove(PlayerData data) {
        if (!data.isOnClimbableBlock() && !data.isInLiquid() && !data.isInWeb() && !data.getPlayer().getAllowFlight()) {

            double accel = data.getDeltaY() - data.getLastDeltaY();
            if (data.getAirTicks() > 1 && Math.abs(accel) < 0.01 && data.velocityTicks() > 4) {
                if (data.flyThreshold++ < 3) {
                    return;
                }
                flag(data, "accel=" + accel, false);
                if (!data.canBypassFlags() && !data.isServerOnGround()) {

                    Vector vel = data.getPlayer().getVelocity().clone();
                    data.getPlayer().teleport(data.getLocation().clone().add(0,
                            accel - (0.01 * Integer.signum((int) Math.ceil(accel))), 0));
                    data.getPlayer().setVelocity(vel.clone().setX(0).setZ(0));
                }
            }
        } else
            data.flyThreshold -= data.flyThreshold > 0 ? 0.25f : 0;
        
    }

}