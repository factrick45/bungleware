package ihm.bungleware.utils;

import net.minecraft.client.MinecraftClient;

public class Utils {
    public static void breakingBad(boolean die) {
        if (die)
            throw new RuntimeException("It's over.");
    }

    public static boolean isInGame() {
        var mc = MinecraftClient.getInstance();
        return mc != null && mc.world != null && mc.player != null;
    }
}
