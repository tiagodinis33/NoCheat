package me.sxstore.nocheat.checks.impl.movement;

import org.bukkit.GameMode;

import me.sxstore.nocheat.checks.Check;
import me.sxstore.nocheat.checks.CheckInfo;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.utils.MathUtils;
@CheckInfo(name="Fly", type="Movement")
public class Fly extends Check {
    double lastDistY;
    boolean lastOnGround;
    boolean lastLastOnGround;
    
    @Override
    public void onMove(PlayerData data) {
        if (!data.isInLiquid() && !data.isInWeb() && !data.getPlayer().getAllowFlight() && !data.getPlayer().isFlying() && data.getPlayer().getGameMode() != GameMode.CREATIVE) {
            double distY = data.getLocation().getY() - data.getLastLocation().getY();
            double lastDistY = this.lastDistY;
            this.lastDistY = distY;
            double predictedDist = ((lastDistY - 0.08D) * 0.9800000190734863D);
            boolean onGround = data.isServerOnGround();
            boolean lastOnGround = this.lastOnGround;
            this.lastOnGround = onGround;
            boolean lastLastOnGround = this.lastLastOnGround;
            this.lastLastOnGround = lastOnGround;
            if (!onGround && !lastOnGround && !lastLastOnGround && Math.abs(predictedDist) >= 0.005D) {
                if (!MathUtils.isRoughlyEqual(distY, predictedDist)) {
                    this.flag(data,predictedDist + " (diferença Y esperada) é diferente de " + distY+ " (diferença Y do player)", false);
                }
            }
        }
    }
    
}