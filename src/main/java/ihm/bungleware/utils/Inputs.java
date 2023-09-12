package ihm.bungleware.utils;

import net.minecraft.client.MinecraftClient;

import ihm.bungleware.mixin.MinecraftClientAccessor;

public class Inputs {
    private static boolean left = false;
    private static boolean right = false;

    public static void leftClick() {
        left = true;
    }

    public static void rightClick() {
        right = true;
    }

    public static void onTickPre() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null)
            return;

        if (left) {
            ((MinecraftClientAccessor)mc).invokeAttack();
            left = false;
        }
        if (right) {
            ((MinecraftClientAccessor)mc).invokeUse();
            right = false;
        }
    }
}
