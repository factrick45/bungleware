package ihm.bungleware.ui;

import ihm.bungleware.module.Modules;
import ihm.bungleware.ui.imgui.ImGuiClient;
import ihm.bungleware.ui.imgui.ImGuiLoader;
import ihm.bungleware.utils.GuiUtils;
import ihm.bungleware.utils.Utils;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import static imgui.flag.ImGuiWindowFlags.*;

/** Draws a watermark and the list of enabled modules. */
public class Hud implements ImGuiClient {
    public static final Hud INSTANCE = new Hud();

    private String watermark;

    // invokes static initialization
    public static void init() {
    }

    private Hud() {
        watermark = "Bungleware " + Utils.getModVersion();
        ImGuiLoader.register(this);
    }

    @Override
    public void renderGui() {
        int winflags =
            NoBackground | NoDecoration | NoFocusOnAppearing | NoMouseInputs |
            NoNav;
        ImVec2 vpos = ImGui.getMainViewport().getPos();
        ImVec2 vsize = ImGui.getMainViewport().getSize();

        ImGui.setNextWindowPos(vpos.x, vpos.y, ImGuiCond.Always);
        ImGui.begin("HudWatermark", winflags);
        GuiUtils.textShadow(watermark);
        ImGui.end();

        ImGui.setNextWindowPos(
            vpos.x + vsize.x, vpos.y,
            ImGuiCond.Always, 1.0f, 0.0f
        );
        ImGui.setNextWindowSize(250.0f, 0.0f);
        ImGui.begin("HudModules", winflags);
        float posx = ImGui.getCursorPosX();
        Modules.forEach((mod, i) -> {
                if (!mod.isEnabled())
                    return;
                ImGui.setCursorPosX(
                    posx + ImGui.getContentRegionAvailX() -
                    ImGui.calcTextSize(mod.getName()).x
                );
                GuiUtils.textShadow(mod.getName(), GuiUtils.rainbow(i, 0.5f));
        });
        ImGui.end();
    }

    @Override
    public boolean guiEnabled() {
        return true;
    }
}
