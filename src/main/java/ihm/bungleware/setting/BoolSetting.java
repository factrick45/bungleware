package ihm.bungleware.setting;

import ihm.bungleware.Bungleware;

import imgui.ImGui;

public class BoolSetting extends AbstractSetting<Boolean> {
    public BoolSetting(String name, String desc, boolean val) {
        super(name, desc, val);
    }

    @Override
    public void render() {
        if (ImGui.checkbox(getName(), getVal())) {
            setVal(!getVal());
            Bungleware.INSTANCE.save();
        }
    }

    @Override
    public String serialize() {
        return getVal() ? "true" : "false";
    }

    @Override
    public void deserialize(String string) {
        if (!string.equals("true") && !string.equals("false"))
            throw new RuntimeException("Can't deserialize BoolSetting: " + string);
        setVal(string.equals("true"));
    }
}
