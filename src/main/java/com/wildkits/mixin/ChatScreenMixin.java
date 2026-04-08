package com.wildkits.mixin;

import com.wildkits.ModSettings;
import com.wildkits.SignPlotter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void onSendMessage(String message, boolean bl, CallbackInfo ci) {
        if (message.startsWith("!")) {
            String[] args = message.split(" ");
            
            if (args[0].equalsIgnoreCase("!wildkit")) {
                ci.cancel();
                
                if (args.length == 1) {
                    // Just !wildkit - show help
                    sendChatMessage("§6[WildKits] Commands: !wildkit tictactoe [auto], !wildkit target, !wildkit plot [clear]");
                } else if (args.length == 2) {
                    String command = args[1].toLowerCase();
                    
                    if (command.equals("tictactoe")) {
                        ModSettings.toggle("tictactoe");
                        sendChatMessage("§6[WildKits] TicTacToe: " + (ModSettings.ticTacToeEnabled ? "§aENABLED" : "§cDISABLED"));
                    } else if (command.equals("target")) {
                        ModSettings.toggle("particle-tracer");
                        sendChatMessage("§6[WildKits] Particle Tracer: " + (ModSettings.particleTracerEnabled ? "§aENABLED" : "§cDISABLED"));
                    } else if (command.equals("plot")) {
                        sendChatMessage("§6[WildKits] Searching for unowned signs...");
                        SignPlotter.searchForUnownedSigns();
                    }
                } else if (args.length == 3) {
                    String command = args[1].toLowerCase();
                    String subcommand = args[2].toLowerCase();
                    
                    if (command.equals("tictactoe") && subcommand.equals("auto")) {
                        ModSettings.toggle("tictactoe-auto");
                        sendChatMessage("§6[WildKits] TicTacToe Auto-Click: " + (ModSettings.ticTacToeAutoClickEnabled ? "§aENABLED" : "§cDISABLED"));
                    } else if (command.equals("tictactoe") && subcommand.equals("random")) {
                        ModSettings.toggle("tictactoe-random");
                        sendChatMessage("§6[WildKits] TicTacToe Random Delay (50ms-2s): " + (ModSettings.ticTacToeRandomDelayEnabled ? "§aENABLED" : "§cDISABLED"));
                    } else if (command.equals("plot") && subcommand.equals("clear")) {
                        SignPlotter.clearPlot();
                    }
                }
            }
        }
    }
    
    private void sendChatMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.of(message), false);
        }
    }
}
