package ihm.bungleware.setting;

import imgui.ImGui;

public class SectionSetting implements Setting {
    private String name;

    public SectionSetting(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public void render() {
        ImGui.text(name);
        ImGui.separator();
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void deserialize(String string) {
        return;
    }
}
