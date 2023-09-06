package ihm.bungleware.utils;

import java.nio.file.Path;

import net.minecraft.client.MinecraftClient;

import net.fabricmc.loader.api.FabricLoader;

public class Utils {
    public static void breakingBad(boolean die) {
        if (die)
            throw new RuntimeException("It's over.");
    }

    public static Path getModAssets() {
        return FabricLoader.getInstance()
            .getModContainer("bungleware")
            .get()
            .getRootPaths()
            .get(0)
            .resolve("assets/bungleware/");
    }

    public static String getModVersion() {
        return FabricLoader.getInstance()
            .getModContainer("bungleware")
            .get()
            .getMetadata()
            .getVersion()
            .getFriendlyString();
    }

    public static boolean isInGame() {
        var mc = MinecraftClient.getInstance();
        return mc != null && mc.world != null && mc.player != null;
    }
}
