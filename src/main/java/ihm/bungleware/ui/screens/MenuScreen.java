package ihm.bungleware.ui.screens;

import ihm.bungleware.Bungleware;
import ihm.bungleware.module.Category;
import ihm.bungleware.module.Module;
import ihm.bungleware.module.Modules;
import ihm.bungleware.setting.Setting;
import ihm.bungleware.ui.Hud;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;

/** Screen that allows the interactive configuration of modules. */
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
        for (Module mod : cat) {
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
        if (settingsModule == null)
            return;
        Module mod = settingsModule;

        ImGui.setNextWindowPos(x, y, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowSize(250.0f, 0.0f);
        ImBoolean popen = new ImBoolean(true);
        if (!ImGui.begin(mod.getName() + "###Settings", popen)) {
            if (!popen.get())
                settingsModule = null;
            ImGui.end();
            return;
        }

        for (Setting set : mod.getSettings()) {
            set.render();
            if (ImGui.isItemHovered() && set.getDesc() != null)
                ImGui.setTooltip(set.getDesc());
        }

        if (!popen.get())
            settingsModule = null;
        ImGui.end();
    }

    private void toolbar() {
        if (!ImGui.beginMainMenuBar())
            return;

        if (ImGui.beginMenu("Hud")) {
            if (ImGui.menuItem("Watermark", "", Hud.INSTANCE.enableWatermark)) {
                Hud.INSTANCE.enableWatermark = !Hud.INSTANCE.enableWatermark;
                Bungleware.instance().save();
            }
            if (ImGui.menuItem("Modules", "", Hud.INSTANCE.enableModules)) {
                Hud.INSTANCE.enableModules = !Hud.INSTANCE.enableModules;
                Bungleware.instance().save();
            }
            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }

    @Override
    public void renderGui() {
        // ImGui.showDemoWindow();
        toolbar();
        int xoff = 10;
        for (Category cat : Modules.getCategories()) {
            categoryWindow(cat, xoff, 40);
            xoff += 160;
        }
        settingsWindow(xoff, 40);
    }

    @Override
    public boolean shouldPauseGame() {
        return false;
    }
}
