package com.wildkits;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

public class BeaconBeamFeature {

    public static BlockPos targetPos = null;
    private static int tickCounter = 0;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(BeaconBeamFeature::tick);
    }

    public static void tick(MinecraftClient client) {
        if (client == null || client.world == null || client.player == null) return;
        
        // Only render if any particle effects are enabled
        boolean hasParticles = ModSettings.particleTracerEnabled || ModSettings.particleBeamEnabled;
        if (!hasParticles && !SignPlotter.isPlotting) return;

        // Sync coords from tracker
        if (CoordTracker.hasCoords) {
            targetPos = new BlockPos(
                (int) CoordTracker.beaconX,
                (int) CoordTracker.beaconY,
                (int) CoordTracker.beaconZ
            );
        } else {
            targetPos = null;
        }

        ClientWorld world = client.world;

        // Reduce spam - only update every 2 ticks
        tickCounter++;
        if (tickCounter % 2 != 0) return;
        tickCounter = 0;

        // Draw particles to tracked target
        if (hasParticles && targetPos != null) {
            drawParticlesTo(client, world, targetPos);
        }

        // Draw particles to unowned signs
        if (SignPlotter.isPlotting) {
            for (BlockPos signPos : SignPlotter.unownedSigns) {
                drawParticlesTo(client, world, signPos);
            }
        }
    }

    private static void drawParticlesTo(MinecraftClient client, ClientWorld world, BlockPos targetPos) {
        // Distance check (performance) - increased for visibility
        if (client.player.squaredDistanceTo(
            targetPos.getX(),
            targetPos.getY(),
            targetPos.getZ()
        ) > 2000 * 2000) return;

        double targetX = targetPos.getX() + 0.5;
        double targetZ = targetPos.getZ() + 0.5;
        
        // Trail from above player's head to beacon (particle tracer)
        if (ModSettings.particleTracerEnabled) {
            double playerX = client.player.getX();
            double playerY = client.player.getY() + 2.6; // Block above head
            double playerZ = client.player.getZ();
            
            int trailSteps = 20;
            for (int i = 0; i <= trailSteps; i++) {
                double t = (double) i / trailSteps;
                double x = playerX + (targetX - playerX) * t;
                double y = playerY + (targetPos.getY() - playerY) * t;
                double z = playerZ + (targetZ - playerZ) * t;
                
                // Particles stay in place with no velocity
                world.addParticle(
                    ParticleTypes.GLOW,
                    x, y, z,
                    0, 0, 0
                );
            }
        }

        // Main beam extending upward from beacon (particle beam)
        if (ModSettings.particleBeamEnabled) {
            for (double y = 0; y < 20; y += 0.5) {
                // Glow particle effect
                world.addParticle(
                    ParticleTypes.GLOW,
                    targetX,
                    targetPos.getY() + y,
                    targetZ,
                    0, 0, 0
                );
            }
        }
    }
}