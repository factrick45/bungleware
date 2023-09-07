package ihm.bungleware.setting;

import ihm.bungleware.Bungleware;

import imgui.ImGui;

public class FloatSetting extends AbstractSetting<Float> {
    private float min;
    private float max;

    public FloatSetting(String name, String desc, float def, float min, float max) {
        super(name, desc, def);
        this.min = min;
        this.max = max;
    }

    @Override
    public void render() {
        float[] out = {getVal()};
        if (ImGui.sliderFloat(getName(), out, min, max)) {
            if (out[0] > max)
                setVal(max);
            else if (out[0] < min)
                setVal(min);
            else
                setVal(out[0]);
        }
        if (ImGui.isItemDeactivatedAfterEdit())
            Bungleware.instance().save();
    }

    @Override
    public String serialize() {
        return Float.toString(getVal());
    }

    @Override
    public void deserialize(String string) {
        setVal(Float.parseFloat(string));
    }
}
