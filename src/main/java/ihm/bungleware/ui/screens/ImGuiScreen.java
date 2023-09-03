package ihm.bungleware.ui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import ihm.bungleware.ui.imgui.ImGuiClient;
import ihm.bungleware.ui.imgui.ImGuiLoader;

/** A screen that renders ImGui elements when shown */
public class ImGuiScreen extends Screen implements ImGuiClient {
    protected ImGuiScreen() {
        super();
        ImGuiLoader.register(this);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.renderBackground();
    }

    @Override
    public void renderGui() {
    }

    @Override
    public boolean guiEnabled() {
        return MinecraftClient.getInstance().currentScreen == this;
    }
}
