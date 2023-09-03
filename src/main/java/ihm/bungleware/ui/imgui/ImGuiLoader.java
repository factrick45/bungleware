package ihm.bungleware.ui.imgui;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.client.MinecraftClient;

import imgui.ImGui;
import imgui.ImGuiIO;

/** Manages ImGui state and calls upon registered clients when rendering. */
public class ImGuiLoader {
    private static boolean initialized = false;

    private static final ImGuiImplDisplay imdisplay = new ImGuiImplDisplay();
    private static final ImGuiImplGl2 imgl2 = new ImGuiImplGl2();

    private static List<ImGuiClient> clients = new ArrayList<>();

    public static void init() {
        if (initialized)
            return;
        initialized = true;

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);

        imdisplay.init();
        imgl2.init();
    }

    public static void render() {
        if (!initialized)
            return;
        imgl2.newFrame();
        imdisplay.newFrame();
        ImGui.newFrame();

        for (ImGuiClient client : clients) {
            if (client.guiEnabled())
                client.renderGui();
        }

        ImGui.render();
        imgl2.renderDrawData(ImGui.getDrawData());
    }

    public static void register(ImGuiClient client) {
        clients.add(client);
    }

    public static void onMouseButton(int button, boolean pressed) {
        if (!initialized)
            return;
        imdisplay.onMouseButton(button, pressed);
    }

    public static void onMouseWheel(int scrolldelta) {
        imdisplay.onMouseWheel(scrolldelta);
    }
}
