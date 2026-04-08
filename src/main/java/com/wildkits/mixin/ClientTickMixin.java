package com.wildkits.mixin;

import com.wildkits.ModSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ClientTickMixin {

    private long lastClickTime = 0;

    @Inject(method = "tick", at = @At("HEAD"), require = 0)
    private void onClientTick(CallbackInfo ci) {
        if (!ModSettings.ticTacToeAutoClickEnabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        
        // Don't auto-click if mod settings menu is open
        if (client.currentScreen instanceof com.wildkits.ModSettingsScreen) {
            return;
        }
        
        // Don't auto-click if in a menu screen
        if (client.currentScreen instanceof HandledScreen) {
            return;
        }
        
        if (client.world == null || client.player == null) {
            return;
        }

        // Calculate click delay
        long clickDelay;
        if (ModSettings.ticTacToeRandomDelayEnabled) {
            // Random delay between 1 second and 3 seconds
            clickDelay = 1000 + (long)(Math.random() * 2000);
        } else {
            // Default 1 second delay
            clickDelay = 1000;
        }

        // Rate limit clicks
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < clickDelay) {
            return;
        }

        // Find nearest minecraft:interaction entity
        Entity nearestEntity = null;
        double nearestDistance = 6.0; // Default reach distance

        for (Entity entity : client.world.getEntities()) {
            Identifier entityId = Registries.ENTITY_TYPE.getId(entity.getType());
            if (entityId != null && entityId.toString().equals("minecraft:interaction")) {
                double distance = client.player.distanceTo(entity);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestEntity = entity;
                }
            }
        }

        // Click on the nearest interaction entity
        if (nearestEntity != null) {
            client.interactionManager.interactEntity(client.player, nearestEntity, net.minecraft.util.Hand.MAIN_HAND);
            lastClickTime = currentTime;
        }
    }
}
