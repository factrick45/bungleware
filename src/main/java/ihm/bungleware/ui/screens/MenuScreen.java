package ihm.bungleware.ui.screens;

import ihm.bungleware.Bungleware;
import ihm.bungleware.module.Category;
import ihm.bungleware.module.Module;
import ihm.bungleware.module.Modules;

import imgui.ImGui;
import imgui.flag.ImGuiCond;

public class MenuScreen extends ImGuiScreen {
    public static final MenuScreen INSTANCE = new MenuScreen();

    private Module settingsModule = null;

    private void categoryWindow(Category cat, int x, int y) {
        ImGui.setNextWindowPos(x, y, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSize(150.0f, 0.0f);
        if (!ImGui.begin(cat.getName(), 0)) {
            ImGui.end();
            return;
        }
        for (var mod : cat) {
            if (ImGui.selectable(mod.getName(), mod.isEnabled())) {
                mod.toggle();
                Bungleware.instance().save();
            }
            if (ImGui.isItemHovered())
                ImGui.setTooltip(mod.getDesc());
            if (ImGui.isItemHovered() && ImGui.isMouseClicked(1))
                settingsModule = mod;
        }
        ImGui.end();
    }

    private void settingsWindow(int x, int y) {
        ImGui.setNextWindowPos(x, y, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSize(250.0f, 0.0f);
        if (!ImGui.begin("Settings", 0)) {
            ImGui.end();
            return;
        }

        var mod = settingsModule;
        if (mod == null) {
            ImGui.text("None");
            ImGui.end();
            return;
        }
        ImGui.text(mod.getName());
        for (var set : mod.getSettings()) {
            set.render();
            if (ImGui.isItemHovered() && set.getDesc() != null)
                ImGui.setTooltip(set.getDesc());
        }

        ImGui.end();
    }

    @Override
    public void renderGui() {
        // ImGui.showDemoWindow();
        int xoff = 10;
        for (var cat : Modules.getCategories()) {
            categoryWindow(cat, xoff, 10);
            xoff += 160;
        }
        settingsWindow(xoff, 10);
    }

    @Override
    public boolean shouldPauseGame() {
        return false;
    }
}
