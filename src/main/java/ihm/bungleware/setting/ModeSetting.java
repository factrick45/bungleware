package ihm.bungleware.setting;

import ihm.bungleware.Bungleware;

import imgui.ImGui;
import imgui.type.ImInt;

public class ModeSetting extends AbstractSetting<String> {
    private String[] modes;
    private ImInt selection = new ImInt(0);

    public ModeSetting(String name, String desc, String def, String... modes) {
        super(name, desc, def);
        this.modes = modes;
    }

    @Override
    public void setVal(String val) {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(val)) {
                selection.set(i);
                super.setVal(val);
                return;
            }
        }
    }

    @Override
    public void render() {
        if (ImGui.combo(getName(), selection, modes)) {
            super.setVal(modes[selection.get()]);
            Bungleware.instance().save();
        }
    }

    @Override
    public String serialize() {
        return getVal();
    }

    @Override
    public void deserialize(String string) {
        setVal(string);
    }
}
