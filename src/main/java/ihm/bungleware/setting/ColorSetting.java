package ihm.bungleware.setting;

import java.awt.Color;

import ihm.bungleware.Bungleware;

import imgui.ImGui;

public class ColorSetting extends AbstractSetting<Integer> {
    private float[] imColor = new float[3];

    public ColorSetting(String name, String desc, int color) {
        super(name, desc, color);
        new Color(color).getRGBColorComponents(imColor);
    }

    @Override
    public void render() {
        if (ImGui.colorEdit3(getName(), imColor))
            setVal(new Color(imColor[0], imColor[1], imColor[2]).getRGB());
        if (ImGui.isItemDeactivatedAfterEdit())
            Bungleware.instance().save();
    }

    @Override
    public String serialize() {
        return Integer.toHexString(getVal());
    }

    @Override
    public void deserialize(String string) {
        setVal(Integer.parseUnsignedInt(string, 16));
        new Color(getVal()).getRGBColorComponents(imColor);
    }
}
