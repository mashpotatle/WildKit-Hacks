package com.wildkits;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildKitsMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("WildKits");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[WildKits] Wild Kits Legit loaded!");
        LOGGER.info("[WildKits] ============================================");
        LOGGER.info("[WildKits] Mod Initialization Started");
        LOGGER.info("[WildKits] Version: 1.0.0");
        LOGGER.info("[WildKits] Minecraft Version: 1.21.1");
        LOGGER.info("[WildKits] ============================================");
        
        // Register beacon beam feature
        BeaconBeamFeature.register();
        LOGGER.info("[WildKits] BeaconBeam feature registered");
        
        // Register screen event listeners for debugging
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            LOGGER.info("[WildKits-DEBUG] Screen opened: {} ({}x{})", 
                screen.getClass().getName(), scaledWidth, scaledHeight);
            if (screen instanceof ModSettingsScreen) {
                LOGGER.info("[WildKits-DEBUG] !!! ModSettingsScreen is now active !!!");
            }
        });
        
        LOGGER.info("[WildKits] Initialization complete!");
        LOGGER.info("[WildKits] Press 'M' key to open settings (check logs for debug info)");
    }
}