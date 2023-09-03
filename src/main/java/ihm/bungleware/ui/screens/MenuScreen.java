package ihm.bungleware.ui.screens;

import imgui.ImGui;

public class MenuScreen extends ImGuiScreen {
    public static final MenuScreen INSTANCE = new MenuScreen();

    @Override
    public void renderGui() {
        ImGui.showDemoWindow();
    }

    @Override
    public boolean shouldPauseGame() {
        return false;
    }
}
