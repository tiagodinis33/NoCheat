package me.sxstore.nocheat.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.sxstore.nocheat.playerdata.PlayerData;

public class PlayerUtils {

    public static class LocationBit {

        private double modifier;
        private double xBit, zBit;

        public LocationBit(double modifier) {
            this.modifier = modifier;
            this.xBit = modifier;
            this.zBit = modifier;
        }

        /**
         * Shift the X and Y.
         *
         * @param bound the index in an array of 5.
         */
        public void shift(int bound) {
            xBit = bound >= 2 ? -modifier : modifier;
            zBit = bound == 1 ? -modifier : bound == 2 ? modifier : bound == 3 ? -modifier : zBit;
        }

        public double getX() {
            return xBit;
        }

        public double getZ() {
            return zBit;
        }
    }

    public static boolean isOnStairJump(Location location) {
        return hasBlock(location, Stairs.class, 1);
    }

    public static boolean hasBlock(Location location, Class comparable, double vertical) {
        LocationBit bit = new LocationBit(0.5);

        // check if were already under that block.
        Location subtracted = location.clone().subtract(0, vertical, 0);
        Block subtractedBlock = subtracted.getBlock();

        if (subtractedBlock.getType().getData().equals(comparable)) {
            return true;
        }

        for (int i = 1; i <= 4; i++) {
            Location newLocation = location.clone().add(bit.getX(), -vertical, bit.getZ());
            Block block = newLocation.getBlock();
            if (block.getType().getData().equals(comparable)) {
                return true;
            }
            bit.shift(i);
        }
        return false;
    }

    public static boolean isOnStair(Location location) {
        return hasBlock(location, Stairs.class);
    }

    public static boolean hasBlock(Location location, Class comparable) {
        LocationBit bit = new LocationBit(0.3);

        // check if were already under that block.
        Location subtracted = location.clone().subtract(0, 0.3, 0);
        Block subtractedBlock = subtracted.getBlock();

        if (subtractedBlock.getType().getData().equals(comparable)) {
            return true;
        }

        for (int i = 1; i <= 4; i++) {
            Location newLocation = location.clone().add(bit.getX(), -0.3, bit.getZ());
            Block block = newLocation.getBlock();
            if (block.getType().getData().equals(comparable)) {
                return true;
            }
            bit.shift(i);
        }
        return false;
    }

    public static boolean isOnSlab(Location location) {
        LocationBit bit = new LocationBit(0.5);

        // check if were already under that block.
        Location subtracted = location.clone().subtract(0, 0.1, 0);
        Block subtractedBlock = subtracted.getBlock();

        if (subtractedBlock.getType().getData().equals(Step.class)) {
            return true;
        }

        for (int i = 1; i <= 4; i++) {
            Location newLocation = location.clone().add(bit.getX(), -0.1, bit.getZ());
            Block block = newLocation.getBlock();
            if (block.getType().getData().equals(Step.class)) {
                return true;
            }
            bit.shift(i);
        }
        return false;
    }

    // Credits to Jonhan
    public static boolean onGround(PlayerData player) {
        Location location = player.getPlayer().getLocation();
        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (!location.clone().add(x, -0.05, z).getBlock().getType().equals(Material.AIR)
                        && !location.clone().add(x, -0.05, z).getBlock().isLiquid())
                    return true;
                if (!location.clone().add(x, -0.5001, z).getBlock().getType().equals(Material.AIR)
                        && !location.clone().add(x, -0.5001, z).getBlock().isLiquid())
                    return true;
                if (isOnLilyOrCarpet(player))
                    return true;
            }
        }
        return false;
    }

    public static boolean inLiquid(PlayerData data) {
        Player player = data.getPlayer();

        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (player.getLocation().clone().add(z, 0, x).getBlock().isLiquid()) {
                    return true;
                }
                if (player.getLocation().clone().add(z, player.getEyeLocation().getY(), x).getBlock().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean blockNearHead(PlayerData data) {
        Player player = data.getPlayer();

        double expand = 0.5;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (player.getLocation().clone().add(z, 2.01, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
                if (player.getLocation().clone().add(z, 1.5001, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOnIce(PlayerData data) {
        Player p = data.getPlayer();
        if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("ICE")
                || p.getLocation().clone().add(0, -0.5, 0).getBlock().getRelative(BlockFace.DOWN).getType().toString()
                        .contains("ICE")) {
            return true;
        }
        return false;
    }

    public static boolean isUnderBlock(Location location) {
        LocationBit bit = new LocationBit(0.5);

        // check if were already under that block.
        Location added = location.clone().add(0, 2, 0);
        Block addedBlock = added.getBlock();
        if (addedBlock.getType().isSolid()) {
            return true;
        }

        for (int i = 1; i <= 4; i++) {
            Location newLocation = location.clone().add(bit.getX(), 2, bit.getZ());
            Block block = newLocation.getBlock();
            if (block.getType().isSolid()) {
                return true;
            }
            bit.shift(i);
        }
        return false;
    }

    public static boolean isOnSlime(PlayerData data) {
        Player p = data.getPlayer();
        if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().toString().contains("SLIME")
                || p.getLocation().clone().add(0, -0.5, 0).getBlock().getRelative(BlockFace.DOWN).getType().toString()
                        .contains("ICE")) {
            return true;
        }
        return false;
    }

    public static boolean isOnLilyOrCarpet(PlayerData data) {
        Location loc = data.getPlayer().getLocation();
        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (loc.clone().add(z, 0, x).getBlock().getType().toString().contains("LILY")
                        || loc.clone().add(z, -0.001, x).getBlock().getType().toString().contains("CARPET")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOnWeirdBlock(PlayerData data) {
        Location loc = data.getPlayer().getLocation();
        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (loc.clone().add(z, 0, x).getBlock().getType().toString().contains("SLIME")
                        || loc.clone().add(z, -0.001, x).getBlock().getType().toString().contains("ICE")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInWeb(PlayerData data) {
        Player player = data.getPlayer();

        double expand = 0.3;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (player.getLocation().clone().add(z, 0, x).getBlock().getType().toString().toLowerCase()
                        .contains("web")) {
                    return true;
                }
                if (player.getLocation().clone().add(z, player.getEyeLocation().getY(), x).getBlock().getType()
                        .toString().toLowerCase().contains("web")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOnClimbable(PlayerData data) {
        Player player = data.getPlayer();
        return player.getLocation().getBlock().getType() == Material.LADDER
                || player.getLocation().getBlock().getType() == Material.VINE
                || player.getLocation().clone().add(0, 1, 0).getBlock().getType() == Material.LADDER
                || player.getLocation().clone().add(0, 1, 0).getBlock().getType() == Material.VINE;
    }

    public static boolean nearWall(PlayerData data) {
        Player player = data.getPlayer();
        double expand = 0.6;
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (player.getLocation().clone().add(z, 0.1, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
                if (player.getEyeLocation().clone().add(z, 0, x).getBlock().getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getPotionEffectLevel(Player player, PotionEffectType pet) {
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (!pe.getType().getName().equals(pet.getName()))
                continue;
            return pe.getAmplifier() + 1;
        }
        return 0;
    }
}
