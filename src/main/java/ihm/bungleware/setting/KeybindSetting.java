package ihm.bungleware.setting;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import ihm.bungleware.Bungleware;
import ihm.bungleware.module.Module;
import ihm.bungleware.utils.Utils;

import imgui.ImGui;
import org.lwjgl.input.Keyboard;

/** Performs an action when the set key is pressed. */
public class KeybindSetting extends AbstractSetting<Integer> {
    private boolean binding = false;
    private boolean toggleOnRelease = false;
    private Module parent;
    private BiConsumer<Module, Info> func;

    public static class Info {
        public int key;
        public boolean pressed;
        public boolean toggleOnRelease;
    }

    public KeybindSetting(String name, Module parent, BiConsumer<Module, Info> func) {
        super(name, null, 0);
        this.parent = parent;
        this.func = func;
    }

    public KeybindSetting(String name, int defkey, Module parent, BiConsumer<Module, Info> func) {
        super(name, null, defkey);
        this.parent = parent;
        this.func = func;
    }

    @Override
    public void render() {
        // window was previously closed
        if (ImGui.isWindowAppearing())
            binding = false;

        ImGui.text(getName());
        ImGui.separator();

        if (ImGui.button("Bind"))
            binding = !binding;
        ImGui.sameLine();
        if (!binding)
            ImGui.text(Keyboard.getKeyName(getVal()).toLowerCase());
        else
            ImGui.text("Press Del to clear...");

        if (ImGui.checkbox("Toggle on release", toggleOnRelease)) {
            toggleOnRelease = !toggleOnRelease;
            Bungleware.instance().save();
        }
    }

    public void onKey(int key, boolean pressed) {
        if (key == Keyboard.KEY_NONE)
            return;

        if (!binding) {
            if (Utils.isInGame() && !Utils.isPaused() && !Utils.isInScreen()) {
                if (key == getVal()) {
                    var info = new Info();
                    info.key = key;
                    info.pressed = pressed;
                    info.toggleOnRelease = toggleOnRelease;

                    func.accept(parent, info);
                }
            }
            return;
        }
        if (key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_DELETE)
            setVal(0);
        else
            setVal(key);
        binding = false;
        Bungleware.instance().save();
    }

    @Override
    public String serialize() {
        return Keyboard.getKeyName(getVal()) + ", " +
            (toggleOnRelease ? "true" : "false");
    }

    @Override
    public void deserialize(String string) {
        String[] split = string.split(", ", 2);
        setVal(Keyboard.getKeyIndex(split[0]));
        toggleOnRelease = split[1].equals("true");
    }
}
