package me.sxstore.nocheat.checks.impl.combat;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.sxstore.nocheat.checks.Check;
import me.sxstore.nocheat.checks.CheckInfo;
import me.sxstore.nocheat.playerdata.PlayerData;
import me.sxstore.nocheat.utils.MathUtils;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;

import org.joml.*;
import static java.lang.Math.*;

import java.text.NumberFormat;
@CheckInfo(name = "KillAura", type = "A")
public class Killaura extends Check {
    
    @Override
    public void onAttack(EntityDamageByEntityEvent e, PlayerData data) {
        Matrix4d matrix = getMatrixS(MathUtils.bukkitVectorToJomlVectord(data.getLocation()), new Vector3d(data.getPitch(),data.getYaw(), 0));
        Vector3d direction = matrix.positiveZ(new Vector3d()).negate();
        direction.z = -direction.z;
        Rayd ray = new Rayd(
                direction,
                MathUtils.bukkitVectorToJomlVectord(data.getPlayer().getEyeLocation()));
        AxisAlignedBB aabbMinecraft = ((CraftEntity) e.getEntity()).getHandle().getBoundingBox();
        AABBd aabb = new AABBd( aabbMinecraft.a,  aabbMinecraft.b,  aabbMinecraft.c,
                (float) aabbMinecraft.d, aabbMinecraft.e, aabbMinecraft.f);
        debug(aabb.toString(NumberFormat.getNumberInstance()));
        double reach;
        Vector2d nearFar = new Vector2d();
        debug(Intersectiond.intersectRayAab(ray, aabb, nearFar));
        reach = nearFar.x;
        debug(reach);
        debug(direction);
        if(reach > (data.getPlayer().getGameMode() == GameMode.CREATIVE? 5:3)){
            flag(data, "Hitou de "+reach+" blocos", false);
        }
    }
    
    public static Matrix4d getMatrixS(Vector3d position,Vector3d rotation) {
        Matrix4d matrixRot;
        Matrix4d matrixRotX = new Matrix4d().rotateX( toRadians(-rotation.x));
        Matrix4d matrixRotY = new Matrix4d().rotateY( toRadians(-rotation.y));
        Matrix4d matrixRotZ = new Matrix4d().rotateZ( toRadians(-rotation.z));
        matrixRot = matrixRotX.mul(matrixRotY).mul(matrixRotZ);
	    return matrixRot.mul(new Matrix4d().translate(position.negate(new Vector3d())));
    }
}