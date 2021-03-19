package me.sxstore.nocheat.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.joml.*;

public class MathUtils {
    
    public long GCD_OFFSET;
    public static final double EXPANDER = java.lang.Math.pow(2, 24);

    public static long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }
    public static Vector3d bukkitVectorToJomlVectord(Vector vec){
        return new Vector3d(vec.getX(),vec.getY(),vec.getZ());
    }
    public static Vector3d bukkitVectorToJomlVectord(Location vec){
        return new Vector3d(vec.getX(),vec.getY(),vec.getZ());
    }
    public static Vector3f bukkitVectorToJomlVectorf(Vector vec){
        return new Vector3f((float)vec.getX(),(float)vec.getY(),(float)vec.getZ());
    }
    public static Vector3f bukkitVectorToJomlVectorf(Location vec){
        return new Vector3f((float)vec.getX(),(float)vec.getY(),(float)vec.getZ());
    }

    public static double getStandardDeviation(long[] numberArray) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += java.lang.Math.pow(num - mean, 2);

        return java.lang.Math.sqrt(deviation / length);
    }
    public static boolean isRoughlyEqual(double var1, double var2){
        return java.lang.Math.abs(var1-var2) < 0.001;
    }   
    public static double getStandardDeviation(double[] numberArray) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += java.lang.Math.pow(num - mean, 2);

        return java.lang.Math.sqrt(deviation / length);
    }

    public static float getAngleDiff(float a, float b) {
        float diff = java.lang.Math.abs(a - b);
        float altDiff = b + 360 - a;
        float altAltDiff = a + 360 - b;
        if (altDiff < diff) diff = altDiff;
        if (altAltDiff < diff) diff = altAltDiff;
        return diff;
    }
    public static double distanceHorizontal(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        return java.lang.Math.sqrt(dx * dx + dz * dz);
    }
}
