package com.wildkits;

public class ModSettings {
    public static boolean ticTacToeEnabled = false;
    public static boolean particleTracerEnabled = false;
    public static boolean particleBeamEnabled = false;
    public static boolean ticTacToeAutoClickEnabled = false;
    public static boolean ticTacToeRandomDelayEnabled = false;

    public static void toggle(String feature) {
        if (feature.equals("tictactoe")) {
            ticTacToeEnabled = !ticTacToeEnabled;
            WildKitsMod.LOGGER.info("[WildKits] TicTacToe: {}", ticTacToeEnabled ? "ENABLED" : "DISABLED");
        } else if (feature.equals("particle-tracer")) {
            particleTracerEnabled = !particleTracerEnabled;
            WildKitsMod.LOGGER.info("[WildKits] Particle Tracer: {}", particleTracerEnabled ? "ENABLED" : "DISABLED");
        } else if (feature.equals("particle-beam")) {
            particleBeamEnabled = !particleBeamEnabled;
            WildKitsMod.LOGGER.info("[WildKits] Particle Beam: {}", particleBeamEnabled ? "ENABLED" : "DISABLED");
        } else if (feature.equals("tictactoe-auto")) {
            ticTacToeAutoClickEnabled = !ticTacToeAutoClickEnabled;
            WildKitsMod.LOGGER.info("[WildKits] TicTacToe Auto-Click: {}", ticTacToeAutoClickEnabled ? "ENABLED" : "DISABLED");
        } else if (feature.equals("tictactoe-random")) {
            ticTacToeRandomDelayEnabled = !ticTacToeRandomDelayEnabled;
            WildKitsMod.LOGGER.info("[WildKits] TicTacToe Random Delay: {}", ticTacToeRandomDelayEnabled ? "ENABLED" : "DISABLED");
        }
    }

    public static void saveSettings() {
        // Settings are saved automatically by Cloth Config at runtime
        WildKitsMod.LOGGER.info("[WildKits] Settings saved");
    }
}
