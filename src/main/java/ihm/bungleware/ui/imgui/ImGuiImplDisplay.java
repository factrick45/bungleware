package ihm.bungleware.ui.imgui;

import imgui.ImGui;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiMouseButton;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/** Implementation of the ImGui platform for lwjgl2. */
public final class ImGuiImplDisplay {
    private boolean mouseButtons[] = new boolean[ImGuiMouseButton.COUNT];
    private long time = 0;

    public void init() {
        var io = ImGui.getIO();
        io.setBackendPlatformName("bungleware_lwjgl2_display");
    }

    public void newFrame() {
        var io = ImGui.getIO();

        // set display size
        float ww = (float)Display.getWidth();
        float wh = (float)Display.getHeight();
        io.setDisplaySize(ww, wh);
        io.setDisplayFramebufferScale(1, 1);

        // set delta
        long nutime = System.currentTimeMillis();
        float delta =
            time > 0 ? (float)(((double)nutime - time) / 1000.0) : 1.0f / 60;
        // prevent failed assert for delta > 0.0f
        io.setDeltaTime((delta > 0.0f) ? delta : 0.01f);
        time = nutime;

        // mouse input
        io.setMousePos((float)Mouse.getX(), wh - (float)Mouse.getY());
        for (int i = 0; i < mouseButtons.length; i++) {
            io.setMouseDown(i, mouseButtons[i] || Mouse.isButtonDown(i));
            mouseButtons[i] = false;
        }
    }

    public void onMouseButton(int button, boolean pressed) {
        if (pressed && button > 0 && button < mouseButtons.length) {
            mouseButtons[button] = true;
        }
    }

    public void onMouseWheel(int scrolldelta) {
        var io = ImGui.getIO();
        io.setMouseWheel(io.getMouseWheel() + (float)(scrolldelta / 120));
    }
}
