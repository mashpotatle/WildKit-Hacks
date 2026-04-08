package com.wildkits.mixin;

import com.wildkits.CoordTracker;
import com.wildkits.WildKitsMod;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    private static final Pattern COORD_PATTERN =
            Pattern.compile("Coords:\\s*(-?\\d+(?:\\.\\d+)?),(-?\\d+(?:\\.\\d+)?),(-?\\d+(?:\\.\\d+)?)\\.");

    private static final Pattern TARGET_PATTERN =
            Pattern.compile("Target:\\s*([A-Za-z0-9_]{3,16})");

    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
    private void onSetOverlayMessage(Text message, boolean tinted, CallbackInfo ci) {
        if (message == null) {
            CoordTracker.clearCoords();
            return;
        }

        String text = message.getString();
        if (text == null || text.isBlank()) {
            CoordTracker.clearCoords();
            return;
        }

        boolean foundCoords = false;
        boolean foundTarget = false;

        Matcher coordMatcher = COORD_PATTERN.matcher(text);
        if (coordMatcher.find()) {
            try {
                CoordTracker.setCoords(
                        Double.parseDouble(coordMatcher.group(1)),
                        Double.parseDouble(coordMatcher.group(2)),
                        Double.parseDouble(coordMatcher.group(3))
                );
                foundCoords = true;
            } catch (NumberFormatException e) {
                WildKitsMod.LOGGER.warn("[WildKits] Failed to parse coords: {}", text);
            }
        }

        Matcher targetMatcher = TARGET_PATTERN.matcher(text);
        if (targetMatcher.find()) {
            CoordTracker.setTargetUsername(targetMatcher.group(1));
            foundTarget = true;
        }

        if (!foundCoords && !foundTarget) {
            CoordTracker.clearCoords();
        }
    }
}