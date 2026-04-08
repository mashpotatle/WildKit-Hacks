package com.wildkits;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public class CoordTracker {
    public static boolean hasCoords = false;
    public static double beaconX = 0;
    public static double beaconY = 0;
    public static double beaconZ = 0;
    public static String targetUsername = null;

    public static AbstractClientPlayerEntity glowingEntity = null;

    public static void setCoords(double x, double y, double z) {
        beaconX = x;
        beaconY = y;
        beaconZ = z;
        hasCoords = true;

        WildKitsMod.LOGGER.info("[WildKits] Target coords: {}, {}, {}", x, y, z);
    }

    public static void setTargetUsername(String username) {
        if (!username.equals(targetUsername)) {
            WildKitsMod.LOGGER.info("[WildKits] Target player: {}", username);
        }
        targetUsername = username;
    }

    public static void clearCoords() {
        if (hasCoords) {
            WildKitsMod.LOGGER.info("[WildKits] Target cleared.");
        }

        hasCoords = false;
        targetUsername = null;

        if (glowingEntity != null) {
            glowingEntity.setGlowing(false);
            glowingEntity = null;
        }
    }
}