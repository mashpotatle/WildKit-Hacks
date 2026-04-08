package com.wildkits.mixin;

import com.wildkits.TicTacToeAI;
import com.wildkits.WildKitsMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    private int     lastMoveSlot = -1;
    private boolean movePending  = false;
    private long    moveTimestamp = 0;
    private long    clickAfterMs = 0;
    private String  lastBoardLog = "";
    private static final long MOVE_TIMEOUT_MS = 1000; // 1 second timeout for move confirmation

    @Inject(at = @At("HEAD"), method = "render")
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!com.wildkits.ModSettings.ticTacToeEnabled) {
            lastMoveSlot = -1; movePending = false; clickAfterMs = 0; lastBoardLog = "";
            return;
        }
        
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Don't auto-click if the mod settings menu is open
        if (client.currentScreen instanceof com.wildkits.ModSettingsScreen) {
            lastMoveSlot = -1; movePending = false; clickAfterMs = 0; lastBoardLog = "";
            return;
        }
        
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;

        Text title = screen.getTitle();
        if (title == null || !title.getString().equals("Tic Tac Toe")) {
            lastMoveSlot = -1; movePending = false; clickAfterMs = 0; lastBoardLog = "";
            return;
        }

        if (System.currentTimeMillis() < clickAfterMs) return;

        ScreenHandler handler = screen.getScreenHandler();
        int[] board = new int[9];
        int playerCount = 0, aiCount = 0;

        for (int i = 0; i < 9; i++) {
            if (i >= handler.slots.size()) return;
            Slot slot = handler.slots.get(i);
            Item item = slot.getStack().getItem();
            if (item == Items.STONE)      { board[i] = TicTacToeAI.PLAYER;    playerCount++; }
            else if (item == Items.DIRT)  { board[i] = TicTacToeAI.SERVER_AI; aiCount++; }
            else                          { board[i] = TicTacToeAI.EMPTY; }
        }

        String boardStr = Arrays.toString(board);
        if (!boardStr.equals(lastBoardLog)) {
            lastBoardLog = boardStr;
            WildKitsMod.LOGGER.info("[WildKits] TTT Board: [{},{},{}] [{},{},{}] [{},{},{}]",
                board[0],board[1],board[2],board[3],board[4],board[5],board[6],board[7],board[8]);
        }

        if (movePending) {
            long currentTime = System.currentTimeMillis();
            if (lastMoveSlot >= 0 && board[lastMoveSlot] == TicTacToeAI.PLAYER) {
                movePending = false;
            } else if (currentTime - moveTimestamp > MOVE_TIMEOUT_MS) {
                // Timeout - force clear and continue
                WildKitsMod.LOGGER.warn("[WildKits] Move timeout, clearing pending state");
                movePending = false;
            } else {
                return;
            }
        }

        int winner = TicTacToeAI.checkWinner(board);
        if (winner == TicTacToeAI.PLAYER)   { WildKitsMod.LOGGER.info("[WildKits] TTT: We WIN!"); return; }
        if (winner == TicTacToeAI.SERVER_AI) { WildKitsMod.LOGGER.info("[WildKits] TTT: Opponent wins."); return; }
        if (TicTacToeAI.isBoardFull(board))  { WildKitsMod.LOGGER.info("[WildKits] TTT: Tie!"); return; }

        // Our turn when player count == ai count
        if (playerCount > aiCount) return;

        int bestSlot = TicTacToeAI.getBestMove(board);
        if (bestSlot == -1) return;

        long delay;
        if (com.wildkits.ModSettings.ticTacToeRandomDelayEnabled) {
            // Random delay between 100ms and 3000ms
            delay = 100 + (long)(Math.random() * 2900);
        } else {
            // Default small delay: 5-100ms
            delay = 5 + (long)(Math.random() * 95);
        }
        clickAfterMs = System.currentTimeMillis() + delay;

        if (client.interactionManager != null && client.player != null) {
            client.interactionManager.clickSlot(handler.syncId, bestSlot, 0, SlotActionType.PICKUP, client.player);
            lastMoveSlot = bestSlot;
            movePending = true;
            moveTimestamp = System.currentTimeMillis();
        }
    }


}
