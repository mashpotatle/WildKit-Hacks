package com.wildkits.mixin;

import com.wildkits.ModSettingsScreen;
import com.wildkits.WildKitsMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class KeyInputMixin {
    private static long lastMKeyPressTime = 0;
    private static final long KEY_DEBOUNCE_MS = 500; // Prevent spam opening
    private static boolean mKeyWasPressed = false;

    @Inject(method = "tick", at = @At("HEAD"), require = 0)
    private void onClientTick(CallbackInfo ci) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            
            if (client.getWindow() == null) {
                return;
            }
            
            long windowHandle = client.getWindow().getHandle();
            int mKeyState = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_M);
            long currentTime = System.currentTimeMillis();
            
            // Detect M key press (transition from not pressed to pressed)
            if (mKeyState == GLFW.GLFW_PRESS && !mKeyWasPressed) {
                mKeyWasPressed = true;
                
                // Debounce
                if (currentTime - lastMKeyPressTime < KEY_DEBOUNCE_MS) {
                    WildKitsMod.LOGGER.trace("[WildKits] M key press ignored (debounce)");
                    return;
                }
                
                lastMKeyPressTime = currentTime;
                
                // Don't open if already showing ModSettingsScreen
                if (client.currentScreen instanceof ModSettingsScreen) {
                    WildKitsMod.LOGGER.info("[WildKits] ModSettingsScreen already open");
                    return;
                }
                
                WildKitsMod.LOGGER.info("[WildKits] ========== M KEY PRESSED ==========");
                WildKitsMod.LOGGER.info("[WildKits] Opening ModSettingsScreen...");
                
                try {
                    // Save the previous screen before opening the mod menu
                    Screen previousScreen = client.currentScreen;
                    ModSettingsScreen screen = new ModSettingsScreen(previousScreen);
                    WildKitsMod.LOGGER.info("[WildKits] ModSettingsScreen instance created: {}", screen.getClass().getName());
                    
                    WildKitsMod.LOGGER.info("[WildKits] Calling client.setScreen()...");
                    client.setScreen(screen);
                    WildKitsMod.LOGGER.info("[WildKits] client.setScreen() completed");
                    
                    if (client.currentScreen == screen) {
                        WildKitsMod.LOGGER.info("[WildKits] ✓ Screen successfully set! Screen is now: {}", 
                            client.currentScreen.getClass().getSimpleName());
                    } else {
                        WildKitsMod.LOGGER.error("[WildKits] ✗ FAILED to set screen! Current screen is: {}", 
                            client.currentScreen != null ? client.currentScreen.getClass().getSimpleName() : "NULL");
                    }
                    
                    WildKitsMod.LOGGER.info("[WildKits] ====================================");
                } catch (Exception e) {
                    WildKitsMod.LOGGER.error("[WildKits] Exception while opening ModSettingsScreen", e);
                }
            } else if (mKeyState == GLFW.GLFW_RELEASE) {
                mKeyWasPressed = false;
            }
            
        } catch (Exception e) {
            WildKitsMod.LOGGER.error("[WildKits] Unexpected error in key input handler", e);
        }
    }
}

