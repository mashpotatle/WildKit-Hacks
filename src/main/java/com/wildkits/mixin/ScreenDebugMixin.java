package com.wildkits.mixin;

import com.wildkits.WildKitsMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenDebugMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private void onScreenInit(CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;
        WildKitsMod.LOGGER.info("[WildKits-DEBUG] Screen.init() called on: {} (size: {}x{})", 
            screen.getClass().getSimpleName(), screen.width, screen.height);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onScreenRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;
        // Only log ModSettingsScreen renders to reduce spam
        if ("ModSettingsScreen".equals(screen.getClass().getSimpleName())) {
            WildKitsMod.LOGGER.debug("[WildKits-DEBUG] Screen.render() called on ModSettingsScreen (mouse: {},{}, delta: {})", 
                mouseX, mouseY, delta);
        }
    }
}
