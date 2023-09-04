package ihm.bungleware.setting;

import imgui.ImGui;

public class BoolSetting extends AbstractSetting<Boolean> {
    public BoolSetting(String name, String desc, boolean val) {
        super(name, desc, val);
    }

    @Override
    public void render() {
        if (ImGui.checkbox(getName(), getVal()))
            setVal(!getVal());
    }
}
