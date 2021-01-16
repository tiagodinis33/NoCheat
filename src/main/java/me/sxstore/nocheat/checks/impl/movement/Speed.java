package me.sxstore.nocheat.checks.impl.movement;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Step;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.sxstore.nocheat.checks.Check;
import me.sxstore.nocheat.checks.CheckInfo;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.playerdata.VelocityData;
import me.sxstore.nocheat.utils.MathUtils;
import me.sxstore.nocheat.utils.PlayerUtils;

@CheckInfo(name = "Speed", type = "Movement")
public class Speed extends Check {

    private double iceBlockMax, ice;
    private double blockMoveMax, stepMax;

    @Override
    public void onMove(PlayerData data) {
        if((data.getPlayer().getAllowFlight() && data.isLastFlying() && !data.isFlying()) && ((!data.isServerOnGround()) && (data.getPlayer().getVelocity().getY() <= 0.0D)  && (!data.isInLiquid()))){
            return;
        }
        if(data.isFlying() || data.getPlayer().getAllowFlight()){
            return;
        }
        Location from = data.getLastLocation();
        Location to = data.getLocation();

        boolean slab = to.getBlock().getRelative(BlockFace.DOWN).getType().getData().equals(Step.class);
        boolean velocityModifier = (PlayerUtils.isOnSlab(to) || slab)
                || (PlayerUtils.isOnStair(to) || PlayerUtils.isOnStairJump(to));

        boolean onGround = data.isOnGround();
        int groundTicks = data.getGroundTicks();

        double thisMove = MathUtils.distanceHorizontal(from, to);
        double baseMove = getBaseMoveSpeed(data.getPlayer());
        double vertical = Math.abs(Math.sqrt((data.getLocation().getY() - data.getLastLocation().getY())*(data.getLocation().getY() - data.getLastLocation().getY())));

        int playerPing = data.getPing();

        // cancel large movements.
        if (thisMove > 8) {
            flag(data, "Moveu-se muito rapido! vel="+thisMove, true);
            return;
        }

        // handle groundTick stuff.
        if (onGround) {
            groundTicks++;
            data.setGroundTicks(groundTicks >= 8 ? 8 : groundTicks);
        }

        if (!onGround) {
            data.setGroundTicks(0);
            groundTicks = 0;
        }

        // handle ice/slimeblock stuff.
        boolean isOnIce = PlayerUtils.isOnIce(data);
        boolean isOnSlimeblock = PlayerUtils.isOnSlime(data);

        int iceTime = data.getIceTicks();
        if (!isOnIce) {
            // update data.
            data.setIceTicks(iceTime > 0 ? iceTime - 1 : 0);
        } else {
            data.setIceTicks(8);
        }

        int slimeblockTime = data.getSlimeTicks();
        if (!isOnSlimeblock) {
            // update data.
            data.setSlimeTicks(slimeblockTime > 0 ? slimeblockTime - 1 : 0);
        } else {
            data.setSlimeTicks(8);
        }

        if (onGround) {
            // Check if we have a block above us.
            boolean hasBlock = PlayerUtils.isUnderBlock(to);

            // modify ground time.
            if (hasBlock) {
                if (vertical > 0.0) {
                    groundTicks = isOnIce ? 0 : 2;
                    data.setGroundTicks(groundTicks);
                }
            }

            // this does not account for jumping.
            double expected = thisMove / data.getGroundTicks() + baseMove;

            if (hasBlock) {
                
                if (vertical > 0.0) {
                    // check if we are on ice.
                    if (isOnIce) {
                        // we have ice and we are jumping lets adjust and check.
                        if (thisMove > iceBlockMax) {
                            flag(data, "Moveu-se muito rapido no gelo!, onground_ice_block m=" + thisMove + " e=" + iceBlockMax, false);

                        }
                    }

                    if (!isOnIce && data.getIceTicks() <= 3) {
                        // no ice and jumping.
                        if (thisMove > blockMoveMax) {
                            flag(data, "Moveu-se muito rapido!, onground_block m=" + thisMove + " e=" + blockMoveMax, true);
                        }
                    }
                }
            }

            // make sure we've actually been onGround, without block jumps, etc.
            if (groundTicks >= 5 && data.getIceTicks() == 0) {
                boolean hadModifier = from.getBlock().getRelative(BlockFace.DOWN).getType().getData()
                        .equals(Step.class);
                double stepModifier = velocityModifier ? stepMax + expected : expected;
                // no block, normal check.
                if (velocityModifier || hadModifier) {
                    if (thisMove > stepModifier) {
                        flag(data, "Moveu-se muito rapido!, onground_expected_velocity m=" + thisMove + " e=" + stepModifier, true);
                    }
                } else {
                    if (vertical > 0.41) {
                        // we jumped but we're still onGround.
                        expected += vertical / thisMove;
                    }
                    if (thisMove > expected) {
                        flag(data, "Moveu-se muito rapido!, onground_expected m=" + thisMove + " e=" + expected, true);
                    }
                }

            }

            // ice speed
            if (isOnIce && groundTicks >= 3) {
                if (thisMove > expected + 0.01) {
                    flag(data, "Moveu-se muito rapido no gelo!, onground_expected_ice m=" + thisMove + " e=" + expected, true);
                }
            }

        }

        if (!onGround) {
            // if we are on a slimeblock.
            if (isOnSlimeblock) {
                // get the jump stage
                double stage = vertical == 0.0 ? 0.1018 : vertical < 0.34 ? 0.1504 : 0.28;
                double expected = (baseMove + stage);
                if (thisMove > expected) {
                    flag(data, "Moveu-se muito rapido no ar (slime)!, offground_slime m=" + thisMove + " e=" + expected, true);
                }
            }

            if (isOnIce) {
                // check if we have been 'launched' by ice.
                if (thisMove > ice) {
                    flag(data, "Moveu-se muito rapido no ar (gelo)!, offground_ice m=" + thisMove + " e=" + ice, true);
                }

            }

            boolean hasModifier = (isOnIce || iceTime > 0) || (isOnSlimeblock || slimeblockTime > 0);
            if (!hasModifier) {
                // get the jump stage
                double stage = vertical == 0.0 ? 0.049 : vertical < 0.34 ? 0.08 : 0.3261;
                double expected = (baseMove + stage);
                VelocityData velData = data.getVelocityData();
                playerPing = Math.min(playerPing, 300); // cap the ping.

                Vector velocity = velData.getRecentVelocity(data.getPing());
                if (velocity != null) {
                    double length = velocity.lengthSquared() + stage;
                    double expLength = expected * length;
                    expected = getBaseMoveSpeed(data.getPlayer()) + expLength;
                    velData.removeVelocity(velocity);
                }
                // too fast, flag.
                if (thisMove > expected) {
                    flag(data, "Moveu-se muito rapido no ar!, offground_expected m=" + thisMove + " e=" + expected, true);
                }
            }
        }


    }

    private double getBaseMoveSpeed(Player player) {
        double baseSpeed = 0.2873;

        for (PotionEffect ef : player.getActivePotionEffects()) {
            if (ef.getType().equals(PotionEffectType.SPEED)) {
                baseSpeed *= 1.0 + 0.2 * ef.getAmplifier() + 1;
            }
        }

        return baseSpeed;
    }
}