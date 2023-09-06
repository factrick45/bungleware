package ihm.bungleware.ui.imgui;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

import ihm.bungleware.utils.Utils;

import imgui.ImFontConfig;
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

        var fc = new ImFontConfig();
        fc.setSizePixels(24);
        var fa = io.getFonts();
        fa.clear();

        byte[] ttf;
        try {
            ttf = Files.readAllBytes(Utils.getModAssets().resolve("ibm.ttf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fa.addFontFromMemoryTTF(ttf, 24.0f, fc);
        fc.destroy();

        fa.build();

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
