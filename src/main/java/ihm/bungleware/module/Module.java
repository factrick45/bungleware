package ihm.bungleware.module;

import java.util.ArrayList;
import java.util.List;

import ihm.bungleware.setting.KeybindSetting;
import ihm.bungleware.setting.Setting;
import ihm.bungleware.utils.Utils;

public abstract class Module {
    private boolean enabled = false;
    private String name;
    private String desc;
    private List<Setting> settings = new ArrayList<>();
    private List<KeybindSetting> binds = new ArrayList<>();

    protected Module(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    protected void addSetting(Setting set) {
        settings.add(set);
        if (set instanceof KeybindSetting)
            binds.add((KeybindSetting)set);
    }

    protected void addSettings(Setting... sets) {
        for (var set : sets)
            addSetting(set);
    }

    protected void addDefaultBind() {
        addSetting(new KeybindSetting("Keybind", this, (mod, info) -> {
                    if (!info.toggleOnRelease) {
                        if (info.pressed)
                            mod.toggle();
                    } else {
                        mod.setEnabled(info.pressed);
                    }
        }));
    }

    public KeybindSetting[] getBinds() {
        return binds.toArray(new KeybindSetting[0]);
    }

    public Setting[] getSettings() {
        return settings.toArray(new Setting[0]);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isEnabled() {
        return enabled;
    }

    // FIXME: will not call when leaving game.
    // ClientPlayNetworkHandler.onDisconnect() does not appear to call unless
    // exiting a server.
    public void onDisabled() {
    }

    public void onEnabled() {
    }

    public void onTick() {
    }

    public void setEnabled(boolean value) {
        // prevents onEnabled and onDisabled being called multiple times
        if (value == enabled)
            return;
        enabled = value;
        if (Utils.isInGame()) {
            if (enabled)
                onEnabled();
            else
                onDisabled();
        }
    }

    public boolean toggle() {
        setEnabled(!enabled);
        return enabled;
    }
}
