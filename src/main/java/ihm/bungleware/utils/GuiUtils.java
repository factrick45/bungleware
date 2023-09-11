package ihm.bungleware.utils;

import java.awt.Color;

import imgui.ImGui;
import imgui.ImVec2;

/** Utilities used primarily for UI. */
public class GuiUtils {
    public static int rainbow(int index, float speed) {
        double time =
            (((double)System.currentTimeMillis() + index * 100) * speed) / 1000.0;
        return Color.HSBtoRGB((float)Math.sin(time), 0.7f, 1.0f);
    }

    public static void textColored(String text, Color color) {
        ImGui.textColored(
            color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
            text
        );
    }

    public static void textShadow(String text) {
        ImVec2 pos = ImGui.getCursorPos();
        ImGui.setCursorPos(pos.x + 2.0f, pos.y + 2.0f);
        ImGui.textColored(0, 0, 0, 255, text);
        ImGui.setCursorPos(pos.x, pos.y);
        ImGui.text(text);
    }

    public static void textShadow(String text, Color color) {
        ImVec2 pos = ImGui.getCursorPos();
        ImGui.setCursorPos(pos.x + 2.0f, pos.y + 2.0f);
        textColored(text, color.darker());
        ImGui.setCursorPos(pos.x, pos.y);
        textColored(text, color);
    }

    public static void textShadow(String text, int color) {
        textShadow(text, new Color(color));
    }
}
