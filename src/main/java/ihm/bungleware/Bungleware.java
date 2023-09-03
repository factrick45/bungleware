package ihm.bungleware;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;

import ihm.bungleware.module.Modules;
import ihm.bungleware.ui.imgui.ImGuiLoader;
import ihm.bungleware.ui.screens.MenuScreen;

import net.fabricmc.api.ModInitializer;
import org.lwjgl.input.Keyboard;

public class Bungleware implements ModInitializer {
    public static final Bungleware INSTANCE = new Bungleware();

    private MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {
        Modules.init();
    }

    public void onInitializePost() {
        ImGuiLoader.init();
    }

    public void onGameJoin() {
        Modules.onGameJoin();
    }

    public void onMouseButton(int button, boolean pressed) {
        ImGuiLoader.onMouseButton(button, pressed);
    }

    public void onMouseWheel(int scrolldelta) {
        ImGuiLoader.onMouseWheel(scrolldelta);
    }

    public void onKey(int key, boolean pressed) {
        if (pressed) {
            if (key == Keyboard.KEY_RSHIFT &&
                (mc.currentScreen == null ||
                 mc.currentScreen instanceof TitleScreen)
            ) {
                mc.setScreen(MenuScreen.INSTANCE);
            }
        }
    }

    public void onRenderPost() {
        ImGuiLoader.render();
    }

    public void onTick() {
        Modules.onTick();
    }
}
