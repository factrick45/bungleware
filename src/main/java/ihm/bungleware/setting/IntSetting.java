package ihm.bungleware.setting;

import ihm.bungleware.Bungleware;

import imgui.ImGui;

public class IntSetting extends AbstractSetting<Integer> {
    private int min;
    private int max;

    public IntSetting(String name, String desc, int def, int min, int max) {
        super(name, desc, def);
        this.min = min;
        this.max = max;
    }

    @Override
    public void render() {
        int[] out = {getVal()};
        if (ImGui.sliderInt(getName(), out, min, max)) {
            setVal(out[0]);
            Bungleware.instance().save();
        }
    }

    @Override
    public String serialize() {
        return Integer.toString(getVal());
    }

    @Override
    public void deserialize(String string) {
        setVal(Integer.parseInt(string));
    }
}
