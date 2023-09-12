package ihm.bungleware;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;

import ihm.bungleware.module.Modules;
import ihm.bungleware.save.Save;
import ihm.bungleware.ui.Hud;
import ihm.bungleware.ui.imgui.ImGuiLoader;
import ihm.bungleware.ui.screens.MenuScreen;
import ihm.bungleware.utils.Inputs;

import net.fabricmc.api.ModInitializer;
import org.lwjgl.input.Keyboard;

public class Bungleware implements ModInitializer {
    private static Bungleware INSTANCE;

    public Path savefile;
    public Path savepath;

    private MinecraftClient mc = MinecraftClient.getInstance();
    private boolean needsSave = false;

    public static Bungleware instance() {
        return INSTANCE;
    }

    public void save() {
        needsSave = true;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;

        Modules.init();

        savepath = mc.runDirectory.toPath().resolve("Bungleware").normalize();
        if (!Files.exists(savepath)) {
            try {
                Files.createDirectory(savepath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        savefile = savepath.resolve("main.cfg");
        Save save = new Save(savefile);
        boolean needsdef = !Files.exists(savefile);

        if (needsdef)
            save.save();
        else
            save.load();
    }

    public void onInitializePost() {
        ImGuiLoader.init();
        Hud.init();
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
        ImGuiLoader.onKey(key, pressed);
        Modules.onKey(key, pressed);
    }

    public void onRenderPost() {
        ImGuiLoader.render();
    }

    public void onTickPre() {
        Inputs.onTickPre();
    }

    public void onTick() {
        if (needsSave) {
            new Save(savefile).save();
            needsSave = false;
        }
        Modules.onTick();
    }
}
