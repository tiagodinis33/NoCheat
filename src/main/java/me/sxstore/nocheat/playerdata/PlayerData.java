package me.sxstore.nocheat.playerdata;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import lombok.Getter;
import lombok.Setter;
import me.sxstore.nocheat.checks.Check;
import me.sxstore.nocheat.checks.CheckManager;
import me.sxstore.nocheat.utils.LogUtils;
import me.sxstore.nocheat.utils.PlayerUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerData {
    public VelocityData getVelocityData() {
        return velocityData;
    }
    private VelocityData velocityData = new VelocityData();
    private Player player;
    private Location location, lastLocation, lastOnGroundLocation, lastLegitLocation;
    private Vector lastVel, direction;
    private double deltaXZ, deltaY, lastDeltaXZ, lastDeltaY;
    private float deltaYaw, deltaPitch, lastDeltaPitch, lastDeltaYaw, yaw, pitch;
    private short velocityID;
    private int ticks, airTicks, velTick, maxVelTicks, iceTicks, legitTick, slimeTicks, liquidTicks, underBlockTicks, sprintingTicks, teleportTicks, groundTicks, totalFlags, cps;
    private long lastSetBack = System.nanoTime() / 1000000, lastShoot;
    private boolean isSprinting, isSneaking, onGround, serverOnGround, alerts, verifyingVelocity, digging,
    isFlying, lastIsFlying;
    private List<Check> checks;
    public boolean isFlying() {
        return isFlying;
    }
    public boolean isLastFlying() {
        return lastIsFlying;
    }
    public void setLastIsFlying(boolean lastIsFlying) {
        this.lastIsFlying = lastIsFlying;
    }
    public void setFlying(boolean isFlying) {
        this.isFlying = isFlying;
    }
    private Player lastAttackedPlayer;
    private ExecutorService executorService;
    private LogUtils.TextFile logFile;
    public boolean isAlerts() {
        return alerts;
    }
    public boolean isDigging() {
        return digging;
    }
    public boolean isOnGround() {
        return onGround;
    }
    public boolean isServerOnGround() {
        return serverOnGround;
    }
    public boolean isSneaking() {
        return isSneaking;
    }
    public boolean isSprinting() {
        return isSprinting;
    }
    public boolean isVerifyingVelocity() {
        return verifyingVelocity;
    }
    public int getCps() {
        return cps;
    }
    public int getAirTicks() {
        return airTicks;
    }
    public List<Check> getChecks() {
        return checks;
    }
    public float getDeltaPitch() {
        return deltaPitch;
    }
    
    public double getDeltaXZ() {
        return deltaXZ;
    }
    public double getDeltaY() {
        return deltaY;
    }
    public float getDeltaYaw() {
        return deltaYaw;
    }
    public Vector getDirection() {
        return direction;
    }
    public ExecutorService getExecutorService() {
        return executorService;
    }
    public int getGroundTicks() {
        return groundTicks;
    }
    public int getIceTicks() {
        return iceTicks;
    }
    public Player getLastAttackedPlayer() {
        return lastAttackedPlayer;
    }
    public float getLastDeltaPitch() {
        return lastDeltaPitch;
    }
    public double getLastDeltaXZ() {
        return lastDeltaXZ;
    }
    public double getLastDeltaY() {
        return lastDeltaY;
    }
    public float getLastDeltaYaw() {
        return lastDeltaYaw;
    }
    public Location getLastLegitLocation() {
        return lastLegitLocation;
    }
    public Location getLastLocation() {
        return lastLocation;
    }
    public Location getLastOnGroundLocation() {
        return lastOnGroundLocation;
    }
    public long getLastSetBack() {
        return lastSetBack;
    }
    public long getLastShoot() {
        return lastShoot;
    }
    public Vector getLastVel() {
        return lastVel;
    }
    public int getLegitTick() {
        return legitTick;
    }
    public int getLiquidTicks() {
        return liquidTicks;
    }
    public Location getLocation() {
        return location;
    }
    public LogUtils.TextFile getLogFile() {
        return logFile;
    }
    public int getMaxVelTicks() {
        return maxVelTicks;
    }
    public float getPitch() {
        return pitch;
    }
    public Player getPlayer() {
        return player;
    }
    public int getSlimeTicks() {
        return slimeTicks;
    }
    public int getSprintingTicks() {
        return sprintingTicks;
    }
    public int getTeleportTicks() {
        return teleportTicks;
    }
    public int getTicks() {
        return ticks;
    }
    public int getTotalFlags() {
        return totalFlags;
    }
    public int getUnderBlockTicks() {
        return underBlockTicks;
    }
    public int getVelTick() {
        return velTick;
    }
    public short getVelocityID() {
        return velocityID;
    }
    public float getYaw() {
        return yaw;
    }
    public void setAirTicks(int airTicks) {
        this.airTicks = airTicks;
    }
    public void setAlerts(boolean alerts) {
        this.alerts = alerts;
    }
    public void setChecks(List<Check> checks) {
        this.checks = checks;
    }
    public void setCps(int cps) {
        this.cps = cps;
    }
    public void setDeltaPitch(float deltaPitch) {
        this.deltaPitch = deltaPitch;
    }
    public void setDeltaXZ(double deltaXZ) {
        this.deltaXZ = deltaXZ;
    }
    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }
    public void setDeltaYaw(float deltaYaw) {
        this.deltaYaw = deltaYaw;
    }
    public void setDigging(boolean digging) {
        this.digging = digging;
    }
    public void setDirection(Vector direction) {
        this.direction = direction;
    }
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
    public void setGroundTicks(int groundTicks) {
        this.groundTicks = groundTicks;
    }
    public void setIceTicks(int iceTicks) {
        this.iceTicks = iceTicks;
    }
    public void setLastAttackedPlayer(Player lastAttackedPlayer) {
        this.lastAttackedPlayer = lastAttackedPlayer;
    }
    public void setLastDeltaPitch(float lastDeltaPitch) {
        this.lastDeltaPitch = lastDeltaPitch;
    }
    public void setLastDeltaXZ(double lastDeltaXZ) {
        this.lastDeltaXZ = lastDeltaXZ;
    }
    public void setLastDeltaY(double lastDeltaY) {
        this.lastDeltaY = lastDeltaY;
    }
    public void setLastDeltaYaw(float lastDeltaYaw) {
        this.lastDeltaYaw = lastDeltaYaw;
    }
    public void setLastLegitLocation(Location lastLegitLocation) {
        this.lastLegitLocation = lastLegitLocation;
    }
    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }
    public void setLastOnGroundLocation(Location lastOnGroundLocation) {
        this.lastOnGroundLocation = lastOnGroundLocation;
    }
    public void setLastSetBack(long lastSetBack) {
        this.lastSetBack = lastSetBack;
    }
    public void setLastShoot(long lastShoot) {
        this.lastShoot = lastShoot;
    }
    public void setLastVel(Vector lastVel) {
        this.lastVel = lastVel;
    }
    public void setLegitTick(int legitTick) {
        this.legitTick = legitTick;
    }
    public void setLiquidTicks(int liquidTicks) {
        this.liquidTicks = liquidTicks;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public void setLogFile(LogUtils.TextFile logFile) {
        this.logFile = logFile;
    }
    public void setMaxVelTicks(int maxVelTicks) {
        this.maxVelTicks = maxVelTicks;
    }
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setServerOnGround(boolean serverOnGround) {
        this.serverOnGround = serverOnGround;
    }
    public void setSlimeTicks(int slimeTicks) {
        this.slimeTicks = slimeTicks;
    }
    public void setSneaking(boolean isSneaking) {
        this.isSneaking = isSneaking;
    }
    public void setSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }
    public void setSprintingTicks(int sprintingTicks) {
        this.sprintingTicks = sprintingTicks;
    }
    public void setTeleportTicks(int teleportTicks) {
        this.teleportTicks = teleportTicks;
    }
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
    public void setTotalFlags(int totalFlags) {
        this.totalFlags = totalFlags;
    }
    public void setUnderBlockTicks(int underBlockTicks) {
        this.underBlockTicks = underBlockTicks;
    }
    public void setVelTick(int velTick) {
        this.velTick = velTick;
    }
    public void setVelocityID(short velocityID) {
        this.velocityID = velocityID;
    }
    public void setVerifyingVelocity(boolean verifyingVelocity) {
        this.verifyingVelocity = verifyingVelocity;
    }
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public PlayerData(UUID uuid){
        this.player = Bukkit.getPlayer(uuid);
        this.checks = CheckManager.loadChecks();
        executorService = Executors.newSingleThreadExecutor();
        logFile = new LogUtils.TextFile("" + uuid, "\\\\logs");
    }

    public boolean isTakingVelocity() { return velocityTicks() < maxVelTicks; }

    public int getPing() { return PacketEvents.getAPI().getPlayerUtils().getPing(getPlayer()); }

    public boolean isOnClimbableBlock() { return PlayerUtils.isOnClimbable(this); }

    public boolean isInLiquid() { return PlayerUtils.inLiquid(this); }

    public boolean isInWeb() { return PlayerUtils.isInWeb(this); }

    public boolean isUnderBlock() { return PlayerUtils.blockNearHead(this); }

    public boolean isNearWall() { return PlayerUtils.nearWall(this); }

    public int velocityTicks() { return Math.abs(ticks - velTick); }

    public int teleportTicks() { return Math.abs(ticks - teleportTicks); }

    public int iceTicks() { return Math.abs(ticks - iceTicks); }

    public int slimeTicks() { return Math.abs(ticks - slimeTicks); }

    public int underBlockTicks() { return Math.abs(ticks - underBlockTicks); }

    public int liquidTicks() { return Math.abs(ticks - liquidTicks); }


    /*
    PacketShit
     */

    public void inbound(PacketReceiveEvent event){
        executorService.execute(() -> checks.forEach(check -> check.onPacketReceive(event, this)));
        if (event.getPacketId() == PacketType.Client.USE_ENTITY)onAttack(new WrappedPacketInUseEntity(event.getNMSPacket()));
        if (event.getPacketId() == PacketType.Client.POSITION || event.getPacketId() == PacketType.Client.POSITION_LOOK || event.getPacketId() == PacketType.Client.LOOK)onMove();
    }

    public void outgoing(PacketSendEvent event){
        executorService.execute(() -> checks.forEach(check -> check.onPacketSend(event, this)));
    }

    public void onAttack(WrappedPacketInUseEntity packet){
        executorService.execute(() -> checks.forEach(check -> check.onAttack(packet, this)));
    }

    public void onMove(){
        executorService.execute(() -> checks.forEach(check -> check.onMove(this)));
    }
}
