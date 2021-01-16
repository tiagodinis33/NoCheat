package me.sxstore.nocheat.utils;

import org.bukkit.Location;

public class MathUtils {

    public long GCD_OFFSET;
    public static final double EXPANDER = Math.pow(2, 24);

    public static long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }


    public static double getStandardDeviation(long[] numberArray) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }
    public static boolean isRoughlyEqual(double var1, double var2){
        return Math.abs(var1-var2) < 0.001;
    }   
    public static double getStandardDeviation(double[] numberArray) {
        double sum = 0.0, deviation = 0.0;
        int length = numberArray.length;
        for (double num : numberArray)
            sum += num;
        double mean = sum / length;
        for (double num : numberArray)
            deviation += Math.pow(num - mean, 2);

        return Math.sqrt(deviation / length);
    }

    public static float getAngleDiff(float a, float b) {
        float diff = Math.abs(a - b);
        float altDiff = b + 360 - a;
        float altAltDiff = a + 360 - b;
        if (altDiff < diff) diff = altDiff;
        if (altAltDiff < diff) diff = altAltDiff;
        return diff;
    }
    public static double distanceHorizontal(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }
}
