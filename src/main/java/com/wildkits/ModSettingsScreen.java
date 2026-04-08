package com.wildkits;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModSettingsScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger("WildKits-UI");
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SPACING = 25;

    private Screen previousScreen;
    
    private ButtonWidget ticTacToeButton;
    private ButtonWidget ticTacToeAutoButton;
    private ButtonWidget ticTacToeRandomButton;
    private ButtonWidget particleTracerButton;
    private ButtonWidget particleBeamButton;
    private ButtonWidget searchPlotsButton;
    private ButtonWidget clearPlotsButton;
    private ButtonWidget closeButton;

    public ModSettingsScreen() {
        this(null);
    }

    public ModSettingsScreen(Screen previousScreen) {
        super(Text.literal("WildKits Settings"));
        this.previousScreen = previousScreen;
        LOGGER.info("[WildKits-UI] ModSettingsScreen constructor called with previous screen: {}", 
            previousScreen != null ? previousScreen.getClass().getSimpleName() : "null");
    }

    @Override
    protected void init() {
        LOGGER.info("[WildKits-UI] init() called - screen size: {}x{}", this.width, this.height);
        int centerX = this.width / 2;
        int startY = this.height / 2 - 80;

        LOGGER.info("[WildKits-UI] Creating buttons at centerX={}, startY={}", centerX, startY);

        int buttonY = startY;

        // TicTacToe Button
        this.ticTacToeButton = new ButtonWidget.Builder(
            Text.literal("TicTacToe: " + (ModSettings.ticTacToeEnabled ? "§aON" : "§cOFF")),
            button -> {
                LOGGER.info("[WildKits-UI] TicTacToe button clicked");
                ModSettings.toggle("tictactoe");
                this.ticTacToeButton.setMessage(Text.literal("TicTacToe: " + (ModSettings.ticTacToeEnabled ? "§aON" : "§cOFF")));
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.ticTacToeButton);
        buttonY += SPACING;

        // TicTacToe Auto Button
        this.ticTacToeAutoButton = new ButtonWidget.Builder(
            Text.literal("TTT Auto: " + (ModSettings.ticTacToeAutoClickEnabled ? "§aON" : "§cOFF")),
            button -> {
                LOGGER.info("[WildKits-UI] TicTacToe Auto button clicked");
                ModSettings.toggle("tictactoe-auto");
                this.ticTacToeAutoButton.setMessage(Text.literal("TTT Auto: " + (ModSettings.ticTacToeAutoClickEnabled ? "§aON" : "§cOFF")));
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.ticTacToeAutoButton);
        buttonY += SPACING;

        // TicTacToe Random Delay Button
        this.ticTacToeRandomButton = new ButtonWidget.Builder(
            Text.literal("TTT Random: " + (ModSettings.ticTacToeRandomDelayEnabled ? "§aON" : "§cOFF")),
            button -> {
                LOGGER.info("[WildKits-UI] TicTacToe Random button clicked");
                ModSettings.toggle("tictactoe-random");
                this.ticTacToeRandomButton.setMessage(Text.literal("TTT Random: " + (ModSettings.ticTacToeRandomDelayEnabled ? "§aON" : "§cOFF")));
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.ticTacToeRandomButton);
        buttonY += SPACING;

        // Coord Tracker Button
        this.particleTracerButton = new ButtonWidget.Builder(
            Text.literal("Particle Tracer: " + (ModSettings.particleTracerEnabled ? "§aON" : "§cOFF")),
            button -> {
                LOGGER.info("[WildKits-UI] Particle Tracer button clicked");
                ModSettings.toggle("particle-tracer");
                this.particleTracerButton.setMessage(Text.literal("Particle Tracer: " + (ModSettings.particleTracerEnabled ? "§aON" : "§cOFF")));
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.particleTracerButton);
        buttonY += SPACING;

        // Beacon Beam Button
        this.particleBeamButton = new ButtonWidget.Builder(
            Text.literal("Particle Beam: " + (ModSettings.particleBeamEnabled ? "§aON" : "§cOFF")),
            button -> {
                LOGGER.info("[WildKits-UI] Particle Beam button clicked");
                ModSettings.toggle("particle-beam");
                this.particleBeamButton.setMessage(Text.literal("Particle Beam: " + (ModSettings.particleBeamEnabled ? "§aON" : "§cOFF")));
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.particleBeamButton);
        buttonY += SPACING;

        // Search Plots Button
        this.searchPlotsButton = new ButtonWidget.Builder(
            Text.literal("Search Plots"),
            button -> {
                LOGGER.info("[WildKits-UI] Search Plots button clicked");
                SignPlotter.searchForUnownedSigns();
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.searchPlotsButton);
        buttonY += SPACING;

        // Clear Plots Button
        this.clearPlotsButton = new ButtonWidget.Builder(
            Text.literal("Clear Plots"),
            button -> {
                LOGGER.info("[WildKits-UI] Clear Plots button clicked");
                SignPlotter.clearPlot();
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.clearPlotsButton);
        buttonY += SPACING;

        // Close Button
        this.closeButton = new ButtonWidget.Builder(
            Text.literal("Close"),
            button -> {
                LOGGER.info("[WildKits-UI] Close button clicked");
                this.close();
            }
        ).dimensions(centerX - BUTTON_WIDTH / 2, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addDrawableChild(this.closeButton);
        buttonY += SPACING;

        LOGGER.info("[WildKits-UI] init() complete - all buttons initialized");
    }

    @Override
    public void close() {
        LOGGER.info("[WildKits-UI] ModSettingsScreen closing, restoring previous screen: {}", 
            previousScreen != null ? previousScreen.getClass().getSimpleName() : "null");
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(previousScreen);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        LOGGER.debug("[WildKits-UI] render() called - mouse: ({},{}), delta: {}", mouseX, mouseY, delta);
        LOGGER.debug("[WildKits-UI] Screen dimensions: {}x{}, DrawContext: {}", this.width, this.height, context);
        
        try {
            this.renderBackground(context, mouseX, mouseY, delta);
            LOGGER.debug("[WildKits-UI] Background rendered");
        } catch (Exception e) {
            LOGGER.error("[WildKits-UI] Error rendering background", e);
        }
        
        try {
            if (this.textRenderer == null) {
                LOGGER.warn("[WildKits-UI] TextRenderer is NULL!");
            } else {
                context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 100, 0xFFFFFF);
                LOGGER.debug("[WildKits-UI] Title text rendered: {}", this.title.getString());
            }
        } catch (Exception e) {
            LOGGER.error("[WildKits-UI] Error rendering title text", e);
        }
        
        try {
            super.render(context, mouseX, mouseY, delta);
            LOGGER.debug("[WildKits-UI] Super render completed (buttons rendered)");
        } catch (Exception e) {
            LOGGER.error("[WildKits-UI] Error in super.render()", e);
            throw e;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
