package ihm.bungleware.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.LiteralText;

public class Chat {
    public static enum Type { INFO, WARN, ERROR }

    private static final String LOGO =
        "\u00a78[\u00a73Bungleware\u00a78]\u00a7r ";
    private static final String[] PREFIX = {
        LOGO,
        LOGO + "[\u00a76WARNING\u00a7r] ",
        LOGO + "[\u00a7cERROR\u00a7r] ",
    };

    public static void display(String message, Type type) {
        ChatHud ch = MinecraftClient.getInstance().inGameHud.getChatHud();
        ch.addMessage(new LiteralText(PREFIX[type.ordinal()] + message));
    }

    public static void display(String message) {
        display(message, Type.INFO);
    }
}
