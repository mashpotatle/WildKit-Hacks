package com.wildkits;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class SignPlotter {
    public static List<BlockPos> unownedSigns = new ArrayList<>();
    public static boolean isPlotting = false;

    public static void searchForUnownedSigns() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) {
            return;
        }

        unownedSigns.clear();

        // Get the player's position to determine search radius
        int playerChunkX = (int) client.player.getX() >> 4;
        int playerChunkZ = (int) client.player.getZ() >> 4;

        // Search in a 16 chunk radius (256 blocks)
        int searchRadius = 16;

        for (int cx = playerChunkX - searchRadius; cx <= playerChunkX + searchRadius; cx++) {
            for (int cz = playerChunkZ - searchRadius; cz <= playerChunkZ + searchRadius; cz++) {
                WorldChunk chunk = client.world.getChunk(cx, cz);
                if (chunk == null) {
                    continue;
                }

                // Iterate through all block entities in the chunk
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (be instanceof SignBlockEntity) {
                        SignBlockEntity sign = (SignBlockEntity) be;
                        
                        // Check the front text of the sign for BOTH "Unowned" AND "Click to Buy"
                        // getText(true) returns the front side's SignText
                        var signText = sign.getText(true);
                        Text[] messages = signText.getMessages(false);
                        
                        boolean hasUnowned = false;
                        boolean hasClickToBuy = false;
                        
                        for (Text lineText : messages) {
                            String lineString = lineText.getString();
                            if (lineString.contains("Unowned")) {
                                hasUnowned = true;
                            }
                            if (lineString.contains("Click to buy")) {
                                hasClickToBuy = true;
                            }
                        }
                        
                        // Add sign only if it has both texts
                        if (hasUnowned && hasClickToBuy) {
                            unownedSigns.add(be.getPos());
                        }
                    }
                }
            }
        }

        isPlotting = true;

        // Send chat message with results
        if (unownedSigns.size() > 0) {
            client.player.sendMessage(
                Text.of("§6[WildKits] Found §a" + unownedSigns.size() + "§6 shop signs (with Unowned + Click to Buy). Plotting beams..."),
                false
            );
        } else {
            client.player.sendMessage(
                Text.of("§6[WildKits] §cNo shop signs found nearby."),
                false
            );
            isPlotting = false;
        }
    }

    public static void clearPlot() {
        if (isPlotting) {
            unownedSigns.clear();
            isPlotting = false;
            
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.sendMessage(
                    Text.of("§6[WildKits] Plot cleared."),
                    false
                );
            }
        }
    }
}
